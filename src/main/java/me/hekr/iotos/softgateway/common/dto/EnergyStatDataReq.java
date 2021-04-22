package me.hekr.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/31
 */
@Data
public class EnergyStatDataReq {
  /** 能耗类别代码 */
  private String energyType;
  /** 查询依据的父类别，取值为Area或Customer，表示查询区域或客户下的数据 */
  private String parentType;
  /** 父Id，即区域Id或客户Code */
  private String parentId;
  /** 日期范围类别，允许为Hour、Day、Month、Year */
  private String dateRange;
  /** 起始日期，格式为yyyy-MM-dd。查询时使用“>=” */
  private String beginDate;
  /** 结束日期，格式为yyyy-MM-dd。查询时使用“<” */
  private String endDate;
}
