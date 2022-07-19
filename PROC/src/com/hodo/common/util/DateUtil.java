/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.hodo.common.config.ResourceLoader;


/**
 * 
 * @author Zhao GuoWei
 * @since ANS 1.1
 * @version Revision: Date: 2008/10/16
 */
public final class DateUtil {


	public static final String[] WEEK_NAMES = {" ", " ", " ", " ", " ", " ", " "};

	
	public static final String[] SETEI_CONTEXT = {"0", "1"};


	private DateUtil() {
	};


	public static DateFormat getDateFormat(String pattern) throws Exception {
		Map config = (Map) ResourceLoader.getInstance().getConfig("textformat-config");
		String formatText = (String) config.get(pattern);

		DateFormat df = new SimpleDateFormat(formatText);
		df.setLenient(false);
		return df;
	}


	public static boolean checkValidity(String ymd) throws Exception {
		try {
			if (parse(ymd) == null) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			return false;
		}
	}


	public static boolean checkTime(String hms) throws Exception {
		try {
			if (parse(hms) == null) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			return false;
		}
	}


	public static String convertFormat(String text, String fromPattern, String toPattern)
		throws Exception {
		if (text == null || text.trim().equals("")) {
			return null;
		}

		try {
			DateFormat formatter = getDateFormat(fromPattern);
			Date date = formatter.parse(text);

			return format(date, toPattern);

		} catch (ParseException e) {
			return null;
		}
	}

	
	public static String convertFormat2Short(String text) throws Exception {
		return convertFormat(text, "format.date.yyyymmdd", "format.date.yyyymmdd.short");
	}

	
	public static String convertFormat2Display(String text) throws Exception {
		return convertFormat(text, "format.date.yyyymmdd.short", "format.date.yyyymmdd");
	}

	
	public static String format(Date date, String pattern) throws Exception {
		if (date == null) {
			return null;
		}
		DateFormat formatter = getDateFormat(pattern);
		return formatter.format(date);
	}

	
	public static String formatYmd(Date date) throws Exception {
		return format(date, "format.date.yyyymmdd");
	}

	
	public static String formatYmdShort(Date date) throws Exception {
		return format(date, "format.date.yyyymmdd.short");
	}

	
	public static String getTodayYMDText(Connection conn) throws Exception {
		Date today = nowDate(conn);
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(today);
		int ret = currentCal.get(Calendar.YEAR) * 10000 + (currentCal.get(Calendar.MONTH) + 1)
			* 100 + currentCal.get(Calendar.DAY_OF_MONTH);

		DecimalFormat dformat = new DecimalFormat("00000000");

		return dformat.format(ret);
	}

	
	public static String getTodayHMSText(Connection conn) throws Exception {
		Date today = nowDate(conn);
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(today);
		int ret = currentCal.get(Calendar.HOUR_OF_DAY) * 10000 + currentCal.get(Calendar.MINUTE)
			* 100 + currentCal.get(Calendar.SECOND);

		DecimalFormat dformat = new DecimalFormat("000000");

		return dformat.format(ret);
	}

	/**
	 * <br>
	 * <ul>
	 * <li>format.date.yyyymmdd</li>
	 * <li>format.date.yyyymmdd.short</li>
	 * <li>format.date.yyyymm</li>
	 * <li>format.date.yyyymm.short</li>
	 * <li>format.date.yyyy</li>
	 * <li>format.date.hhmmss</li>
	 * <li>format.date.hhmmss.short</li>
	 * <li>format.date.hhmm</li>
	 * <li>format.date.hh</li>
	 * @throws Exception Exception
	 */
	public static Date parse(String value) throws Exception {
		Date date = null;
		String[] patterns = new String[]{"format.date.yyyymmdd", "format.date.yyyymmdd.short",
			"format.date.yyyymm", "format.date.yyyymm.short", "format.date.yyyy",
			"format.date.hhmmss", "format.date.hhmmss.short", "format.date.hhmm", "format.date.hh"};

		if (value == null || value.trim().equals("")) {
			return null;
		}

		for (int i = 0; i < patterns.length; i++) {
			date = parseImpl(value.trim(), patterns[i]);

			if (date != null) {
				break;
			}
		}

		return date;
	}


