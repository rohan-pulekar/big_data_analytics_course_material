package cscie63.section1.agg;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * @author marinapopova
 *
 */
public class EclipseLogAggDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		final LogAggregatorByHour la = new LogAggregatorByHour();
		la.run(args);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		final EclipseLogAggDriver driver = new EclipseLogAggDriver();
		final int exitCode = ToolRunner.run(driver, args);
		System.exit(exitCode);
	}
}