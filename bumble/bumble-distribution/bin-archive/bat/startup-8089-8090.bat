set dir=%CD%

set p1=--server.port=8089
set p2=bumble.manager.server.port=8090
set p3=bumble.log.projName=bumble-manager-8089-8090
set p4=bumble.basic.name=M10.18.5.110:8090
set params=%p1% %p2% %p3% %p4%

java -jar %dir%\jar\bumble-manager-0.0.1-SNAPSHOT.jar %params%

pause