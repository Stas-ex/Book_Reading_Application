#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -tt -i ~/.ssh/authorized_keys \
    target/black_fox_ex-0.0.1-SNAPSHOT.jar \
    ssss@192.168.0.102:/Users/ssss/

echo 'Restart server'
ssh -tt -i ~/.ssh/authorized_keys ssss@192.168.0.102 << EOF
git checkout master
pgrep java | xargs kill -9
java -jar black_fox_ex-0.0.1-SNAPSHOT.jar

nohup java -jar black_fox_ex-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'