package com.sdnu.dboj.judger.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: WangChen
 * @Date: 2023/3/13 20:50
 * @Version: 1.0
 * @Description:
 */

public class FileUtils {

    public static boolean fileCompare(String file1Path, String file2Path) throws IOException, NoSuchAlgorithmException {
        File file1 = new File(file1Path);
        File file2 = new File(file2Path);

        String hash1 = getFileHash(file1);
        String hash2 = getFileHash(file2);

        if (hash1.equals(hash2)) {
            return true;
        }
        return false;
    }

    private static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        FileInputStream inputStream = new FileInputStream(file);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] bytesBuffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
            digest.update(bytesBuffer, 0, bytesRead);
        }

        byte[] hash = digest.digest();
        inputStream.close();

        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
