# 该镜像需要依赖的基础镜像
FROM openjdk:8-jdk
# 将targer目录下的jar包复制到docker容器/home/springboot目录下面目录下面
ADD ./target/CollaborAIte-0.0.1-SNAPSHOT.jar /home/springboot/CollaborAIte.jar
# 声明服务运行在8081端口
EXPOSE 8085
# 执行命令
CMD ["java","-jar","/home/springboot/CollaborAIte.jar"]
