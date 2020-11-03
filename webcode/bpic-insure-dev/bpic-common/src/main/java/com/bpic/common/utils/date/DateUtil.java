package com.bpic.common.utils.date;

import com.bpic.common.utils.StringUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class DateUtil {

	private static final ConcurrentMap<String, SimpleDateFormat> dateFormatCache = new ConcurrentHashMap<String, SimpleDateFormat>();

	private static final String dateInputFmt = "yyyyMMdd";
	private static final String dateOutputFmt = "yyyy-MM-dd";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static SimpleDateFormat getSimpleDateFormat(String format) {
		dateFormatCache.putIfAbsent(format, new SimpleDateFormat(format));
		return (SimpleDateFormat) dateFormatCache.get(format);
	}

	public static String format(String format) {
		return getSimpleDateFormat(format).format(now());
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		return getSimpleDateFormat(format).format(date);
	}

	public static String getYMDHMSS() {
		return format(new Date(), "yyyyMMddHHmmssSSS");
	}

	public static String toSeconds(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String toMinute(Date date) {
		return format(date, "yyyy-MM-dd HH:mm");
	}

	public static String toMonthAndDay(Date date) {
		return format(date, "MM月dd日");
	}

	public static String toYearMonthAndDay(Date date) {
		return format(date, "yyyy年MM月dd日");
	}
	public static String toDaySeconds(Date date) {
		return format(date, "MM/dd HH:mm");
	}

	public static String toYearMonthDay(Date date) {
		return format(date, "yyyy/MM/dd HH:mm");
	}

	public static String toDay(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String toYearMonth(Date date) {
		return format(date, "yyyy.MM");
	}

	public static String toMonthDate(Date date) {
		return format(date, "MM月dd日 HH:mm");
	}

	public static String toMonthDateNoHour(Date date) {
		String dateStr = format(date, "MM月dd日");
		if (dateStr.startsWith("0")) {
			dateStr = dateStr.substring(1);
		}
		return dateStr;
	}

	public static String toShortDay(Date date) {
		return format(date, "yyyyMMdd");
	}

	public static String toMonth(Date date) {
		return format(date, "MM");
	}

	public static String toShortdd(Date date) {
		return format(date, "dd");
	}

	public static String toShortSeconds(Date date) {
		return format(date, "HH:mm:ss");
	}

	public static String toShortYear(Date date) {
		return format(date, "yyyy");
	}

	public static Date valueof(String s, String format) {
		return parse(s, format);
	}

	public static Date valueOfStandard(String s) throws Exception {
		return parse(s, "yyyy-MM-dd");
	}

	public static Date valueOfShort(String s) throws ParseException {
		return parse(s, "yyyyMMdd");
	}

	public static boolean isSameDay(Date d1, Date d2) {
		return roundToDay(d1).getTime() == roundToDay(d2).getTime();
	}

	public static boolean compareDay(Date d1, Date d2) {
		return d1.compareTo(d2) >= 0;
	}

	public static Date parse(String s, String format) {
		if (s == null)
			return null;
		try {
			return getSimpleDateFormat(format).parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date parse(String s) {
		if (s == null) {
			return null;
		}
		return parse(s, "yyyy-MM-dd HH:mm");
	}

	public static int workDayInterval(Date big, Date small) {
		big = roundToDay(big);
		small = roundToDay(small);

		GregorianCalendar smallGc = new GregorianCalendar();
		smallGc.setTime(small);

		GregorianCalendar bigGc = new GregorianCalendar();
		bigGc.setTime(big);

		int workDays = 0;
		long bigTime = bigGc.getTime().getTime();
		while (smallGc.getTime().getTime() < bigTime) {
			int week = smallGc.get(7);

			smallGc.add(5, 1);
			if ((week == 7) || (week == 1)) {
				continue;
			}
			workDays++;
		}

		return workDays;
	}

	public static boolean isWorkDay(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int week = gc.get(7);

		return (week == 7) || (week == 1);
	}

	public static Date roundToDay(Date date) {
		date = roundToHour(date);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(11, 0);
		return gc.getTime();
	}

	public static Date roundToHour(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(12, 0);
		gc.set(13, 0);
		gc.set(14, 0);
		return gc.getTime();
	}

	public static Date roundToMinute(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(13, 0);
		gc.set(14, 0);
		return gc.getTime();
	}

	public static Date nextDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, 1);
		return roundToDay(gc.getTime());
	}

	public static Date nextThreeDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, 3);
		return roundToDay(gc.getTime());
	}

	public static Date nextHour(Date date) {
		date = add(date, 10, 1);
		return roundToHour(date);
	}

	public static Date add(Date date, int field, int amount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(field, amount);
		return gc.getTime();
	}

	public static long interval(Date big, Date small, int field) {
		long time = big.getTime() - small.getTime();

		if (field == 14) {
			return time;
		}
		time /= 1000L;

		if (field == 13) {
			return time;
		}
		time /= 60L;

		if (field == 12) {
			return time;
		}
		time /= 60L;

		if (field == 10) {
			return time;
		}
		time /= 24L;

		if (field == 5) {
			return time;
		}
		time /= 30L;

		if (field == 2) {
			return time;
		}
		time /= 365L;

		if (field == 1) {
			return time;
		}
		return time;
	}

	// public static void main(String[] args) {
	// Date[] dates = getDatesByMonth(parse("2012-04", "yyyy-MM"), 4);
	// for (Date date : dates)
	// }

	public static Date addDay(Date date, int amount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, amount);
		return roundToDay(gc.getTime());
	}

	public static Date lastDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, -1);
		return roundToDay(gc.getTime());
	}

	public static Date newlastDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, -1);
		return roundToHour(gc.getTime());
	}

	public static Date lastMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(2, -1);
		return roundToDay(gc.getTime());
	}

	public static Date nextMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(2, 1);
		return roundToDay(gc.getTime());
	}

	public static Date roundToTenMinute(Date date) {
		Date minuteTime = roundToMinute(date);
		int minuteNum = getTimeField(minuteTime, 12);
		minuteNum = minuteNum / 10 * 10;
		minuteTime = setTimeField(minuteTime, 12, minuteNum);
		return minuteTime;
	}

	public static Date getFirstDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(5, 1);
		return roundToDay(gc.getTime());
	}

	public static Date getFirstDayOfWeek(Date date) {
		while (getTimeField(date, 7) != 2) {
			date = lastDate(date);
		}
		return date;
	}

	public static Date getLastDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		gc.add(2, 1);
		gc.set(5, 0);
		return roundToDay(gc.getTime());
	}

	public static Date getLastDayOfWeek(Date date) {
		while (getTimeField(date, 7) != 1) {
			date = nextDate(date);
		}
		return date;
	}

	public static String oracleToDate(Date date) {
		return "to_date('" + format(date, "yyyy-MM-dd") + "', 'yyyy-mm-dd')";
	}

	public static int getTimeField(Date date, int field) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(field);
	}

	public static Date setTimeField(Date date, int field, int timeNum) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(field, timeNum);
		return gc.getTime();
	}

	public static String ampm(Date date) {
		int hours = getTimeField(date, 11);

		if (hours <= 12) {
			return "A";
		}
		return "P";
	}

	public static String ampm(Date startTime, Date endTime) {
		String start = ampm(startTime);
		String end = ampm(endTime);

		if ((start == "A") && (end == "A"))
			return "A";
		if ((start == "P") && (end == "P")) {
			return "P";
		}
		return "N";
	}

	public static Date[] getTimeInterval(Date date, String ampm) {
		Date startDate = (Date) date.clone();
		Date endDate = (Date) date.clone();

		if (ampm.equals("A")) {
			startDate = setTimeField(startDate, 11, 9);
			endDate = setTimeField(endDate, 11, 12);
		} else if (ampm.equals("P")) {
			startDate = setTimeField(startDate, 11, 12);
			endDate = setTimeField(endDate, 11, 18);
		} else if (ampm.equals("N")) {
			startDate = setTimeField(startDate, 11, 9);
			endDate = setTimeField(endDate, 11, 18);
		}

		startDate = setTimeField(startDate, 12, 0);
		endDate = setTimeField(endDate, 12, 0);
		startDate = setTimeField(startDate, 13, 0);
		endDate = setTimeField(endDate, 13, 0);

		Date[] dates = new Date[2];
		dates[0] = startDate;
		dates[1] = endDate;

		return dates;
	}

	public static String getChineseWeekName(Date date) {
		int w = getTimeField(date, 7);
		String cw = "";
		switch (w) {
			case 1:
				cw = "星期日";
				break;
			case 2:
				cw = "星期一";
				break;
			case 3:
				cw = "星期二";
				break;
			case 4:
				cw = "星期三";
				break;
			case 5:
				cw = "星期四";
				break;
			case 6:
				cw = "星期五";
				break;
			case 7:
				cw = "星期六";
				break;
		}

		return cw;
	}

	public static Date[] getDatesByMonth(Date date, int week) {
		List<Date> dates = new ArrayList<Date>();
		date = setTimeField(date, 5, 1);
		int day = getTimeField(setTimeField(setTimeField(date, 2, getTimeField(date, 2) + 1), 5, 0), 5);
		for (int i = 1; i <= day; i++) {
			Date temp = setTimeField(date, 5, i);
			if (getTimeField(temp, 7) == week) {
				dates.add(temp);
			}
		}
		return (Date[]) dates.toArray(new Date[0]);
	}

	public static String getChineseMonthName(Date date) {
		int w = getTimeField(date, 2);
		String cw = "";
		switch (w) {
			case 0:
				cw = "一月";
				break;
			case 1:
				cw = "二月";
				break;
			case 2:
				cw = "三月";
				break;
			case 3:
				cw = "四月";
				break;
			case 4:
				cw = "五月";
				break;
			case 5:
				cw = "六月";
				break;
			case 6:
				cw = "七月";
				break;
			case 7:
				cw = "八月";
				break;
			case 8:
				cw = "九月";
				break;
			case 9:
				cw = "十月";
				break;
			case 10:
				cw = "十一月";
				break;
			case 11:
				cw = "十二月";
				break;
		}

		return cw;
	}

	public static Date nextSevenDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, 7);
		return roundToDay(gc.getTime());
	}

	public static Date previousSevenDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(5, -7);
		return roundToDay(gc.getTime());
	}

	public static Date previousYear(Date date, int num) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(1, -num);
		return roundToDay(gc.getTime());
	}

	public static Date setTimeOfDay(Date date, int hour, int minute, int second) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(11, hour);
		gc.set(12, minute);
		gc.set(13, second);
		gc.set(14, 0);
		return gc.getTime();
	}

	public static boolean isTimeContain(Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime) {
		return (oldEndTime.getTime() > newStartTime.getTime()) && (oldStartTime.getTime() < newEndTime.getTime());
	}

	public static Date min(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			return d2;
		}
		return d1;
	}

	public static Date max(Date d1, Date d2) {
		if (d1.getTime() < d2.getTime()) {
			return d2;
		}
		return d1;
	}

	public static int compareTime(Date date1, Date date2) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(date1);
		GregorianCalendar g2 = new GregorianCalendar();
		g1.setTime(date2);

		clearYMD(g1);
		clearYMD(g2);

		if (g1.before(g2))
			return 1;
		if (g2.before(g1)) {
			return -1;
		}

		return 0;
	}

	private static void clearYMD(GregorianCalendar g) {
		g.set(1, 1900);
		g.set(2, 0);
		g.set(5, 1);
	}

	public static List<Date> listMonthOption(Date startMonth, Date endMonth) {
		List<Date> list = new ArrayList<Date>();
		Date date = endMonth;
		while (date.getTime() - startMonth.getTime() > 0L) {
			list.add(add(date, 2, -1));
		}
		return list;
	}

	public static List<Date> listMonthOption(int monthNum) {
		Date endDate = new Date();
		Date startDate = add(endDate, 2, -monthNum);
		return listMonthOption(startDate, endDate);
	}

	public static List<Date> getWeekList(Date date) {
		Date day = getFirstDayOfWeek(date);
		List<Date> dates = new ArrayList<Date>();
		for (int i = 0; i < 7; i++) {
			Date temp = addDay(day, i);
			dates.add(temp);
			if (isSameDay(temp, new Date()))
				break;
		}
		return dates;
	}

	public static boolean isNotEmptyPreviousWeek(Date date) {
		return getFirstDayOfWeek(date).compareTo(new Date()) < 0;
	}

	public static Date now() {
		return new Date();
	}

	public static final String getFormatDate(String inDate, String inFmt, String outFmt) throws ParseException {
		if ((inDate == null) || (inDate.length() < 1) || (inDate.trim().length() < 1))
			return "";
		SimpleDateFormat dtInFmt = null;
		SimpleDateFormat dtOutFmt = null;
		if ((inFmt == null) || (inFmt.length() < 1))
			dtInFmt = new SimpleDateFormat(dateInputFmt, Locale.US);
		else
			dtInFmt = new SimpleDateFormat(inFmt, Locale.US);
		if ((outFmt == null) || (outFmt.length() < 1))
			dtOutFmt = new SimpleDateFormat(dateOutputFmt, Locale.US);
		else
			dtOutFmt = new SimpleDateFormat(outFmt, Locale.US);
		return dtOutFmt.format(dtInFmt.parse(inDate));
	}

	public static final long dateToMinute(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf= new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = sdf.parse(endDate);
		Date start = sdf.parse(startDate);
		return (end.getTime() - start.getTime())/(1000*60);
	}
	
	/**
     * 
     * 计算两个日期相差的月份数
     * 
     * @param date1 日期1
     * @param date2 日期2
     * @param pattern  日期1和日期2的日期格式
     * @return  相差的月份数
     */
    public static int countMonths(String date1,String date2,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        
        
        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        
        //开始日期若小月结束日期
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }
        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
    }

	public static final long dateToSecond(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf= new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = sdf.parse(endDate);
		Date start = sdf.parse(startDate);
		return (end.getTime() - start.getTime())/1000;//秒数
	}

	public static final long dateToDate(Date startDate, Date endDate) throws ParseException {

		return (endDate.getTime() - startDate.getTime())/1000;//秒数
	}

	public static final long dateToRedisTime(Date endDate) throws ParseException {

		return (endDate.getTime() - new Date().getTime())/1000;//秒数
	}

	public static String dateToString(Date calendarDate) {
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   String dateString = formatter.format(calendarDate);
		   return dateString;
	}
	
	/**
	   * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDate(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}

	 /**
	  * 得到现在时间
	  * 
	  * @return 字符串yyyy-MM-dd
	  */
	 public static String getStringToday() {
	  Date currentTime = new Date();
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	  String dateString = formatter.format(currentTime);
	  return dateString;
	 }

	 /**
	 * unix时间戳转换
	 * timestampString需要转换的unix时间戳
	 * formats时间格式
	 * @param timestampString
	 * @param formats
	 * @return
	 */
	public static String timeStampDate(String timestampString, String formats) {
        if (StringUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

	/**
	 * date转为时间戳
	 * @param timestampString
	 * @param formats
	 * @return
	 */
	public static Integer timeStampDate(Date tdate) {
		Integer result = 0;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(tdate);
			long timestamp = cal.getTimeInMillis();
			return Integer.valueOf(String.valueOf(timestamp / 1000));
		} catch (Exception e) {
			return result;
		}
	}
	public static int  birthDays(String birthDay){
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cToday = Calendar.getInstance(); // 存今天
        Calendar cBirth = Calendar.getInstance(); // 存生日
        try {
			cBirth.setTime(myFormatter.parse(birthDay));
		} catch (ParseException e) {
			e.printStackTrace();
		} // 设置生日
        cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
        int days; 
        if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
            // 生日已经过了，要算明年的了
            days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            days += cBirth.get(Calendar.DAY_OF_YEAR);
        } else {
            // 生日还没过
            days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
        }
        return days;
	}
	
}