## GBT32960 Client platform 功能实现
#### 据说，公司最近可能要搭建电动汽车远程服务与管理系统，那么客户端的应用肯定也少不了，而网络上相关的实现简直是，少！既然没有轮子那么咱就自己造，注意，注意：由于没有条件，该代码没有经过任务测试 〒︵〒

##功能
* 通信协议已基本完成，发包，解包，粘包已处理
* 模拟车辆登入
* 数据发送会尝试三次，失败后会储存到数据库
* 自动补发7天内发送失败的数据


## 说明
目前无法得到CAN数据，所以实时上传的数据都没有办法得到，后续有时间了会考虑模拟数据上传。
