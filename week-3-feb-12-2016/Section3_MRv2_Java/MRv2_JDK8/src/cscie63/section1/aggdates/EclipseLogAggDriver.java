package cscie63.section1.aggdates;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * By extending hadoop's test classes, I am able to invoke the mapreduce jobs
 * outside of Cloudera
 */
public class EclipseLogAggDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		final LogAggregatorByDateHour la = new LogAggregatorByDateHour();
		la.run(args);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		final EclipseLogAggDriver driver = new EclipseLogAggDriver();
		final int exitCode = ToolRunner.run(driver, args);
		System.exit(exitCode);
	}
}