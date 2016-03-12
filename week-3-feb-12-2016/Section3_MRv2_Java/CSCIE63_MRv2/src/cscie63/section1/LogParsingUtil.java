package cscie63.section1;

import java.util.Date;

public class LogParsingUtil {

	public static Date getDateFromLine (String logLine) {
		// TODO parse the line here and get the date
		return null;
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
