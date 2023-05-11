package com.sdnu.dboj.judger.service;

import com.sdnu.dboj.judger.entity.JudgerModule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdnu.dboj.judger.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-03-15 08:44:03
 */
public interface JudgerModuleService extends IService<JudgerModule> {

   /**
    * 树形查询课程章节
    * @return 章节列表
    */
   List<ChapterVo> moduleTree();
}
