package com.sdnu.dboj.judger.config;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: WangChen
 * @Date: 2023/3/8 14:10
 * @Version: 1.0
 * @Description:
 */

@Configuration
public class PathConfig {

    public static final String WORKSPACE_DIR = "/workspace";
    public static final String LOG_DIR = "/log";
    public static final String DATA_DIR = "/data";
    public static final String ZIP_DIR = "/zip";

    public static final String PROJECT_PATH = "C:/Users/WangChen/Desktop/jartest";
    public static final String USER_SOURCE_PATH = "C:/Users/WangChen/Desktop/jartest/src";
    public static final String ORIGIN_PATH = "C:/Users/WangChen/Desktop/jartest/origin";
    public static final String TARGET_PATH = "C:/Users/WangChen/Desktop/jartest/out";
    public static final String JAR_PATH = "C:/Users/WangChen/Desktop/jartest/jar";
    public static final String TEST_PATH = "C:/Users/WangChen/Desktop/jartest/test";
    public static final String REAL_RESULT_PATH = "C:/Users/WangChen/Desktop/jartest/result/real";
    public static final String USER_RESULT_PATH = "C:/Users/WangChen/Desktop/jartest/result/user";

}
