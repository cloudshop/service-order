package com.eyun.order.web.rest.util;

import java.text.SimpleDateFormat;
import java.util.Random;

public class OrderNoUtil {
	
	private final static String ORDER_NO_PRE = "1";

		public static String getOrderNoUtil(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String format = sdf.format(System.currentTimeMillis());
//	        String random = StringUtil.random(6);
	        Random random = new Random();
	        int k = (int)(random.nextDouble() * (99999 - 10000 + 1)) + 10000;
	        String r = k+"";
	        String orderNo = ORDER_NO_PRE + format + r;
			return orderNo;	
		}
}
