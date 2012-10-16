export CLASSPATH=.:./LAUNCHER_JAR:LAUNCHER_DEPENDENCY

java com.fb.launcher.LauncherBootstrap 2>&1 | tee -a ../logs/launcher/cmdOut.txt

