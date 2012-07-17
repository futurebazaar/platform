export CLASSPATH=.:./sapReceiver-1.0-SNAPSHOT.jar:MOCK_SAP_DEPENDENCY

java -Djava.library.path=. com.fb.platform.sap.launcher.MockInventorySapServer

