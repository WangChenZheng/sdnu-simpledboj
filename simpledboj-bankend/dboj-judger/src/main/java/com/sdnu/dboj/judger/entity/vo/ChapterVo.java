package com.sdnu.dboj.judger.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: WangChen
 * @Date: 2023/4/1 19:01
 * @Version: 1.0
 * @Description:
 */


@Data
public class ChapterVo {

   private String id;

   private String label;

   private String details;

   private String modulePath;

   private List<ChapterVo> children;

   private int sort;

   private int caseNum;

   private int submitNum;

   private int acceptNum;
}
