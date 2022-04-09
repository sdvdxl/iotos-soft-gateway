# FAQ

## 没有配置映射设备信息，请在远程配置中进行配置，设备信息

> 没有配置映射设备信息，请在远程配置中进行配置，设备信息 xxx

出现这个问题是因为 根据 `DeviceMapper.getProps` 返回的数据未能找到一个远程配置中的设备 

### 解决方案

1. 根据远程配置信息，返回正确的 `Props getProps()`
2. 根据 `Props getProps()` 配置正确的远程配置参数


4. 远程配置中映射关系配置错误，映射的字段要和代码中一致


## Spring

允许循环依赖，需要配置：

spring.main.allow-circular-references=true