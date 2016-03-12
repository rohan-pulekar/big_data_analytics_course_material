package cscie63.section1.aggdates;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LogParsingUtil {

	public static DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
	public static ZonedDateTime now = ZonedDateTime.now();
	
	public static ZonedDateTime getDateFromLine (String logLine) {
		String dateStr = getFullDateStrFromLine(logLine);
		ZonedDateTime zonedDateTime = dateFormatter.parse(dateStr, ZonedDateTime::from);
		// get rid of minutes and seconds - we only care about which hour the log is for
		ZonedDateTime hourDate = zonedDateTime.withMinute(0).withSecond(0);
		return hourDate;
	}
	
	public static String getFullDateStrFromLine (String logLine) {
		// parse the line here and get the date up to an hour; example line format:
		// 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
		if (logLine == null)
			return null;
		String[] baseParts = logLine.split("\\[");
		if (baseParts.length < 2)
			return null;
		String[] dateParts = baseParts[1].split("\\]");
		if (dateParts.length < 2)
			return null;
		String dateStr = dateParts[0];
		return dateStr;
	}

	public static String getDateStrFromLine (String logLine) {
		// parse the line here and get the date up to an hour; example line format:
		// 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
		if (logLine == null)
			return null;
		String[] baseParts = logLine.split("\\[");
		if (baseParts.length < 2)
			return null;
		String[] dateParts = baseParts[1].split(":", 3);
		if (dateParts.length < 3)
			return null;
		String dateHour = dateParts[0] + ":" + dateParts[1];
		return dateHour;
	}

	public static String getRequestURLFromLine (String logLine) {
		// parse the line here and get the date up to an hour; example line format:
		// 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
		if (logLine == null)
			return null;
		String[] baseParts = logLine.split("GET ");
		if (baseParts.length < 2)
			return null;
		String[] requestParts = baseParts[1].split(" ", 2);
		if (requestParts.length < 2)
			return null;
		String requestUrl = requestParts[0];
		return requestUrl;
	}

}
