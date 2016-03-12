package cscie63.section1.aggdates;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.ZonedDateTime;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class DateWritable implements WritableComparable<DateWritable> {

	public DateWritable() {
		super();
	}

	public DateWritable(ZonedDateTime zonedDateTime) {
		super();
		this.zonedDateTime = zonedDateTime;
	}

	private ZonedDateTime zonedDateTime;
	
	@Override
	public void write(DataOutput out) throws IOException {
		String dateStr = LogParsingUtil.dateFormatter.format(zonedDateTime);
		WritableUtils.writeString(out, dateStr);		
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		String dateStr = WritableUtils.readString(in);
		zonedDateTime = LogParsingUtil.dateFormatter.parse(dateStr, ZonedDateTime::from);
		
	}

	@Override
	public int compareTo(DateWritable o) {
		return this.zonedDateTime.compareTo(o.zonedDateTime);
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	public void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
	}

	@Override
	public String toString() {
		return LogParsingUtil.dateFormatter.format(zonedDateTime);
	}

}
