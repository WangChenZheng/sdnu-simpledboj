package com.sdnu.dboj.judger.service;

import com.sdnu.dboj.judger.entity.JudgerModuleFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdnu.dboj.judger.entity.vo.ModuleFileVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-02 10:24:52
 */
public interface JudgerModuleFileService extends IService<JudgerModuleFile> {

    List<ModuleFileVo> getModuleFileList(String moduleId);
}
