#Bumble
[![GitHub release](https://img.shields.io/badge/release-download-orange.svg)](README.md)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

#Introduction 
Bumble is a distributed platform resolving data consistency for micro-service based on 2PC mechanism.

#Getting Started
This start guide is a detailed instruction of setting up Bumble system on your local machine.
1.	Install jdk1.8+ & enviroment settings
2.	Install maven3.5.3+ & enviroment settings
3.	Install zookeeper4.3.12+, ports used: 2181,2182, 2183
4.	Install Redis64.3.0.503+ with sentinel mode, ports used: 6379, 6380, 6381, 26379, 26479, 26579
5.	Install Mysql8.0.11+, ports used: 3306
6.	Install Mysql client heidisql
7.	All the ports used by default can be referenced at the end of this markdown
8.	Download [Bumble source code]()
9.	CD to bumble-all folder and execute mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
10.	Assuming using PC as working machine, copy bumble-manager-0.0.1-SNAPSHOT.jar to E:\Java\bumble\bumble-managers\jar\
11.	Assuming using PC as working machine, copy all the bat files under bumble-manager/cli to E:\Java\bumble\bumble-managers\
12. CD to E:\Java\bumble\bumble-managers\
13.	Click and run the bat files startup-8087-8088.bat, startup-8089-8090.bat, startup-8091-8092.bat
14.	Run the mysql script bumble-demo.sql which can be found under bumble-springcloud-demo1/src/main/resource
15. Start up springboot project bumble-eureka-server which is the registry center for springcloud demo
16. Start up springboot project bumble-springcloud-demo1
17. Start up springboot project bumble-springcloud-demo2
18. Navigate to http://localhost:8082/demo/hello to trigger demo transaction
19.	Check the result in the heidisql mysql client

#Demo Ports
```js
{
	"redis": {
		"master-slaves": {
			"node1" : "6379",
			"node2" : "6380",
			"node3" : "6381"
		},
		"sentinels": {
			"node1" : "26379",
			"node2" : "26479",
			"node3" : "26579"
		}
	},
	"zookeeper": {
		"node1": "2181",
		"node2": "2182",
		"node3": "2183"
	},
	"eureka": {
		"node1": "8761",
		"node2": "8762",
		"node3": "8763"
	},
	"mysql": {
		"node1": "3306"
	},
	"springboot-demo1": {
		"node1": "8082"
	},
	"springboot-demo2": {
		"node1": "8083"
	},
	"monitor-manager": {
		"node1": "8084"
	},
	"bumble-manager": {
		"node1": "8085",
		"node2": "8086",
		"node3": "8087"
	},
	"bumble-manager-node-dev": {
		"p1": "9527"
	}
}

```



