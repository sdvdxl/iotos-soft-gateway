## 对接相关说明

### 帧格式

|名称|长度|内容|
|:---:|:---:|:---:|
|起始位|1|0xFF（起始位固定）
|控制命令|1|CMD(命令编码)
|数据长度|1|LEN（数据域的数据长度）
|数据域|L|MAC 地址+设备类型+防区数量+DATA 等，根据不同命令组合成不同数据域。
|校验码|1|异或校验（起始位至数据域按字节异或运算）

>说明：
>1. MAC 地址指网络 MAC 地址， MAC 地址不可重复，用于区分不同设备依据;
>2. 数据域指根据不同的控制命令，协议包含的数据长度及数据内容不尽相同;
>3. 设备定时主动推送报警信息，可充当 TCP 心跳包，若发生报警时，即时推送。


### 设备类型
|名称|编号|说明|
|:---:|:---:|:---:|
|智能终端|0x01|张力及脉冲混用键盘
|脉冲设备|0x03|脉冲主机（内置网络模块）

### 控制指令
|命令名称|命令编码|数据长度|数据内容|说明|
|:---:|:---:|:---:|:---:|:---:|
|状态查询|0x01|0|无|查询设备下的所有防区状态，此命令可不下发，周界设备定时主动上报状态。|
|布撤防设置|0x02|2|防区编号+布撤防标志|防区布防与撤防，布撤防标志定义：0x01=布防，0x00=撤防|

### 返回控制指令
|命令名称|命令编码|数据长度|数据内容|说明|
|:---:|:---:|:---:|:---:|:---:|
|状态查询|0x01|8+N|MAC 地址+设备类型+防区数量+防区1状态+防区2状态+…+防区N状态|MAC 地址为 6 个字节 16 进制数，设备类型，N=防区数量，防区状态详见下表，周界设备定时主动上报状态。|
|布撤防设置|0x02|3|防区编号+布撤防状态+成功标志|布撤防状态定义：0x01=布防，0x00=撤防<br>成功标志定义：0x01=成功，0x00=失败|
>注意
>防区数量超过 64 个时，分两包上传，第一包数据防区数量为 64，第二包防区数量为实际数量，防区状态从第 65 个防区开始，以此区分两包数据。

### 防区状态
|状态|编号|说明|
|:---:|:---:|:---:|
|布防|0x01|通用|
|撤防|0x02|通用|
|旁路|0x03|通用|
|电源故障|0x04|通用|
|通讯故障|0x05|通用|
|防拆报警|0x06|通用|
|断线报警|0x09|张力及脉冲适用|
|短路报警|0x0B|脉冲适用|
|单线触摸报警|0x0C|脉冲适用|



### 模拟物模型列表

|参数名|defineIndex|
|:---:|:---:|
|设备在离线|1
|A相电压|101
|B相电压|102
|C相电压|103
|AB相电压|104
|BC相电压|105
|CA相电压|106
|A相电流|107
|B相电流|108
|C相电流|109
|总功率|110
|正向总有功电能|111
|有功功率|203
|无功功率|204
|视在功率|205
|功率因素|206
|A相绕组温度|207
|B相绕组温度|208
|C相绕组温度|209
|电流报警|210
|欠电压报警|211
|高温报警|212
|过负载报警|213
|日统计电能|T_111

物模型命名规则：
```DI+_+defineIndex```，例如设备在离线为：```DI_1```；
因此统计类前缀为```DI_T_``,例如统计电能为：```DI_T_111```。


### 远程配置示例

```json

{"devId":"dev001","areaNum":1,"devName": "一号防区"}
{"devId":"dev002","areaNum":2,"devName": "二号防区"}


