package com.sdnu.dboj.judger.utils;

import com.sdnu.dboj.judger.entity.JudgerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: WangChen
 * @Date: 2023/5/10 3:02
 * @Version: 1.0
 * @Description:
 */


public class JudgeQueue {

    private static ConcurrentLinkedQueue<Map<Object, Object>> queue = new ConcurrentLinkedQueue<>();

    public static void add(String moduleId, JudgerRecord record) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("moduleId", moduleId);
        map.put("record", record);
        queue.offer(map);
    }

    public static Map<Object, Object> poll() {
        return queue.poll();
    }

    public static boolean isEmpty() {
        return queue.isEmpty();
    }
}
