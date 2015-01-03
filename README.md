# Web Data Analytics

## Quick run

Create standalone jar

     sbt assembly

specify hadoop and scalding command in ./script/common-env.drake. Default setting is below:

     HDFS_EXEC:=hdfs
     HADOOP_EXEC:=hadoop
     SCALDRB_EXEC:=scald.rb

Initiallize local environment, download sample log and execute the log etl job on scalding local mode

     drake -w ./script/webDataAnalytics.drake %init %splunk %AccessLogEtlJob_local

## Dependencies

Hadoop CDH 4.5.0 MR1
Scalding compiled with CDH 4.5.0 MR1
Drake

if you switch to different hadoop distribution, update the hadoop version in ./project/Dependencies.scala 

