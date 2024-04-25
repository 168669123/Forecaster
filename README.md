# Forecaster 运输车辆实时跟踪与预测系统

## 工程结构

#### 技术栈：Spring Boot、MySQL、Mybatis、Redis、Kafka

| 序号  | 名称  | 模块                        | 作用                                |
|:---:|:---:|:--------------------------|-----------------------------------|
|  1  | 表示层 | forecaster-interfaces     | 系统的启动入口，负责向用户显示信息和解释用户指令          |
|  2  | 应用层 | forecaster-application    | 协调和编排domain层原子方法，完成业务功能           |
|  3  | 领域层 | forecaster-domain         | 表达业务概念、业务状态信息以及业务规则               |
|  4  | 基础层 | forecaster-infrastructure | 为application层传递消息，为domain层提供持久化机制 |
|  5  | 门面层 | forecaster-facade         | 对外提供RPC接口服务                       |
|  6  | 通用层 | forecaster-common         | 提供与业务无关的工具方法                      |
|  7  | 测试层 | forecaster-test           | 单元测设与集成测试                         |

## 数据监控

* [监控面板](http://47.120.70.3:3000/d/GWK9al1Iz/monitor?orgId=1&refresh=10s)（账号：user，密码：1234）

## API服务

#### POST http://10.147.20.1:5555/estimateArrive/estimate

#### 请求体：

|    参数名称    |    参数含义    | 类型     | 必填  |
|:----------:|:----------:|:-------|-----|
|  carCode   |   运输车辆id   | string | 是   |
| longitude  |     经度     | double | 是   |
|  latitude  |     纬度     | double | 是   |
|  gpsTime   | 运输车辆信息发送时间 | date   | 是   |
| createTime | 运输车辆信息创建时间 | date   | 是   |
|  address   |     地址     | string | 是   |

## 环境配置

| 名称            | 版本     | 说明       |
|---------------|--------|----------|
| Java          | 1.8    |          |
| Maven         | 3.6.3  |          |
| Spring Boot   | 2.6.13 | 随 POM 版本 |
| Mybatis       | 2.1.4  | 随 POM 版本 |