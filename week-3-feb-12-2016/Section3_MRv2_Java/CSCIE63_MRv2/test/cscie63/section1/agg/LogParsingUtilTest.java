package cscie63.section1.agg;

import static org.junit.Assert.*;

import org.junit.Test;

import cscie63.section1.LogParsingUtil;

/**
 * Unit tests for NASA Log Parsing utility
 * 
 * @author marinapopova
 *
 */
public class LogParsingUtilTest {

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
