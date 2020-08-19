## 对接相关说明

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
