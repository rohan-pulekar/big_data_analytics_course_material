select count(1) from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NOT NULL;
select count(1) from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NULL; 
select count(1) from MERGED where freq_in_bible IS NULL AND freq_in_shake IS NOT NULL; 

select count(1) from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NOT NULL;
select count(1) from MERGED where freq_in_bible IS NOT NULL AND freq_in_shake IS NULL;
select count(1) from MERGED where freq_in_bible IS NULL AND freq_in_shake IS NOT NULL; 