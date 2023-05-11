package com.sdnu.dboj.judger.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.entity.JudgerRecord;
import com.sdnu.dboj.judger.service.ExecService;
import com.sdnu.dboj.judger.service.JudgerRecordService;
import com.sdnu.dboj.judger.utils.JudgeQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangChen
 * @Date: 2023/5/10 3:00
 * @Version: 1.0
 * @Description:
 */


@Component
public class JudgeSchedule {

    @Autowired
    private ExecService execService;
    @Autowired
    private JudgerRecordService recordService;

    @Scheduled(fixedDelay = 10000) // 每隔10秒执行一次
    public void judge() {
        if (!JudgeQueue.isEmpty()) {
            Map<Object, Object> elem = JudgeQueue.poll();
            String moduleId = (String) elem.get("moduleId");
            JudgerRecord record = (JudgerRecord) elem.get("record");
            execService.execTest(moduleId, record);
        }
    }

    @Scheduled(fixedDelay = 30000) // 每隔30秒执行一次
    public void queryUnStart() {
        QueryWrapper<JudgerRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("status", 0);
        List<JudgerRecord> list = recordService.list(recordQueryWrapper);
        for (JudgerRecord judgerRecord : list) {
            String moduleId = judgerRecord.getModuleId();
            JudgeQueue.add(moduleId, judgerRecord);
        }
    }
}
