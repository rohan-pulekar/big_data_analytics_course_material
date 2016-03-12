CREATE TABLE apachelog ( host STRING, identity STRING, user STRING, time STRING, request STRING, status STRING, size STRING, referer STRING, agent STRING) ROW FORMAT SERDE 'org.apache.hadoop.hive.contrib.serde2.RegexSerDe' WITH SERDEPROPERTIES ( "input.regex" = "([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?", "output.format.string" = "%1$s %2$s %3$s %4$s %5$s %6$s %7$s %8$s %9$s" ) STORED AS TEXTFILE;
select * from apachelog;
LOAD DATA LOCAL INPATH "/mnt/hgfs/shared_dir_with_cloudera_vm/examples_older/apache.access.2.log" INTO TABLE apachelog;
select * from apachelog;
LOAD DATA LOCAL INPATH "/mnt/hgfs/shared_dir_with_cloudera_vm/examples_older/apache.access.log" INTO TABLE apachelog;
select * from apachelog;
LOAD DATA LOCAL INPATH "/mnt/hgfs/shared_dir_with_cloudera_vm/access_log_1.txt" INTO TABLE apachelog;
select count(1) from apachelog;