from kafka import KafkaProducer
import time

producer = KafkaProducer(bootstrap_servers='localhost:9092')

fname = 'apache_logs_small.txt'
f = open(fname)
## Read the first line 
line = f.readline()

## If the file is not empty keep reading line one at a time
## till the file is empty
## send 'batchSize' number of lines to Kafka - then sleep for a few seconds
batchSize = 2
lineBatchNum = 0
lineNum = 0
while line:
	if lineBatchNum < batchSize:
		lineNum+=1
		lineBatchNum+=1
		print 'sending message #' + str(lineNum) + ': ' + line
		producer.send('spark_topic', line )
		line = f.readline()
	else:
		lineBatchNum = 0
		print 'Sleeping for 2 seconds ...'
		time.sleep(2)

f.close()	
print 'Done sending messages'
