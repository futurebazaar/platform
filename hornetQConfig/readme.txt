INSTRUCTIONS

1)Copy the platform folder to the following location
HORNET_Q_INSTALLATION/config/stand-alone

2)Edit hornetq-configuration.xml 

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

3)To start the HornetQ navigate to the following location
HORNET_Q_INSTALLATION/bin
and run the following command:
./run.sh ../config/stand-alone/platform