	public static java.sql.Date toSqlDate(String value, String pattern) throws Exception {
		Date date = parseImpl(value, pattern);
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}


	public static java.sql.Time toSqlTime(String value, String pattern) throws Exception {
		Date date = parseImpl(value, pattern);
		if (date == null) {
			return null;
		}
		return new java.sql.Time(date.getTime());
	}

	
	public static java.sql.Timestamp toSqlTimestamp(String value, String pattern) throws Exception {
		Date date = parseImpl(value, pattern);
		if (date == null) {
			return null;
		}
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * <br>
	 * <ul>
	 * <li>format.date.yyyymmdd</li>
	 * <li>format.date.yyyymmdd.long</li>
	 * <li>format.date.yyyymmdd.short</li>
	 * <li>format.date.yyyymd</li>
	 * <li>format.date.yyyymd.long</li>
	 * <li>format.date.yymmdd</li>
	 * <li>format.date.yymmdd.long</li>
	 * <li>format.date.yymd</li>
	 * <li>format.date.yymd.long</li>
	 * <li>format.date.yyyymm</li>
	 * <li>format.date.yyyymm.long</li>
	 * <li>format.date.yyyymm.short</li>
	 * <li>format.date.yyyym</li>
	 * <li>format.date.yyyym.long</li>
	 * <li>format.date.yymm</li>
	 * <li>format.date.yymm.long</li>
	 * <li>format.date.yym</li>
	 * <li>format.date.yym.long</li>
	 * </ul>
	 * @throws Exception Exception
	 */
	public static Date parseImportData(String value) throws Exception {
		Date date = null;

		String[] patterns = new String[]{"format.date.yyyymmdd", "format.date.yyyymmdd.long",
			"format.date.yyyymmdd.short", "format.date.yyyymd", "format.date.yyyymd.long",
			"format.date.yymmdd", "format.date.yymmdd.long", "format.date.yymd",
			"format.date.yymd.long", "format.date.yyyymm", "format.date.yyyymm.long",
			"format.date.yyyymm.short", "format.date.yyyym", "format.date.yyyym.long",
			"format.date.yymm", "format.date.yymm.long", "format.date.yym", "format.date.yym.long"};

		for (int i = 0; i < patterns.length; i++) {
			try {
				date = getDateFormat(patterns[i]).parse(value);
			} catch (ParseException e) {
				date = null;
			}

			if (date != null) {
				break;
			}
		}

		return date;
	}


	public static Date parse(String value, String pattern) throws Exception {
		return parseImpl(value, pattern);
	}

	
	public static Date parseHour(String value, String pattern) throws Exception {
		return parseHour(value, pattern, true);
	}

	
	public static Date parseHour(String value, String pattern, boolean lenient) throws Exception {
		return parseHourImpl(value, pattern, lenient);
	}

	
	public static Date parseHour(String value) throws Exception {
		return parseHour(value, true);
	}

	
	public static Date parseHour(String value, boolean lenient) throws Exception {

		if (value == null) {
			return null;
		}
		Date date = null;
		String[] patterns = new String[]{"format.date.hhmm", "format.date.hhmm.short",
			"format.date.hhmmss", "format.date.hhmmss.short"};

		for (int i = 0; i < patterns.length; i++) {
			date = parseHourImpl(value.trim(), patterns[i], lenient);

			if (date != null) {
				break;
			}
		}
		return date;
	}

	
	private static Date parseHourImpl(String value, String pattern, boolean lenient)
		throws Exception {
		Map config = (Map) ResourceLoader.getInstance().getConfig("textformat-config");
		String formatText = (String) config.get(pattern);

		if ((formatText != null && value != null) && formatText.length() == value.length()) {

			try {

				DateFormat format = getDateFormat(pattern);
				format.setLenient(lenient);

				return format.parse(value);

			} catch (ParseException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	
	private static Date parseImpl(String value, String pattern) throws Exception {
		Map config = (Map) ResourceLoader.getInstance().getConfig("textformat-config");
		String formatText = (String) config.get(pattern);

		if ((formatText != null && value != null) && formatText.length() == value.length()) {

			try {
				return getDateFormat(pattern).parse(value);
			} catch (ParseException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	public static Date parseBefore(String value) throws Exception {

		if (value == null || value.trim().equals("")) {
			return new Date(Long.MIN_VALUE);
		}

		Date date = parse(value);
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(date);
		currentCal.set(Calendar.HOUR_OF_DAY, 0);
		currentCal.set(Calendar.MINUTE, 0);
		currentCal.set(Calendar.SECOND, 0);
		currentCal.set(Calendar.MILLISECOND, 0);

		return currentCal.getTime();

	}

	
	public static Date parseAfter(String value) throws Exception {
		if (value == null || value.trim().equals("")) {
			return new Date(Long.MAX_VALUE);
		}

		Date date = parse(value);
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(date);
		currentCal.set(Calendar.HOUR_OF_DAY, 23);
		currentCal.set(Calendar.MINUTE, 59);
		currentCal.set(Calendar.SECOND, 59);
		currentCal.set(Calendar.MILLISECOND, 999);

		return currentCal.getTime();
	}

	
	public static String parseText(String value) throws Exception {
		if (value == null || value.trim().equals("")) {
			return null;
		}

		Date date = parse(value);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int ret = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100
			+ calendar.get(Calendar.DAY_OF_MONTH);

		return (new DecimalFormat("00000000")).format(ret);

	}

	
	public static String getLastDateOfMonth(Object value) {
		String lastDate = "";
		try {
			if (value != null) {
				if (value instanceof String) {
					value = parse((String) value);
				}

				if (value instanceof Date) {
					Calendar cd = Calendar.getInstance();
					cd.setTime((Date) value);
					String strMonth = String.valueOf(cd.get(Calendar.MONTH) + 1);
					if (strMonth.length() == 1) {
						strMonth = "0" + strMonth;
					}
					lastDate = String.valueOf(cd.get(Calendar.YEAR)) + "/" + strMonth + "/"
						+ cd.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
			}
			return lastDate;

		} catch (Exception ex) {
			return lastDate;
		}
	}

	
	public static String getWeekDayName(Object value) {
		String weekDay = "";
		try {
			if (value != null) {
				if (value instanceof String) {
					Date tmpDate = parse((String) value);
					if (!Util.isEmpty(tmpDate)) {
						Calendar cd = Calendar.getInstance();
						cd.setTime(tmpDate);
						weekDay = WEEK_NAMES[cd.get(Calendar.DAY_OF_WEEK) - 1];
					}
				} else if (value instanceof Date) {
					Calendar cd = Calendar.getInstance();
					cd.setTime((Date) value);
					weekDay = WEEK_NAMES[cd.get(Calendar.DAY_OF_WEEK) - 1];
				}
			}
			return weekDay;

		} catch (Exception ex) {
			return weekDay;
		}
	}

	
	public static BigDecimal getWeekDay(Object value) {
		BigDecimal bdResult = null;
		Calendar cal = Calendar.getInstance();
		try {
			if (value != null) {
				Date valDate = null;
				if (value instanceof String) {
					valDate = parse((String) value);
				} else {
					valDate = (Date) value;
				}
				cal.setTime(valDate);
				bdResult = BigDecimal.valueOf(cal.get(Calendar.DAY_OF_WEEK));
			}
			return bdResult;
		} catch (Exception ex) {
			return bdResult;
		}
	}

	
	public static Date nowDate(Connection conn) throws Exception { 
			return null;
//		}
	}

	
	public static Calendar nowCalendar() {
		return Calendar.getInstance();
	}
	public static String getNextMonth(String date) {

		String nextMonth = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					Calendar cal = Calendar.getInstance();
					int year = Integer.parseInt(date.substring(0, 4));
					int month = Integer.parseInt(date.substring(4, 6)) + 1;
					cal.set(year, month, 0, 0, 0, 0);
					Date nextDate = cal.getTime();
					return DateUtil.format(nextDate, "format.date.yyyymm.short");
				}
			}
		} catch (Exception ex) {
			return nextMonth;
		}

		return nextMonth;
	}

	public static String getPreMonth(String date) {

		String preMonth = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					Calendar cal = Calendar.getInstance();
					int year = Integer.parseInt(date.substring(0, 4));
					int month = Integer.parseInt(date.substring(4, 6)) - 1;
					cal.set(year, month, 0, 0, 0, 0);
					Date nextDate = cal.getTime();
					return DateUtil.format(nextDate, "format.date.yyyymm.short");
				}
			}
		} catch (Exception ex) {
			return preMonth;
		}

		return preMonth;
	}

	public static String getNextYear(String date) {

		String next = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					int year = Integer.parseInt(date) + 1;
					return String.valueOf(year);
				}
			}
		} catch (Exception ex) {
			return next;
		}

