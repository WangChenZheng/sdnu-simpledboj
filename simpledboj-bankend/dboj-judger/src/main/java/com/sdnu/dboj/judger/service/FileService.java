package com.sdnu.dboj.judger.service;

import com.sdnu.dboj.judger.config.PathConfig;
import com.sdnu.dboj.judger.entity.JudgerModule;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: WangChen
 * @Date: 2023/4/2 9:58
 * @Version: 1.0
 * @Description:
 */

@Service
public class FileService {

    public void saveChunk(MultipartFile file, String filename, String filepath, int chunk) throws IOException {
        File dir = new File(filepath, filename);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, chunk + ".part");
        file.transferTo(dest);
    }

    public void mergeChunks(String filename, String filepath) throws IOException {
        File dir = new File(filepath, filename);
        File[] parts = dir.listFiles((dir1, name) -> name.endsWith(".part"));
        Arrays.sort(parts, Comparator.comparingInt(o -> Integer.parseInt(o.getName().split("\\.")[0])));
        File dest = new File(filepath, filename);
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            for (File part : parts) {
                Files.copy(part.toPath(), fos);
            }
        }
        for (File part : parts) {
            part.delete();
        }
    }

    public void saveFile(MultipartFile file, String filepath) throws IOException {
        // 获取上传的文件名
        String filename = file.getOriginalFilename();
        // 保存文件
        Path savePath = Paths.get(filepath, filename);
        Files.createDirectories(savePath.getParent());
        Files.write(savePath, file.getBytes());
    }

    public void saveFile(String fileBase64, String filepath) {
        File outputFile = new File(filepath);
        outputFile.getParentFile().mkdirs(); // 创建父目录
        byte[] testFileByte = Base64.getDecoder().decode(fileBase64);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(testFileByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getModuleZip(JudgerModule module) throws IOException {
        // 打包后的 ZIP 文件名
        File zipFile = Paths.get(PathConfig.ORIGIN_PATH, module.getModulePath(), module.getModuleName()+".zip").toFile();
        // 创建 ZIP 输出流
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        File fileDir = Paths.get(PathConfig.ORIGIN_PATH, module.getModulePath()).toFile();
        File[] files = fileDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".zip")) {
                    continue;
                }
                // 创建文件输入流
                FileInputStream fis = new FileInputStream(file);

                // 创建 ZIP 文件条目
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);

                // 将文件内容写入 ZIP 输出流
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                // 关闭文件输入流和 ZIP 文件条目
                zos.closeEntry();
                fis.close();
            }
            // 关闭 ZIP 输出流
            zos.close();
        }

        // 将 ZIP 文件返回给前端
        FileInputStream zipFis = new FileInputStream(zipFile);
        byte[] zipBytes = new byte[(int) zipFile.length()];
        zipFis.read(zipBytes);
        byte[] base64Bytes = Base64.getEncoder().encode(zipBytes);
        String base64String = new String(base64Bytes, "UTF-8");
        return base64String;
    }
}
