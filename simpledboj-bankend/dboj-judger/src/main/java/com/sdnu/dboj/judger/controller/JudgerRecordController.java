package com.sdnu.dboj.judger.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.entity.JudgerRecord;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.entity.vo.RecordVo;
import com.sdnu.dboj.judger.service.JudgerModuleService;
import com.sdnu.dboj.judger.service.JudgerRecordService;
import com.sdnu.dboj.judger.service.UcenterUserService;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-06 04:04:58
 */
@RestController
@RequestMapping("/judger/record")
public class JudgerRecordController {

    @Autowired
    private JudgerRecordService recordService;
    @Autowired
    private JudgerModuleService moduleService;
    @Autowired
    private UcenterUserService userService;

    @GetMapping("getRecordList")
    public Result getRecordList(HttpServletRequest request) {
        String token = request.getHeader("Token");
        String username = JwtUtils.getIdByJwtToken(token);
        QueryWrapper<UcenterUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        UcenterUser user = userService.getOne(userQueryWrapper);
        QueryWrapper<JudgerRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("user_id", user.getId());
        List<JudgerRecord> recordList = recordService.list(recordQueryWrapper);
        ArrayList<RecordVo> recordVoList = new ArrayList<>();
        for (JudgerRecord record : recordList) {
            RecordVo recordVo = new RecordVo();
            BeanUtils.copyProperties(record, recordVo);
            String moduleName = moduleService.getById(record.getModuleId()).getModuleName();
            recordVo.setModuleName(moduleName);
            recordVoList.add(recordVo);
        }
        return Result.ok().data("list", recordVoList);
    }
}

