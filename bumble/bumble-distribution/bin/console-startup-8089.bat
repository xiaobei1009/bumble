set dir=%CD%
set p1=bumble.manager.server.port=8089
set p2=bumble.log.projName=bumble-manager-8089
set p3=bumble.basic.name=M10.18.5.110:8089
set params=%p1% %p2% %p3%%

call %dir%\console-startup.bat %params%

pause