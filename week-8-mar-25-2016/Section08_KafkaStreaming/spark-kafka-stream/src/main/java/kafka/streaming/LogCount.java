package kafka.streaming;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class LogCount {
	public static void main(String[] args) throws Exception {
		String inputFile = args[0];
		String outputFile = args[1];
		
		Pattern pattern = Pattern.compile("([^ ]*) ([^ ]*) ([^ ]*) (-|\\[[^\\]]*\\]) ([^ \"]*|\"[^\"]*\") (-|[0-9]*) (-|[0-9]*)(?: ([^ \"]*|\"[^\"]*\") ([^ \"]*|\"[^\"]*\"))?");

		// Create a Java Spark Context.
	    JavaSparkContext sc = new JavaSparkContext("local", "LogCountApp");

		// Load our input data.
		JavaRDD<String> input = sc.textFile(inputFile);
		
		// get client IPs out of the lines
		FlatMapFunction<String, String> parseLogFunction = new FlatMapFunction<String, String>() {
			public Iterable<String> call(String line) {
				Matcher matcher = pattern.matcher(line);
				String clientIp = null;
				List<String> clientIps = new LinkedList<>();
				if (matcher.find()) {
				    clientIp = matcher.group(1);
				    clientIps.add(clientIp);
				}
				return clientIps;
			}
		};
				
		JavaRDD<String> words = input.flatMap(parseLogFunction);
		
		// Transform into IP and count.
		JavaPairRDD<String, Integer> counts = words.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String x) {
				return new Tuple2(x, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer x, Integer y) {
				return x + y;
			}
		});
		
		// Save the IP count back out to a text file, causing evaluation.
		counts.saveAsTextFile(outputFile);
	}

}
