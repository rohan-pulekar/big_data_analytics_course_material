from kafka import KafkaProducer
import time

producer = KafkaProducer(bootstrap_servers='localhost:9092')

for batch in range(3):
	print 'Starting batch #' + str(batch)
	for i in range(4):
		print 'sending message #' + str(i)
		producer.send('spark_topic', b'test message #' + str(i) )
	print 'Finished batch #' + str(batch)
	print 'Sleeping for 5 seconds ...'
	time.sleep(5)

print 'Done sending messages'
