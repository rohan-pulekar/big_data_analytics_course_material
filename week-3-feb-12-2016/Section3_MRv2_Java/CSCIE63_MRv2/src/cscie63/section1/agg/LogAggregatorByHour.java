package cscie63.section1.agg;

import java.io.IOException;
import java.lang.InterruptedException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import cscie63.section1.LogParsingUtil;

public class LogAggregatorByHour extends Configured implements Tool { 

	public static class LogAggMapper extends Mapper<Object, Text, Text, IntWritable> {

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			//System.out.println("key = " + key + ", value = " + value);
			String dateHour = null;
			try {
				if (value != null)
					dateHour = LogParsingUtil.getDateStrFromLine(value.toString());
			} catch (Throwable t) {
				System.out.println("ERROR parsing this line: " + value.toString() + 
						"\n ERROR: " + t.getMessage());
			}
			if (dateHour == null) {
				context.write(new Text("UNKNOWN"), new IntWritable(1));
				System.out.println("ERROR parsing DATE for this line: " + value.toString() + 
						"\n , storing as an UNKNOWN date");
			}
			else
				context.write(new Text(dateHour), new IntWritable(1));
		}
	} 

	public static class LogAggReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
			throws IOException, InterruptedException {

			int count = 0;
			for (IntWritable val : values) {
				count += val.get();
			}
			context.write(key, new IntWritable(count));
		}
	} 

	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);

		Job job = Job.getInstance(conf, "LogAggregatorByHour");
		job.setJarByClass(LogAggregatorByHour.class);

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setMapperClass(LogAggMapper.class);
		job.setReducerClass(LogAggReducer.class);
		job.setMapOutputKeyClass(Text.class); 
		job.setMapOutputValueClass(IntWritable.class); 
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		boolean success = job.waitForCompletion(true);
		if (success)
			return 0;
		else 
			return 1;
	} 

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new LogAggregatorByHour(),
				args);
		System.exit(res);
	} 

}
