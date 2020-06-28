package com.example.iotcloudgateway.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

/** 错误码 */
@Getter
public enum ErrorCode {
  /** 成功 */
  SUCCESS(0, "success"),
  /** 未知 */
  UNDEFINED(65535, "undefined"),
  /** 参数错误 */
  PARAM_INVALID(4000, "param invalid"),
  /** json 解析错误 */
  JSON_PARSE_ERROR(1001, "json parse error"),
  /** 指令（动作）不支持 */
  ACTION_NOT_SUPPORT(1002, "action not support"),
  /** 设备未登录 */
  DEVICE_NOT_LOGIN(1003, "device not login"),
  /** 设备不存在 */
  DEVICE_NOT_EXIST(1004, "device not exist"),
  /** 设备登录校验失败 */
  DEVICE_LOGIN_AUTH_FAILED(1005, "device login auth failed"),
  /** 设备不支持子设备 */
  DEVICE_NOT_SUPPORT_SUB_DEV(1006, "device not support sub device"),
  /** 设备添加拓扑结构不允许形成环 */
  DEVICE_TOPO_NOT_ALLOW_CIRCLE(1007, "device topo not allow circle"),
  /** 设备不在线 */
  DEVICE_NOT_ONLINE(1008, "device not online"),
  /** 设备被禁用 */
  DEVICE_DISABLED(1009, "device is disabled"),
  /** 设备没有这个子设备 */
  DEVICE_NOT_HAS_SUB_DEVICE(1010, "device not has the sub device"),
  /** 产品不存在 */
  PRODUCT_NOT_EXIST(1104, "product not exist"),
  /** 产品不支持远程配置 */
  PRODUCT_NOT_SUPPORT_REMOTE_CONFIG(1105, "product not support remote config"),
  /** 产品不存在 */
  PRODUCT_DELETED(1106, "product deleted"),
  /** imei 不合法 */
  DEVICE_IMEI_INVALID(1107, "device imei invalid"),
  /** imei 已经存在 */
  DEVICE_IMEI_EXIST(1108, "device imei exist"),

  /** rule select 参数不正确 */
  RULE_SELECT_PARAM_INVALID(1109, "rule select param not valid"),
  /** 规则表达式不合法 */
  RULE_EXPRESSION_INVALID(1110, "rule expression not valid"),
  /** 规则不存在 */
  RULE_NOT_FOUND(1111, "rule not found"),
  /** 规则 目标地址 http 不合法 */
  RULE_DEST_HTTP_NOT_VALID(1112, "rule dest http url not valid"),
  /** 协议参数不合法（包括cmd） */
  MODEL_DATA_PARAM_INVALID(1113, "model params not valid"),
  /** 配额已经存在 */
  QUOTA_EXIST(1114, "quota exist"),
  /** 配额不存在 */
  QUOTA_NOT_FOUND(1115, "quota not found"),
  /** 升级任务启用冲突 */
  UPGRADE_TASK_ENABLE_CONFLICT(1116, "upgrade task enable conflict"),
  /** 升级任务启用存在循环 */
  UPGRADE_TASK_ENABLE_CYCLE(1117, "upgrade task has cycle"),
  /** 升级任务不存在 */
  UPGRADE_TASK_NOT_FOUND(1118, "upgrade task not found"),
  /** 么有可用的远程配置 */
  AVAILABLE_REMOTE_CONFIG_NOT_FOUND(1119, "available remote config not found"),
  /** 配额不足 */
  QUOTA_INSUFFICIENT(1120, "quota insufficient"),

  /** 协议参数名字重复 */
  MODEL_DATA_PARAM_NAME_CONFLICT(1121, "model data param name conflict"),

  /** 协议参数标识符重复 */
  MODEL_DATA_PARAM_IDENTIFIER_CONFLICT(1122, "model data param identifier conflict"),

  /** 协议命令名字符重复 */
  MODEL_DATA_CMD_NAME_CONFLICT(1123, "model data cmd name conflict"),

  /** 协议命令标识符重复 */
  MODEL_DATA_CMD_IDENTIFIER_CONFLICT(1124, "model data cmd identifier conflict"),

  /** 设备id不合法 */
  DEV_ID_INVALID(1125, "devId invalid"),

  /** imei 已经存在 */
  DEV_ID_EXIST(1126, "device id exist"),

  /** 产品下还存在设备，不允许删除产品 */
  PRODUCT_NOT_ALLOW_DELETE_DEVICE_EXIST(1127, "product has devices not allow to delete"),

  /** 子设备不允许直连 */
  DEVICE_NOT_ALLOW_TO_DIRECT_CONNECT(1128, "device(sub) not allow to direct connect"),

  /** 设备所属网关不在线 */
  DEVICE_GATEWAY_NOT_ONLINE(1129, "device's gateway not online"),

  /** 产品编解码插件（脚本）未运行，可能不存在或者编译失败 */
  PRODUCT_SCRIPT_NOT_RUNNING(1130, "product script not running or not exist"),

  /** 产品编码插件编码错误 */
  PRODUCT_SCRIPT_ENCODE_ERROR(1131, "product script encode error"),

  /** 产品编码插件解码码错误 */
  PRODUCT_SCRIPT_DECODE_ERROR(1132, "product script decode error"),

  /** 产品不支持脚本 */
  PRODUCT_NOT_SUPPORT_SCRIPT(1133, "product not support script"),

  /** 脚本解码返回值不正确 */
  PRODUCT_SCRIPT_RETURN_VALUE_INVALID(1134, "product script return value invalid"),

  /** 产品脚本编译失败 */
  PRODUCT_SCRIPT_COMPILE_ERROR(1135, "product script compile error"),

  /** 设备不支持批量上报数据，只有网关支持 */
  DEVICE_NOT_SUPPORT_BATCH_DEV_SEND(
      1136, "device not support batch dev send, only gateway support"),

  /** * 设备拓扑关系不存在 */
  DEVICE_TOPO_NOT_EXIST(1137, "device topo not exist"),

  /** payload（负载内容）为空 */
  PAYLOAD_IS_EMPTY(1138, "payload is empty"),

  /** 设备登录太频繁，在间隔时间范围内进行了重复登录 */
  DEVICE_LOGIN_TOO_FREQUENTLY(1139, "device login too frequently"),
  /** 系统内部错误 */
  INTERNAL_ERROR(500, "internal error");
  private static final Map<Integer, ErrorCode> ERROR_CODE_MAP;

  static {
    ERROR_CODE_MAP =
        Arrays.stream(ErrorCode.values()).collect(toMap(ErrorCode::getCode, Function.identity()));
  }

  /** 错误码 */
  private int code;
  /** 简要描述 */
  private String desc;

  ErrorCode(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static ErrorCode of(int code) {
    return ERROR_CODE_MAP.getOrDefault(code, UNDEFINED);
  }
}
