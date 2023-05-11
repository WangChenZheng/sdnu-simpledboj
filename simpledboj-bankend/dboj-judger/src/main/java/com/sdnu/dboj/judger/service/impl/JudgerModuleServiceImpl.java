package com.sdnu.dboj.judger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.entity.JudgerModule;
import com.sdnu.dboj.judger.entity.vo.ChapterVo;
import com.sdnu.dboj.judger.mapper.JudgerModuleMapper;
import com.sdnu.dboj.judger.service.JudgerModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-03-15 08:44:03
 */
@Service
public class JudgerModuleServiceImpl extends ServiceImpl<JudgerModuleMapper, JudgerModule> implements JudgerModuleService {

    @Override
    public List<ChapterVo> moduleTree() {
        QueryWrapper<JudgerModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<JudgerModule> moduleList = this.list(queryWrapper);
        List<ChapterVo> chapterVoTreeList = new ArrayList<>();
        for (JudgerModule module : moduleList) {
            if (StringUtils.isEmpty(module.getParentId())) {
                // 父级节点
                ChapterVo chapterVo = new ChapterVo();
                BeanUtils.copyProperties(module, chapterVo);
                chapterVo.setLabel(module.getModuleName());
                ArrayList<ChapterVo> subList = new ArrayList<>();
                for (JudgerModule subModule : moduleList) {
                    if (StringUtils.equals(module.getId(), subModule.getParentId())) {
                        // 子元素
                        ChapterVo subChapterVo = new ChapterVo();
                        BeanUtils.copyProperties(subModule, subChapterVo);
                        subChapterVo.setLabel(subModule.getModuleName());
                        subList.add(subChapterVo);
                    }
                }
                chapterVo.setChildren(subList);
                chapterVoTreeList.add(chapterVo);
            }
        }
        return chapterVoTreeList;
    }
}
