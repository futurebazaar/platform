export CLASSPATH=.:./SAP_LOAD_RECEIVER_JAR:MOCK_SAP_LOAD_DEPENDENCY

java -Djava.library.path=. com.fb.platform.sap.launcher.MockSapLoadTester