```

### 相关物模型

#### 楼层电表

```json
{
  "params": [
    {
      "name": "设备在离线",
      "param": "online",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 1,
            "desc": "在线"
          },
          {
            "value": 0,
            "desc": "离线"
          }
        ]
      }
    },
    {
      "name": "A相电压",
      "param": "DI_101",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电压",
      "param": "DI_102",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电压",
      "param": "DI_103",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "AB相电压",
      "param": "DI_104",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "BC相电压",
      "param": "DI_105",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "CA相电压",
      "param": "DI_106",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "A相电流",
      "param": "DI_107",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电流",
      "param": "DI_108",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电流",
      "param": "DI_109",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "总功率",
      "param": "DI_110",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "正向总有功电能",
      "param": "DI_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "日统计电能",
      "param": "DI_T_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 9999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    }
  ],
  "cmds": [
    {
      "name": "上报参数",
      "cmd": "reportValue",
      "frameType": "DEV_UP",
      "params": [
        "DI_101",
        "DI_102",
        "DI_103",
        "DI_104",
        "DI_105",
        "DI_106",
        "DI_107",
        "DI_108",
        "DI_109",
        "DI_110",
        "DI_111",
        "DI_T_111",
        "online"
      ]
    }
  ]
}
```

#### 地下室低压表具

```json
{
  "params": [
    {
      "name": "设备在离线",
      "param": "online",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 1,
            "desc": "在线"
          },
          {
            "value": 0,
            "desc": "离线"
          }
        ]
      }
    },
    {
      "name": "A相电压",
      "param": "DI_101",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电压",
      "param": "DI_102",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电压",
      "param": "DI_103",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "AB相电压",
      "param": "DI_104",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "BC相电压",
      "param": "DI_105",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "CA相电压",
      "param": "DI_106",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "A相电流",
      "param": "DI_107",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电流",
      "param": "DI_108",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电流",
      "param": "DI_109",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "总功率",
      "param": "DI_110",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "正向总有功电能",
      "param": "DI_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "电流报警",
      "param": "DI_210",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "欠电压报警",
      "param": "DI_211",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "过负载报警",
      "param": "DI_213",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "日统计电能",
      "param": "DI_T_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 9999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    }
  ],
  "cmds": [
    {
      "name": "上报参数",
      "cmd": "reportValue",
      "frameType": "DEV_UP",
      "params": [
        "DI_101",
        "DI_102",
        "DI_103",
        "DI_104",
        "DI_105",
        "DI_106",
        "DI_107",
        "DI_108",
        "DI_109",
        "DI_110",
        "DI_111",
        "DI_210",
        "DI_211",
        "DI_213",
        "DI_T_111",
        "online"
      ]
    }
  ]
}
```

#### 地下室配电

```json
{
  "params": [
    {
      "name": "设备在离线",
      "param": "online",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 1,
            "desc": "在线"
          },
          {
            "value": 0,
            "desc": "离线"
          }
        ]
      }
    },
    {
      "name": "A相电压",
      "param": "DI_101",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电压",
      "param": "DI_102",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电压",
      "param": "DI_103",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "A相电流",
      "param": "DI_107",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相电流",
      "param": "DI_108",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相电流",
      "param": "DI_109",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "总功率",
      "param": "DI_110",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "正向总有功电能",
      "param": "DI_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "有功功率",
      "param": "DI_203",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "无功功率",
      "param": "DI_204",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "视在功率",
      "param": "DI_205",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "功率因素",
      "param": "DI_206",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "电流报警",
      "param": "DI_210",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "欠电压报警",
      "param": "DI_211",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "高温报警",
      "param": "DI_212",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "过负载报警",
      "param": "DI_213",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    },
    {
      "name": "日统计电能",
      "param": "DI_T_111",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 9999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    }
  ],
  "cmds": [
    {
      "name": "上报参数",
      "cmd": "reportValue",
      "frameType": "DEV_UP",
      "params": [
        "DI_101",
        "DI_102",
        "DI_103",
        "DI_107",
        "DI_108",
        "DI_109",
        "DI_110",
        "DI_111",
        "DI_203",
        "DI_204",
        "DI_205",
        "DI_206",
        "DI_210",
        "DI_211",
        "DI_212",
        "DI_213",
        "DI_T_111",
        "online"
      ]
    }
  ]
}
```

#### 变压器
```json
{
  "params": [
    {
      "name": "A相绕组温度",
      "param": "DI_207",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "B相绕组温度",
      "param": "DI_208",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "C相绕组温度",
      "param": "DI_209",
      "dataType": "FLOAT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "RANGE",
        "max": {
          "value": 999999,
          "desc": "最大值"
        },
        "min": {
          "value": 0,
          "desc": "最小值"
        }
      }
    },
    {
      "name": "高温报警",
      "param": "DI_212",
      "dataType": "INT",
      "frameType": "DEV_UP",
      "checkType": {
        "type": "ENUM",
        "values": [
          {
            "value": 0,
            "desc": "正常"
          },
          {
            "value": 1,
            "desc": "告警"
          }
        ]
      }
    }
  ],
  "cmds": [
    {
      "name": "上报参数",
      "cmd": "reportValue",
      "frameType": "DEV_UP",
      "params": [
        "DI_207",
        "DI_208",
        "DI_209",
        "DI_212"
      ]
    }
  ]
}
```
