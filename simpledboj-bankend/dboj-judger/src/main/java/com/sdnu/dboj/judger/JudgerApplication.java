package com.sdnu.dboj.judger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Properties;

/**
 * @Author: WangChen
 * @Date: 2023/3/8 12:10
 * @Version: 1.0
 * @Description: 评测机
 */

@EnableWebMvc
@EnableScheduling
@SpringBootApplication
public class JudgerApplication {
    public static void main(String[] args) {
//        isLinux();
        SpringApplication.run(JudgerApplication.class, args);
    }

    /**
     * 判断当前系统是否是Linux
     */
    private static void isLinux() {
        try{
            // 获得系统属性集
            Properties props = System.getProperties();
            String osName = (String) props.get("os.name");
            if (!osName.toLowerCase().contains("linux")) {
                System.out.println("OS isn't Linux, but " + osName);
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
