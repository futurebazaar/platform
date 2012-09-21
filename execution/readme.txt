INSTRUCTIONS
1) Run mvn clean install on the build machine.

2) LAUNCHER
	i)	Navigate to PLATFORM/launcher/target and copy dependency folder to PLATFORM/execution/launcher
	ii)	Navigate to PLATFORM/launcher/target and copy launcher-1.0-SNAPSHOT.jar to PLATFORM/execution/launcher

3) SAPRECEIVER
	i)	Navigate to PLATFORM/sapReceiver/target and copy dependency folder to PLATFORM/execution/sapReceiver
	ii)	Navigate to PLATFORM/sapReceiver/target and copy sapReceiver-1.0-SNAPSHOT.jar to PLATFORM/execution/sapReceiver

4) Navigate to PLATFORM/execution and compress sapReceiver to sapReceiver.tar.gz and launcher to launcher.tar.gz

5) Copy PLATFORM/execution/sapReceiver.tar.gz and PLATFORM/execution/launcher.tar.gz on both the QA servers (or both the PROD server).
IMPORTANT SAME TAR FILES SHOULD BE COPIED ON THE MACHINES OF AN ENVIRONMENT.

6) Untar the files on the target machine. 
	i)	Navigate to launcher and edit tinlaReceivers.properties
		receiver.inventory.url=ENVIRONMENT_URL
		replace ENVIRONMENT_URL with the tinla update inventory url.
	ii)	Navigate to sapReceiver and edit platformJndi.properties to point to the hornetq jndi server address.
	iii) 	Navigate to launcher and edit platformJndi.properties to point to the hornetq jndi server address.

7) Navigate to PLATFORM/execution/launcher and execute the following command:
./run.sh

8)Navigate to PLATFORM/execution/sapReceiver and execute the following command
QA environment:
./run.sh qa
PROD environment:
./run.sh prod

