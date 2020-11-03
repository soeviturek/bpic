package com.bpic.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串工具类
 *
 */
public class FomartTimeUtil {
/**
 * yyyyMMdd格式
 */
	public static Integer ymdUtil(Date time){
		Integer res=null;
		String format="";
		SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMdd");
		if(null != time){
			 format = format0.format(time);	
		}
		if(!"".equals(format)){
			res=Integer.valueOf(format);
		}
		return res;
	}

/**
 * yyyyMMdd格式
 */
	public static Long ymdhmsUtil(Date time){
			Long res=null;
			String format="";
			SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
			if(null != time){
				 format = format0.format(time);	
			}
			if(!"".equals(format)){
				res=Long.valueOf(format);
			}
			return res;
	}
	
	/**
	 * yyyy-MM-dd格式
	 */
	public static String ymdStrUtil(Date time){
		String format="";
		SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
		if(null != time){
			format = format0.format(time);
		}
		return format;
	}

	/**
	 * yyyy-MM-dd格式
				*/
		public static String ymStrUtil(Date time){
			String format="";
			SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM");
			if(null != time){
				format = format0.format(time);
			}
		return format;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss格式
	 */
	public static String ymdhmsStrUtil(Date time){
		String format="";
		SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null != time){
			 format = format0.format(time);	
		}
		return format;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss格式 转 yyyy-MM-dd格式
	 * @throws ParseException 
	 */
	public static String yl2ysUtil(String time) {
		String result =null;
		Date format=null;
		SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format = format0.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if(null != format){
			result = format0.format(format);
		}
		return result;
	}
	
}
