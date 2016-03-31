/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.streaming;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scala.Tuple2;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 * Usage: JavaDirectKafkaWordCount <brokers> <topics>
 *   <brokers> is a list of one or more Kafka brokers
 *   <topics> is a list of one or more kafka topics to consume from
 *
 * Example:
 *    $ bin/run-example streaming.JavaDirectKafkaWordCount broker1-host:port,broker2-host:port topic1,topic2
 */

public final class KafkaWindowLogCount {
  private static final Pattern APACHE_LOG_PATTERN = Pattern.compile("([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?");

  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: KafkaWindowLogCount <brokers> <topics> [<batch_interval> <window_duration> <sliding_window_duration>]\n" +
          "  <brokers> is a list of one or more Kafka brokers\n" +
          "  <topics> is a list of one or more kafka topics to consume from\n\n");
      System.exit(1);
    }

    String brokers = args[0];
    String topics = args[1];
    int batchIntervalInSeconds = 5;
    int windowDurationInSeconds = 20;
    int slidingWindowDurationInSeconds = 10;
    if (args.length == 5) {
    	batchIntervalInSeconds = Integer.parseInt(args[2]);
    	windowDurationInSeconds = Integer.parseInt(args[3]);
    	slidingWindowDurationInSeconds = Integer.parseInt(args[4]);
    }
    // Create context with a 10 seconds batch interval
    JavaSparkContext sparkConf = new JavaSparkContext("local[5]", "KafkaLogCount");
    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(batchIntervalInSeconds));

    HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
    HashMap<String, String> kafkaParams = new HashMap<String, String>();
    kafkaParams.put("metadata.broker.list", brokers);
    kafkaParams.put("zookeeper.connect", "localhost:2181");
    kafkaParams.put("group.id", "spark-app");
    System.out.println("Kafka parameters: " + kafkaParams);
    System.out.println("KafkaWindowLogCount parameters: topics=" + topics + 
    	"; batchIntervalInSeconds=" + batchIntervalInSeconds + 
    	"; windowDurationInSeconds=" + windowDurationInSeconds +
    	"; slidingWindowDurationInSeconds=" + slidingWindowDurationInSeconds);

    // Create direct kafka stream with brokers and topics
    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
        jssc,
        String.class,
        String.class,
        StringDecoder.class,
        StringDecoder.class,
        kafkaParams,
        topicsSet
    );

    // Get the lines, split them into words
    JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
      @Override
      public String call(Tuple2<String, String> tuple2) {
        return tuple2._2();
      }
    });
    //lines.print();

	// get client IPs out of the lines
	FlatMapFunction<String, String> parseLogFunction = new FlatMapFunction<String, String>() {
		public Iterable<String> call(String line) {
			Matcher matcher = APACHE_LOG_PATTERN.matcher(line);
			String clientIp = null;
			List<String> clientIps = new LinkedList<>();
			if (matcher.find()) {
			    clientIp = matcher.group(1);
			    clientIps.add(clientIp);
			}
			return clientIps;
		}
	};
    
    JavaDStream<String> words = lines.flatMap(parseLogFunction);
    //words.print();
    
 // Count each IP in each batch
    JavaPairDStream<String, Integer> pairs = words.mapToPair(
      new PairFunction<String, String, Integer>() {
        @Override public Tuple2<String, Integer> call(String s) {
          return new Tuple2<String, Integer>(s, 1);
        }
      });
    
    // Reduce function adding two integers, defined separately for clarity
    Function2<Integer, Integer, Integer> reduceFunc = new Function2<Integer, Integer, Integer>() {
      @Override public Integer call(Integer i1, Integer i2) {
        return i1 + i2;
      }
    };

    // Reduce last windowDurationInSeconds seconds of data, every slidingWindowDurationInSeconds seconds
    JavaPairDStream<String, Integer> windowedWordCounts = pairs.reduceByKeyAndWindow(
    		reduceFunc, Durations.seconds(windowDurationInSeconds), Durations.seconds(slidingWindowDurationInSeconds));
      
    windowedWordCounts.print();

    // Start the computation
    jssc.start();
    jssc.awaitTermination();
  }
}