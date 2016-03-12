create table SHAKE (freq INT, word STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' stored as textfile;
LOAD DATA INPATH "/user/cloudera/hadoop-grep-shakespeare-output/part-r-00000" INTO TABLE SHAKE;
create table MERGED (freq_in_bible INT, freq_in_shake INT, word STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' stored as textfile;
INSERT OVERWRITE TABLE MERGED select k.freq AS freq_in_bible, s.freq AS freq_in_shake, COALESCE(s.word, k.word) AS wordfrom KINGJAMES k FULL OUTER JOIN SHAKE s ON (k.word = s.word);
select count(1) from MERGED where freq_in_shake IS NOT NULL AND  freq_in_bible IS NULL; 
select count(1) from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NULL; 
select word from MERGED where freq_in_shake IS NOT NULL AND freq_in_bible IS NULL LIMIT 10;
select word from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NULL LIMIT 10;
select word from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NOT NULL LIMIT 10;