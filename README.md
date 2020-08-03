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

{"devId":"dev001","pk":"a0d89988c6b2419994951b076afd0150","DeviceId":"12e8ef8b5332fa2d","devName": "楼层电表001"}
{"devId":"BYQ001","pk":"0a118c82e1744cfd99b3c3afbff5a853","DeviceId":"30524b183f8e3ae3","devName": "变压器001"}
{"devId":"DXPD001","pk":"f0d9b9266ca54ad080ef8c590861bdcd","DeviceId":"35dd9a224718c971","devName": "地下配电001"}
{"devId":"DYBJ001","pk":"35234f5c84aa4b21b4b5d69ab90e8e70","DeviceId":"145bdd7a3c27d2d5","devName": "低压表具001"}


```

