set dir=%CD%

set p1=--server.port=8091
set p2=bumble.manager.server.port=8092
set p3=bumble.log.projName=bumble-manager-8091-8092
set p4=bumble.basic.name=M10.18.5.110:8092
set params=%p1% %p2% %p3% %p4%

java -jar %dir%\jar\bumble-manager-2.0.0-SNAPSHOT.jar %params%

pause