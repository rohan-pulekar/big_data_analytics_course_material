create table KINGJAMES (freq INT, word STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' stored as textfile;
LOAD DATA INPATH "/user/cloudera/hadoop-grep-bible-output/part-r-00000" INTO TABLE KINGJAMES;
select * from KINGJAMES where lower(word) like 'w%' and length(word)>=4 and freq>250 order by freq;
select count(1) from KINGJAMES where lower(word) like 'w%' and length(word)>=4 and freq>250;