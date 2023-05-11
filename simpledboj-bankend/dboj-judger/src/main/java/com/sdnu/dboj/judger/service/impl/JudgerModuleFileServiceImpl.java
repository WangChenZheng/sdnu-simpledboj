package com.sdnu.dboj.judger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.entity.JudgerModuleFile;
import com.sdnu.dboj.judger.entity.vo.ModuleFileVo;
import com.sdnu.dboj.judger.mapper.JudgerModuleFileMapper;
import com.sdnu.dboj.judger.service.JudgerModuleFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-02 10:24:52
 */
@Service
public class JudgerModuleFileServiceImpl extends ServiceImpl<JudgerModuleFileMapper, JudgerModuleFile> implements JudgerModuleFileService {

    @Override
    public List<ModuleFileVo> getModuleFileList(String moduleId) {
        QueryWrapper<JudgerModuleFile> moduleFileQueryWrapper = new QueryWrapper<>();
        moduleFileQueryWrapper.eq("module_id", moduleId);
        List<JudgerModuleFile> moduleFileList = baseMapper.selectList(moduleFileQueryWrapper);
        ArrayList<ModuleFileVo> moduleFileVoList = new ArrayList<>();
        for (JudgerModuleFile moduleFile : moduleFileList) {
            ModuleFileVo moduleFileVo = new ModuleFileVo();
            moduleFileVo.setId(moduleFile.getId());
            moduleFileVo.setFilename(moduleFile.getFilename());
            moduleFileVo.setDetails(moduleFile.getDetails());
            moduleFileVo.setFilepath(moduleFile.getFilepath());
            moduleFileVoList.add(moduleFileVo);
        }
        return moduleFileVoList;
    }
}
