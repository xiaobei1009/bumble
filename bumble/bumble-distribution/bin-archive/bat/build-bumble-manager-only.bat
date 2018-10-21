cd /d F:\git\bumble-gpl\BUMBLE\bumble\bumble-manager
call mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

copy F:\git\bumble-gpl\BUMBLE\bumble\bumble-manager\target\bumble-manager-0.0.1-SNAPSHOT.jar E:\Java\bumble\bumble-managers\jar
pause