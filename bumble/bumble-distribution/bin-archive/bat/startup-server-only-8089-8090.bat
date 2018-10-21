set dir=%CD%
java -jar %dir%\jar\bumble-manager-0.0.1-SNAPSHOT.jar --server.port=8089 bumble.manager.server.port=8090 bumble.log.projName=bumble-manager-8089-8090 server.only=true
