package me.hekr.iotos.softgateway.core.common.dto;

import lombok.Data;

@Data
public class EnergyMeterReq {
  /** 能耗类别代码 */
  private String energyType;
  /** 查询依据的父类别，取值为Area或Customer，表示查询区域或客户下的数据 */
  private String parentType;
  /** 父Id，即区域Id或客户Code */
  private String parentId;
  /** 起始日期，格式为yyyy-MM-dd。查询时使用“>=” */
  private String beginDate;
  /** 结束日期，格式为yyyy-MM-dd。查询时使用“<” */
  private String endDate;
}
