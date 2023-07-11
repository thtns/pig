## 启动服务端
> nohup java -Dfile.encoding=utf-8 -jar pig-register.jar > /dev/null 2>&1 &
>
> nohup java -Dfile.encoding=utf-8 -jar pig-gateway.jar > /dev/null 2>&1 &
> 
> nohup java -Dfile.encoding=utf-8 -jar pig-auth.jar > /dev/null 2>&1 &
> 
> nohup java -Dfile.encoding=utf-8 -jar pig-upms-biz.jar > /dev/null 2>&1 &
>

pig-register 可以修改启动启动ip和端口
String NACOS_SERVER_IP = "nacos.server.ip";
String IP_ADDRESS = "nacos.inetutils.ip-address";
Sting SERVER_PORT = "server.port"
nohup java -Dfile.encoding=utf-8 -Dnacos.server.ip=x    xxx.xx.xx -jar pig-register.jar