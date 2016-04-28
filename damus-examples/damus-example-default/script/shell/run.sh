#!/bin/bash
source /etc/profile

mkdir -p /var/log/damus

currentdir=`dirname $0`

case $1 in
start)
    nohup ${JAVA_HOME}/bin/java -XX:PermSize=128m -XX:MaxPermSize=256m -classpath /usr/local/spark-1.4.1-bin-hadoop2.6/lib/spark-assembly-1.4.1-hadoop2.6.0.jar:$currentdir/damus-offline.jar com.ximalaya.damus.offline.Main >/dev/null 2>/var/log/damus/damus-offline.log.wf &
    ;;
stop)
    kill $(ps -ef|grep "damus-offline.jar" | awk '$0 !~/grep/ {print $2}' |tr -s '\n' ' ')
    ;;
*)
    echo "Usage: $0 {start|stop}"
esac
