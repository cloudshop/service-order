package com.eyun.order.web.rest.util;

import java.text.SimpleDateFormat;
import java.util.Random;

public class OrderNoUtil {
	
	private final static String ORDER_NO_PRE = "1";
	
	private final static String LEAGUER_NO_PRE = "3";
	
	private final static String DEPOSIT_NO_PRE = "2";
	
	private final static String FACEORDER_NO_PRE = "4";

		public static String getOrderNoUtil(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String format = sdf.format(System.currentTimeMillis());
	        Random random = new Random();
	        int k = (int)(random.nextDouble() * (999999 - 100000 + 1)) + 100000;
	        String r = k+"";
	        String orderNo = ORDER_NO_PRE + format + r;
			return orderNo;	
		}
		
		public static String leaGuerNoPre(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String format = sdf.format(System.currentTimeMillis());
	        Random random = new Random();
	        int k = (int)(random.nextDouble() * (999999 - 100000 + 1)) + 100000;
	        String r = k+"";
	        String orderNo = LEAGUER_NO_PRE + format + r;
			return orderNo;	
		}
		
		public static String depositNoPre(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String format = sdf.format(System.currentTimeMillis());
	        Random random = new Random();
	        int k = (int)(random.nextDouble() * (999999 - 100000 + 1)) + 100000;
	        String r = k+"";
	        String orderNo = DEPOSIT_NO_PRE + format + r;
			return orderNo;	
		}
		
		public static String faceNoPre(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String format = sdf.format(System.currentTimeMillis());
	        Random random = new Random();
	        int k = (int)(random.nextDouble() * (999999 - 100000 + 1)) + 100000;
	        String r = k+"";
	        String orderNo = FACEORDER_NO_PRE + format + r;
			return orderNo;	
		}
}
