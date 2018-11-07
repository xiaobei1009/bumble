探针技术

需要将javassist-3.19.0-GA.jar放到bumble-agent.jar的同级目录下
需要将目标jar包例如bumble-test.jar放到bumble-agent.jar的同级目录下

测试探针启动目标jar包
java -javaagent:bumble-agent.jar=XXX -jar bumble-test.jar