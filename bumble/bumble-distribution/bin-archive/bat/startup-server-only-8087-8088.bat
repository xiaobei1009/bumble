set dir=%CD%
java -jar %dir%\jar\bumble-manager-0.0.1-SNAPSHOT.jar --server.port=8087 bumble.manager.server.port=8088 bumble.log.projName=bumble-manager-8087-8088 server.only=true
