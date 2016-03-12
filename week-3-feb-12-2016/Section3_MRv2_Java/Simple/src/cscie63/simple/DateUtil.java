package cscie63.simple;

import java.util.Date;

public class DateUtil {

	private String myText = "my test";
	
	private void printCurrentDate(){
		Date myDate = new Date();
		System.out.println("Now is " + myDate + "; and myText = " + myText);
	}
	
	public static void main(String[] args) {
		DateUtil myUtil = new DateUtil();
		myUtil.printCurrentDate();
	}

}
