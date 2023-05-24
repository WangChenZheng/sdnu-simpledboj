package com.sdnu.dboj.judger.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.config.PathConfig;
import com.sdnu.dboj.judger.entity.JudgerModule;
import com.sdnu.dboj.judger.entity.JudgerRecord;
import com.sdnu.dboj.judger.entity.JudgerTestProcedure;
import com.sdnu.dboj.judger.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author: WangChen
 * @Date: 2023/3/13 19:04
 * @Version: 1.0
 * @Description:
 */

@Service
public class ExecService {

    @Autowired
    private JudgerModuleService moduleService;
    @Autowired
    private JudgerTestProcedureService testProcedureService;
    @Autowired
    private JudgerRecordService recordService;

    public boolean execTest(String moduleId, JudgerRecord record) {
        record.setStatus(1);
        record.setCosttime(System.currentTimeMillis());
        JudgerModule module = moduleService.getById(moduleId);
        recordService.saveOrUpdate(record);
        return compile2Class(module, record);
    }

    private boolean compile2Class(JudgerModule module, JudgerRecord record) {
        String modulePath = module.getModulePath();
        String sourcePath = Paths.get(PathConfig.USER_SOURCE_PATH, record.getUserId(), modulePath) + File.separator + "*.java";
        String javacCommend = "javac -encoding utf-8 " + sourcePath + " -d " + PathConfig.TARGET_PATH;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execCommend(javacCommend, record, "编译");
        record.setStatus(2);
        recordService.saveOrUpdate(record);
        return pack2Jar(module, record);
    }

    private boolean pack2Jar(JudgerModule module, JudgerRecord record) {
        String modulePath = module.getModulePath();
        String jarName = modulePath.replace("/", "_")+".jar";
        String jarPath = Paths.get(PathConfig.JAR_PATH, jarName).toString();
        String jarCommend = "jar cfM " + jarPath + " -C " + PathConfig.TARGET_PATH + " " + modulePath;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execCommend(jarCommend, record, "打包");
        record.setStatus(3);
        recordService.saveOrUpdate(record);
        return testProcedure(module, record);
    }

    private boolean testProcedure(JudgerModule module, JudgerRecord record) {
        String testJar = module.getModulePath().replace("/", "_")+".jar";
        String testJarPath = Paths.get(PathConfig.JAR_PATH, testJar).toString();
        String moduleId = module.getId();
        QueryWrapper<JudgerTestProcedure> testProcedureQueryWrapper = new QueryWrapper<JudgerTestProcedure>();
        testProcedureQueryWrapper.eq("module_id", moduleId);
        List<JudgerTestProcedure> testProcedureList = testProcedureService.list();
        boolean flag = false;
        for (JudgerTestProcedure testProcedure : testProcedureList) {
            String testProcedurePath = testProcedure.getPath();
            testProcedurePath = Paths.get(PathConfig.TEST_PATH,module.getModulePath(), testProcedurePath, testProcedure.getFilename()).toString();
//            execCommend("javac -encoding utf-8 -classpath " + testJarPath + " " + testProcedurePath, record, "编译测试程序");
//            String packagePath = Paths.get(PathConfig.TEST_PATH,module.getModulePath()).toString();
            String testCommend = "java -classpath " + testJarPath  + " " +testProcedurePath;
            execCommend(testCommend, record, "测试程序");
            record.setErrorcase(testProcedure.getFilename());
            flag = judge(testProcedure, module);
            if (!flag) {
                return false;
            }
        }
        record.setStatus(4);
        record.setResult("成功");
        record.setCosttime(System.currentTimeMillis() - record.getCosttime());
        record.setErrorcase("空");
        recordService.saveOrUpdate(record);
        return flag;
    }

    private boolean judge(JudgerTestProcedure testProcedure, JudgerModule module) {
        String realFilePath = Paths.get(PathConfig.REAL_RESULT_PATH, module.getModulePath(), testProcedure.getResultPath()).toString();
        String userFilePath = Paths.get(PathConfig.USER_RESULT_PATH, module.getModulePath(), testProcedure.getUserFilePath()).toString();
        boolean flag = false;
        try {
            flag = FileUtils.fileCompare(realFilePath, userFilePath);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        if (!flag) {
            System.out.println("judge result: " + testProcedure.getModuleId() + " is failure");
            return false;
        }
        System.out.println("judge result: " + testProcedure.getModuleId() + " is right");
        return flag;
    }

    private void execCommend(String commend, JudgerRecord record, String message) {
//        String move2ProjectPath = "cd " + PathConfig.PROJECT_PATH;
//        String execCommend = move2ProjectPath + " && " + commend;
        System.out.println("exec commend: " + commend);
        try {
            // 创建 ProcessBuilder 实例，并设置要执行的命令
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", commend);
            // 设置工作目录
            pb.directory(new File("D:/"));
            // 启动进程
            Process process = pb.start();
            // 读取命令的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            // 等待命令执行完成并输出返回值
            int exitCode = process.waitFor();
            if (0 == exitCode) {
                // 执行成功
                System.out.println("code: " + exitCode + ", message: success");
            } else {
                // 执行失败
                record.setStatus(-1);
                record.setCosttime(System.currentTimeMillis() - record.getCosttime());
                record.setResult(message+"失败");
                recordService.saveOrUpdate(record);
                System.out.println("code: " + exitCode + ", message: \n" + result);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
