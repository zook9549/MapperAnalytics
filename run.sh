#!/bin/sh

export TZ='America/New_York'

cd /apps/refer

pkill -f MapperApplication
unzip -oq mapper-0.0.1-SNAPSHOT.jar
java -Dspring.profiles.active=prod -cp ./:./lib/*:./:./BOOT-INF/classes:./BOOT-INF/lib/* team.refer.mapper.MapperApplication
