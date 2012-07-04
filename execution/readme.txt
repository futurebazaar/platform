INSTRUCTIONS
1) Run mvn clean install on the build machine.

2) LAUNCHER
	i)	Navigate to PLATFORM/launcher execute the following command
		mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=true
	ii)	Navigate to PLATFORM/launcher/target and copy dependency folder to PLATFORM/execution/launcher
	iii)	Navigate to PLATFORM/launcher/target and copy launcher-1.0-SNAPSHOT.jar to PLATFORM/execution/launcher
	iv)	Navigate to PLATFORM/execution/launcher edit tinlaReceivers.properties
		receiver.inventory.url=ENVIRONMENT_URL
		replace ENVIRONMENT_URL with the tinla update inventory url.

3) SAPRECEIVER
	i)	Navigate to PLATFORM/sapReceiver execute the following command
		mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=true
	ii)	Navigate to PLATFORM/sapReceiver/target and copy dependency folder to PLATFORM/execution/sapReceiver
	iii)	Navigate to PLATFORM/sapReceiver/target and copy sapReceiver-1.0-SNAPSHOT.jar to PLATFORM/execution/sapReceiver

4) Navigate to PLATFORM/execution and compress sapReceiver to sapReceiver.tar.gz and launcher to launcher.tar.gz

5) Copy PLATFORM/execution/sapReceiver.tar.gz and PLATFORM/execution/launcher.tar.gz on both the QA servers (or both the PROD server).
IMPORTANT SAME TAR FILES SHOULD BE COPIED ON THE MACHINES OF AN ENVIRONMENT.

6) Navigate to PLATFORM/execution/launcher and execute the following command:
./run.sh

7)Navigate to PLATFORM/execution/sapReceiver and execute the following command
QA environment:
./run.sh qa
PROD environment:
./run.sh prod
