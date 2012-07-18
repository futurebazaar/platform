export CLASSPATH=.:./sapReceiver-1.0-SNAPSHOT.jar:SAP_DEPENDENCY

java -Djava.library.path=. com.fb.platform.sap.launcher.SapServerLauncher $1

