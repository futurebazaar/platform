#remove tar.gz file
cd execution
rm -r launcher.tar.gz
rm -r sapReceiver.tar.gz

#remove dependencies from execution/launcher
cd launcher
rm -r dependency/
rm launcher*.jar
rm launcherRun.sh

#remove dependencies from execution/sapReceiver
cd ../sapReceiver
rm -r dependency/
rm sapReceiver*.jar
rm sapReceiverRun.sh
rm mockSapReceiverRun.sh

#return to the platform folder
cd ../..

#create the cp.txt for the launcher project
cd launcher
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt

#replace the M2_REPO path with .dependency
sed  "s|$M2_REPO|./dependency|g" cp.txt >target/cp.txt

#copy the dependency folder and the launcher jar to execution/launcher
cd target
cp -r dependency/ ../../execution/launcher/
cp launcher*.jar ../../execution/launcher/

#store dependency classpath in a variable
dependency=`cat cp.txt`
echo $dependency

#get jar filename
jarFile=`ls launcher*.jar`
echo "Jar file name : " $jarFile

#return to the platform folder
cd ../..

#create launcher executable file
cd execution/launcher
sed  "s|LAUNCHER_DEPENDENCY|$dependency|g" launcherTemplate.sh >launcherRun.sh
sed -i "s|LAUNCHER_JAR|$jarFile|g" launcherRun.sh

chmod 777 launcherRun.sh

#return to the platform folder
cd ../..

#create the cp.txt for the sapReceiver project
cd sapReceiver
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt

#replace the M2_REPO path with .dependency
sed  "s|$M2_REPO|./dependency|g" cp.txt >target/cp.txt

#copy the dependency folder and the launcher jar to execution/sapReceiver
cd target
cp -r dependency/ ../../execution/sapReceiver/
cp sapReceiver*.jar ../../execution/sapReceiver/

#store dependency classpath in a variable
dependency=`cat cp.txt`
echo $dependency

#get jar filename
jarFile=`ls sapReceiver*.jar`
echo "Jar file name : " $jarFile

#return to the platform folder
cd ../..

#create sap receiver and mock sap receiver executable file
cd execution/sapReceiver
sed  "s|SAP_DEPENDENCY|$dependency|g" sapReceiverTemplate.sh >sapReceiverRun.sh
sed  "s|MOCK_SAP_DEPENDENCY|$dependency|g" mockSapReceiverTemplate.sh >mockSapReceiverRun.sh
sed -i "s|SAP_RECEIVER_JAR|$jarFile|g" sapReceiverRun.sh
sed -i "s|SAP_RECEIVER_JAR|$jarFile|g" mockSapReceiverRun.sh

chmod 777 sapReceiverRun.sh
chmod 777 mockSapReceiverRun.sh

#return to execution folder
cd ..

#create launcher.tar and sapReceiver.tar
tar -cvf launcher.tar launcher
tar -cvf sapReceiver.tar sapReceiver

#create gzip files from the tar
gzip -9 launcher.tar
gzip -9 sapReceiver.tar