		return next;
	}

	public static String getPreYear(String date) {

		String pre = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					int year = Integer.parseInt(date) - 1;
					return String.valueOf(year);
				}
			}
		} catch (Exception ex) {
			return pre;
		}

		return pre;
	}

	public static String getNumTimeFmt(int nMinute) {
		if (nMinute == 0) {
			return "";
		}

		String mod = String.valueOf(nMinute % 60);
		if (mod.length() == 1) {
			mod = "0" + mod;
		}
		return nMinute / 60 + ":" + mod;
	}

	public static String getTimeFmtShort(String sTime) {

		if (Util.isEmpty(sTime)) {

			return "";
		}

		sTime = sTime.replaceAll(":", "");

		return sTime;
	}

	public static String getTimeFmt(String sTime) {

		if (Util.isEmpty(sTime)) {

			return "";
		}

		String strTime = null;
		if (sTime.length() == 6) {
			strTime = (sTime.substring(0, 2).concat(":").concat((sTime.substring(2, 4)))
				.concat(":").concat((sTime.substring(4, 6))));
		} else if (sTime.length() == 4) {
			strTime = (sTime.substring(0, 2).concat(":").concat((sTime.substring(2, 4))));
		} else {
			strTime = "";
		}

		return strTime;
	}

	public static String getFirstDay(String date) {

		String firstDay = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(tmpDate);
					int minDate = cal.getActualMinimum(Calendar.DATE);
					cal.set(Calendar.DATE, minDate);

					return DateUtil.format(cal.getTime(), "format.date.yyyymmdd.short");
				}
			}

		} catch (Exception ex) {

			return firstDay;
		}

		return firstDay;
	}

	public static String getLastDay(String date) {

		String lastDay = "";
		try {
			if (date != null) {
				Date tmpDate = parse((String) date);
				if (!Util.isEmpty(tmpDate)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(tmpDate);
					int maxDate = cal.getActualMaximum(Calendar.DATE);
					cal.set(Calendar.DATE, maxDate);
					return DateUtil.format(cal.getTime(), "format.date.yyyymmdd.short");
				}
			}

		} catch (Exception ex) {

			return lastDay;
		}

		return lastDay;
	}

	public static String getPrevDay(String ymdDate) throws Exception {

//		return getPrevDay(ymdDate, TastyCommonConsts.PREVYEARSAMEDAY_SETTEITEISU);
		return null;
	}

	public static String getPrevDay(String ymdDate, String flg) throws Exception {

		String strDate = null;

		if (DateUtil.checkValidity(ymdDate)) {

			Calendar thisYear = Calendar.getInstance();
			thisYear.setTime(DateUtil.parse(ymdDate));

			int nowWeek = thisYear.get(Calendar.DAY_OF_WEEK);

			Calendar lastYear = Calendar.getInstance();
			lastYear.setTime(DateUtil.parse(ymdDate));
			lastYear.set(lastYear.get(Calendar.YEAR) - 1, lastYear.get(Calendar.MONTH), lastYear
				.get(Calendar.DAY_OF_MONTH));

			int lastWeek = lastYear.get(Calendar.DAY_OF_WEEK);

			if (Util.isEquals("0", flg)) {

				strDate = DateUtil.formatYmdShort(lastYear.getTime());
			} else if (Util.isEquals("1", flg)) {

				int minusValue = Math.abs(nowWeek - lastWeek);

				int absoluteValue = 1;

				if (minusValue == 2 || minusValue == 5) {

					absoluteValue = 2;
				}

				lastYear.set(Calendar.DAY_OF_MONTH, lastYear.get(Calendar.DAY_OF_MONTH)
					+ absoluteValue);

				strDate = DateUtil.formatYmdShort(lastYear.getTime());
			}
		}

		return strDate;
	}

	public static long subtract(String date1, String date2) throws Exception {

		long theday = 0;
		Map config = (Map) ResourceLoader.getInstance().getConfig("textformat-config");
		String formatText = (String) config.get("format.date.yyyymmdd.short");

		SimpleDateFormat sdf = new SimpleDateFormat(formatText);

		java.util.Date d1 = sdf.parse(date1);
		java.util.Date d2 = sdf.parse(date2);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d1);
		long timethis = calendar.getTimeInMillis();

		calendar.setTime(d2);
		long timeend = calendar.getTimeInMillis();

		theday = (timethis - timeend) / (1000 * 60 * 60 * 24);

		return theday;
	}

	
	public static String getAddMonth(String viewDate, int months) throws Exception {

		String strDate = "";
		try {
			if (!Util.isEmpty(viewDate)) {
				Date tmpDate = DateUtil.parse((String) viewDate);
				if (!Util.isEmpty(tmpDate)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tmpDate);
					calendar.add(Calendar.MONTH, months);
					Date nextDate = calendar.getTime();
					return DateUtil.format(nextDate, "format.date.yyyymm.short");
				}
			}
		} catch (Exception ex) {
			return strDate;
		}
		return strDate;
	}

	public static String getTimeFmthhmmss(String inputtime) {

		if (inputtime == null || "".equals(inputtime)) {
			return inputtime;
		}

		inputtime = inputtime.replaceAll(":", "");

		if (inputtime.length() != 6) {
			return null;
		}

		try {
			Integer.parseInt(inputtime);
		} catch (NumberFormatException e) {
			return null;
		}

//		Perl5Util util = new Perl5Util();
//		if (util.match("/:/", inputtime)) {
//			return null;
//		}

		StringBuffer sbuf = new StringBuffer();
		sbuf.append(inputtime.substring(0, 2));
		sbuf.append(":");
		sbuf.append(inputtime.substring(2, 4));
		sbuf.append(":");
		sbuf.append(inputtime.substring(4, 6));
		return sbuf.toString();
	}

	public static String getAddDay(String viewDate, int days) throws Exception {

		String strDate = "";
		try {
			if (!Util.isEmpty(viewDate)) {
				Date tmpDate = DateUtil.parse((String) viewDate);
				if (!Util.isEmpty(tmpDate)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tmpDate);
					calendar.add(Calendar.DAY_OF_YEAR, days);
					Date nextDate = calendar.getTime();
					return DateUtil.format(nextDate, "format.date.yyyymmdd.short");
				}
			}
		} catch (Exception ex) {
			return strDate;
		}
		return strDate;
	}
	public static Timestamp getAddDay(Timestamp viewTimestamp,int days) throws Exception {

		Timestamp resultTimestamp =new Timestamp(System.currentTimeMillis());
		try {
			if (!Util.isEmpty(viewTimestamp)) {
				Date tmpDate = new Date(viewTimestamp.getTime());
				if (!Util.isEmpty(tmpDate)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tmpDate);
					calendar.add(Calendar.DAY_OF_YEAR, days);
					Date nextDate = calendar.getTime();
					resultTimestamp = new Timestamp(nextDate.getTime());
					return resultTimestamp;
				}
			}
		} catch (Exception ex) {
			return resultTimestamp;
		}
		return resultTimestamp;
	}
	public static int compareTo(String date1, String date2) throws Exception {

		try {
			Date d1 = parse(date1);
			Date d2 = parse(date2);

			return compareTo(d1, d2);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static int compareTo(Date date1, Date date2) throws Exception {

		int relation = 0;

		try {
			if (date1.equals(date2)) {
				relation = 0;
			} else if (date1.before(date2)) {
				relation = -1;
			} else {
				relation = 1;
			}
		} catch (Exception ex) {
			throw ex;
		}

		return relation;
	}

	public static int calcJisu(String startTime, String endTime) throws Exception {

		BigDecimal minuts = new BigDecimal(0);
		BigDecimal sMinuts = new BigDecimal(0);
		BigDecimal eMinuts = new BigDecimal(0);

		if (Util.isEmpty(startTime) || Util.isEmpty(endTime)) {

			return minuts.intValue();
		} else if (startTime.length() != 5 || endTime.length() != 5) {

			return minuts.intValue();
		} else {

			sMinuts = new BigDecimal(startTime.substring(0, 2)).multiply(new BigDecimal(60)).add(
				new BigDecimal(startTime.substring(3, 5)));

			eMinuts = new BigDecimal(endTime.substring(0, 2)).multiply(new BigDecimal(60)).add(
				new BigDecimal(endTime.substring(3, 5)));

			if (sMinuts.compareTo(eMinuts) == 1) {

				eMinuts = new BigDecimal(24).multiply(new BigDecimal(60)).add(eMinuts);
			}

			minuts = eMinuts.subtract(sMinuts);
		}

		return minuts.intValue();
	}

	public static String calcMarume(String time, String marumeTime, String marumeFlg)
		throws Exception {

		String afterTime = null;
		String hnour = null;
		String minuts = null;

		String marumeMinnuts = null;

		if (Util.isEmpty(time) || time.length() != 5) {

			return afterTime;
		}

		hnour = time.substring(0, 2);
		minuts = time.substring(3, 5);

		if (Util.isEmpty(marumeTime) && !Util.isEquals("0", marumeTime)) {

			if (Util.isEquals("0", marumeFlg)) {

				marumeMinnuts = NumberUtil.nullToZero(new BigDecimal(minuts).divide(
					new BigDecimal(marumeTime), 1).multiply(new BigDecimal(marumeTime)));

				if (marumeMinnuts.length() == 1) {

					marumeMinnuts = "0" + marumeMinnuts;
				}
			} else if (Util.isEquals("1", marumeFlg)) {

				marumeMinnuts = NumberUtil.nullToZero(new BigDecimal(minuts).divide(
					new BigDecimal(marumeTime), 0).multiply(new BigDecimal(marumeTime)));

				if (marumeMinnuts.length() == 1) {

					marumeMinnuts = "0" + marumeMinnuts;
				}
			}

			if (new BigDecimal(60).equals(marumeMinnuts)) {

				hnour = String.valueOf(new BigDecimal(hnour).add(new BigDecimal(1)));

				if (Util.isEquals("24", hnour)) {

					hnour = "00";
				}

				afterTime = hnour + ":00";
			} else {

				if (Util.isEquals("24", hnour)) {

					hnour = "00";
				}

				afterTime = hnour + ":" + marumeMinnuts;
			}
		}

		return afterTime;
	}


	public static String getReportTime(Connection conn) throws Exception {

		String date = DateUtil.formatYmd(DateUtil.nowDate(conn));

		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(2);
		formatter.setMaximumIntegerDigits(2);
		Calendar cd = Calendar.getInstance();
		String hh = formatter.format(cd.get(Calendar.HOUR_OF_DAY));
		String mm = formatter.format(cd.get(Calendar.MINUTE));

		String reportTime = date.concat(" ").concat(hh.concat(":").concat(mm));

		return reportTime;
	} 
	
	// gdszm 20100923 ADD
  	public static String getTodayYMDText(){
		Calendar currentCal = Calendar.getInstance();

		int ret = currentCal.get(Calendar.YEAR) * 10000 + (currentCal.get(Calendar.MONTH) + 1)
			* 100 + currentCal.get(Calendar.DAY_OF_MONTH);

		DecimalFormat dformat = new DecimalFormat("00000000");

		return dformat.format(ret);
	}
	// gdszm 20140415 ADD
	public static String getTimeText(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeText = dateFormat.format(date);
		return timeText;
	}
	// gdszm 20140415 ADD
	public static String getDateText(Timestamp timestamp) throws Exception {
		Date date = new Date(timestamp.getTime());
		return DateUtil.format(date, "format.date.yyyymmddhhmmss");
	}
	
	// gdszm 20141012 ADD
	public static Timestamp getCurrentTimestamp() throws Exception {
		return toSqlTimestamp(getTodayYMDText(), "format.date.yyyymmdd.short");
	}
	
	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 字符串转日期
	 * 
	 * @param yyyy-MM-dd HH:mm:ss,yyyy-MM-dd
	 * @return
	 */
	public static Date dateToString(String date,String pattern) {
		Date dateR = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			dateR = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateR;
	}
	/**
	 * 字符串转日期
	 * 
	 * @param yyyy-MM-dd HH:mm:ss,yyyy-MM-dd
	 * @return
	 */
	public static Date stringToDate(String date,String pattern) {
		Date dateR = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			dateR = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateR;
	}
	/**
	 * 时间计算
	 * 
	 * @param date ...前
	 * @return
	 */
	public static String dateMinus(Date date) throws Exception{
		// 刚刚 0<x<60秒
		// 1分钟前  -60分钟   秒数/60    分钟前
		// 1小时前3-24小时前  分钟数/60 小时前
		// 1天前 29天前 小时数/24 天前
		// 1个月前 --12个月前 天数/30 月前 
		// 1年前 100年前 月数/12 年前
		
		//秒数 1
		//分钟数 60
		//小时数 1*60*60
		//天数 1*24*60*60
		//月数 1*30*24*60*60
		//年数 1*12*30*24*60*60 
		// 现在-x=t
		
		//现在时间
		Calendar calendar = Calendar.getInstance();
		Date nowDate = calendar.getTime();
		Long long1 = nowDate.getTime();
		BigDecimal BigDecimal1 = NumberUtil.toBigDecimal(long1);
		//发布时间 
		Long long2 = date.getTime();;
		BigDecimal BigDecimal2 = NumberUtil.toBigDecimal(long2);
		//计算秒数差
		BigDecimal bigDecimalSc = (BigDecimal1.subtract(BigDecimal2)).divide(new BigDecimal(1000));

		BigDecimal ss = bigDecimalSc.divideToIntegralValue(new BigDecimal(1));
		BigDecimal mm = bigDecimalSc.divideToIntegralValue(new BigDecimal(60));
		BigDecimal hh = bigDecimalSc.divideToIntegralValue(new BigDecimal(60*60));
		BigDecimal d = bigDecimalSc.divideToIntegralValue(new BigDecimal(24*60*60));
		BigDecimal m = bigDecimalSc.divideToIntegralValue(new BigDecimal(30*24*60*60));
		BigDecimal y = bigDecimalSc.divideToIntegralValue(new BigDecimal(12*30*24*60*60));

		if(y.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(y.intValue()) + "年前";
		} else 
			if(m.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(m.intValue()) + "个月前";
		} else
			if(d.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(d.intValue()) + "天前";
		} else
			if(hh.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(hh.intValue()) + "小时前";
		} else
			if(mm.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(mm.intValue()) + "分钟前";
		} else
			if(ss.compareTo(new BigDecimal(0)) == 1) {
			 return  String.valueOf(ss.intValue()) + "秒前";
		} else {
			return  "刚刚";
		}
	}
}