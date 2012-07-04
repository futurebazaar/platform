INSTRUCTIONS

1)Copy the platform folder to the following location
HORNET_Q_INSTALLATION/config/stand-alone

2)Edit hornetq-configuration.xml 

Original:
   <connectors>      
      <connector name="netty">
         <factory-class>org.hornetq.core.remoting.impl.netty.NettyConnectorFactory</factory-class>
         <param key="host"  value="${hornetq.remoting.netty.host:MACHINE_IP_ADDRESS}"/>
         <param key="port"  value="${hornetq.remoting.netty.port:5445}"/>
      </connector>
      
      <connector name="netty-throughput">
         <factory-class>org.hornetq.core.remoting.impl.netty.NettyConnectorFactory</factory-class>
         <param key="host"  value="${hornetq.remoting.netty.host:localhost}"/>
         <param key="port"  value="${hornetq.remoting.netty.batch.port:5455}"/>
         <param key="batch-delay" value="50"/>
      </connector>
   </connectors>

   <acceptors>
      <acceptor name="netty">
         <factory-class>org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory</factory-class>
         <param key="host"  value="${hornetq.remoting.netty.host:MACHINE_IP_ADDRESS}"/>
         <param key="port"  value="${hornetq.remoting.netty.port:5445}"/>
      </acceptor>
      
      <acceptor name="netty-throughput">
         <factory-class>org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory</factory-class>
         <param key="host"  value="${hornetq.remoting.netty.host:localhost}"/>
         <param key="port"  value="${hornetq.remoting.netty.batch.port:5455}"/>
         <param key="batch-delay" value="50"/>
         <param key="direct-deliver" value="false"/>
      </acceptor>
   </acceptors>

Replace MACHINE_IP_ADDRESS with the ipaddress of the machine.

3)Edit hornetq-configuration.xml 

Original:
   <broadcast-groups>
      <broadcast-group name="bg-group1">
         <group-address>229.141.145.81</group-address>
         <group-port>PORT</group-port>
         <broadcast-period>5000</broadcast-period>
         <connector-ref>netty</connector-ref>
      </broadcast-group>
   </broadcast-groups>

   <discovery-groups>
      <discovery-group name="dg-group1">
         <group-address>229.141.145.81</group-address>
         <group-port>PORT</group-port>
         <refresh-timeout>10000</refresh-timeout>
      </discovery-group>
   </discovery-groups>

Replace PORT with 8856 for QA and 8865 for PROD

4)To start the HornetQ navigate to the following location
HORNET_Q_INSTALLATION/bin
and run the following command:
./run.sh ../config/stand-alone/platform

