export CLASSPATH=.:./SAP_RECEIVER_JAR:SAP_DEPENDENCY

java -Djava.library.path=. com.fb.platform.sap.launcher.SapServerLauncher $1 2> ../logs/sapReceiver/cmdOut.txt

