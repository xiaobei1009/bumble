# bumble config api 

It is the interface for bumble manager to fetch its configurations, 
such as the expired time for data in Redis which is used for storing the
transaction group JSON object.

The configurations will be stored wherever you like, currently the default
implementation is storing the data in Zookeeper, and you can make your own
favorite choice. The implementation binding is taken effect automatically, 
all you need is to implement the interface and put the jar on the classpath.
The bumble-config-zookeeper jar can be taken as an good example for reference.

