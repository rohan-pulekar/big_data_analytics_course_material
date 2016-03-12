package cscie63.section1.aggdates;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;

import org.junit.Test;

public class LogParsingUtilTest {

	@Test
	public void testGetDateFromLine() {
		String testLine = "199.72.81.55 - - [01/Jul/1995:03:20:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245";
		ZonedDateTime resultDate = LogParsingUtil.getDateFromLine(testLine);
		int resultDay = resultDate.getDayOfMonth();
		int expectedDay = 1;
		assertEquals(expectedDay, resultDay);
		
		String expectedFormattedDateStr = "01/Jul/1995:03:00:00 -0400";
		String resultFormattedDateStr = LogParsingUtil.dateFormatter.format(resultDate);
		assertEquals(expectedFormattedDateStr, resultFormattedDateStr);
		System.out.println("Result date is: " + resultFormattedDateStr);
	}

	@Test
	public void testGetDateStrFromLine() {
		String testLine = "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245";
		String expectedDateHour = "01/Jul/1995:00";
		String resultDateHour = LogParsingUtil.getDateStrFromLine(testLine);
		assertEquals(expectedDateHour, resultDateHour);
	}

	@Test
	public void testGetRequestURLFromLine() {
		String testLine = "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245";
		String expectedRequestUrl = "/history/apollo/";
		String resultRequestUrl = LogParsingUtil.getRequestURLFromLine(testLine);
		assertEquals(expectedRequestUrl, resultRequestUrl);
	}

}
