cd /d F:\git\bumble-gpl\BUMBLE\bumble\bumble-manager-modules
call mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

copy F:\git\bumble-gpl\BUMBLE\bumble\bumble-manager\target\bumble-manager-2.0.0-SNAPSHOT.jar E:\Java\bumble\bumble-managers\jar
pause