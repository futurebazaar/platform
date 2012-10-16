export CLASSPATH=.:./SAP_RECEIVER_JAR:MOCK_SAP_DEPENDENCY

java -Djava.library.path=. com.fb.platform.sap.launcher.MockInventorySapServer

