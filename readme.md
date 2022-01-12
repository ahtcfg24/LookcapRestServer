# 任务目标
实现一个简单的Rest风格HTTP调用网关

# 模块说明
## lookcap.annotation
链接网关和Rest服务的Java注解类
* RestPath 该注解标注具体Rest请求的URI路径。
  * 该注解可以用于标记类，此时的注解值为该类下所有接口的前缀。
  * 只有被该注解标记的类及方法才能响应对应的HTTP请求。
## lookcap.service
REST服务类
* CustomerService 一个用于测试的Rest服务类
## lookcap.gateway
实现对HTTP请求的响应并调用响应的Rest服务方法

# 规格要求
* 在lookcap.gateway下实现主要的网关逻辑
* 代码实现只能使用JDK及JavaWeb标准类库(Java对象和JSON之间的序列化、反序列化除外)
* 只考虑POST类型请求，且请求和应答的Content-Type均为application/json;charset=UTF-8
* 交付结果可以直接在Jetty或Tomcat容器下启动运行
  * 能够正常运行下文参考用例(50)
  * 代码结构整洁规范，思路清晰，不过设计(10)
  * gateway包和service包不存在直接耦合(10)
  * service包下新增服务类能够自动识别，无需额外配置文件(10)
  * 对边界条件和异常情况进行优雅的处理(20)

# 参考用例
## 添加其他Rest服务类
略

## 创建张一
```
# 请求
curl -X POST 'http://localhost:8080/customer/create' \
-H 'Content-Type: application/json;charset=UTF-8' \
--data-raw '{"name":"张一","age":18}'

# 应答
{"id":1,"age":18,"name":"张一","cts":"2021-12-30T09:09:01.752+0000"}
```
## 创建张二
```
# 请求
curl -X POST 'http://localhost:8080/customer/create' \
-H 'Content-Type: application/json;charset=UTF-8' \
--data-raw '{"name":"张二","age":19}'

# 应答
{"id":2,"age":19,"name":"张二","cts":"2021-12-30T09:10:12.053+0000"}
```
## 创建张三
```
# 请求
curl -X POST 'http://localhost:8080/customer/create' \
-H 'Content-Type: application/json;charset=UTF-8' \
--data-raw '{"name":"张三","age":20}'

# 应答
{"id":3,"age":20,"name":"张三","cts":"2021-12-30T09:11:07.697+0000"}
```
## 查看张二
```
# 请求
curl -X POST 'http://localhost:8080/customer/get' \
-H 'Content-Type: application/json;charset=UTF-8' \
--data-raw '{"id":2}'

# 应答
{"id":2,"age":19,"name":"张二","cts":"2021-12-30T09:10:12.053+0000"}
```
## 列表全部
```
# 请求
curl -X POST 'http://localhost:8080/customer/list' \
-H 'Content-Type: application/json;charset=UTF-8'

# 应答
[{"id":1,"age":18,"name":"张一","cts":"2021-12-30T09:09:01.752+0000"},{"id":2,"age":19,"name":"张二","cts":"2021-12-30T09:10:12.053+0000"},{"id":3,"age":20,"name":"张三","cts":"2021-12-30T09:11:07.697+0000"}]
```
