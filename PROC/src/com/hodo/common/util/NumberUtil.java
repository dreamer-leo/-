/*
 * ANS
 * COPYRIGHT(C) 2008-2008 Qualica Inc.
 *
 * Author: Zhao GuoWei
 * Creation Date : 2008/10/15
 */
package com.hodo.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hodo.common.config.ResourceLoader;


public class NumberUtil {
	private static final BigDecimal ZERO = new BigDecimal(0);

	private static final BigDecimal ONE = new BigDecimal(1);

	private static Pattern overScalePattern = Pattern.compile("\\.[0-9]*0+$");

	private static Pattern trimScalePattern = Pattern.compile("(\\.0+$|0+$)");

	private NumberUtil() {
	}

	public static boolean checkValidity(String value) {
		return checkValidityImpl(value) == -1;
	}

	public static int checkValidityImpl(String value) {

		if (value == null || "".equals(value)) {
			return -1;
		}

		byte[] chars = value.getBytes();
		int periodCount = 0;
		int numberCount = 0;

		for (int i = 0; i < chars.length; i++) {

			switch (chars[i]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				numberCount++;
				break;
			case ',':
				if (chars.length == 1 || (numberCount == 0 && i == chars.length - 1)) {
					return i;
				}
				break;
			case '.':
				periodCount++;

				if (periodCount > 1 || chars.length == 1) {
					return i;
				}

				break;

			case '+':
			case '-':
				if (i != 0 || chars.length == 1) {
					return i;
				}

				break;

			default:
				return i;
			}
		}

		return -1;
	}

	public static Number parse(String value) throws ParseException {

		if (value == null || "".equals(value)) {
			return null;
		}

		int paeseRet = checkValidityImpl(value);

		if (paeseRet != -1) {
			throw new ParseException(value, paeseRet);
		}

		value = value.trim();

		if (value.startsWith("+")) {
			value = value.substring(1);

		}

		Matcher overScaleMatcher = overScalePattern.matcher(value);

		if (overScaleMatcher.find()) {
			Matcher trimScaleMatcher = trimScalePattern.matcher(value);
			value = trimScaleMatcher.replaceAll("");

		}

		return new BigDecimal(value.replaceAll(",", ""));
	}

	public static String parseText(String value) throws ParseException {

		if (value == null || "".equals(value)) {
			return null;
		}
		return String.valueOf(parse(value));
	}

	public static String format(Number value, int decimalPlace) {

		if (value == null || "".equals(value)) {
			return null;
		}
		String formatText = null;

		if (decimalPlace == 0) {
			formatText = "#,##0";
		} else {
			char[] formatChars = new char[6 + decimalPlace];

			formatChars[0] = '#';
			formatChars[1] = ',';
			formatChars[2] = '#';
			formatChars[3] = '#';
			formatChars[4] = '0';
			formatChars[5] = '.';

			for (int i = 0; i < decimalPlace; i++) {
				formatChars[i + 6] = '0';
			}

			formatText = new String(formatChars);

		}

		NumberFormat formata = new DecimalFormat(formatText);

		BigDecimal roundValue = new BigDecimal(value.toString());

		roundValue = roundValue.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);

		return formatImpl(roundValue, formata);

	}

	public static String format(Number value) {

		if (value == null || "".equals(value)) {
			return null;
		}

		String formatText = "#,##0.###";
		NumberFormat formata = new DecimalFormat(formatText);
		return formatImpl(new BigDecimal(value.toString()), formata);
	}

	public static String formatText(String value, int decimalPlace) throws ParseException {
		return format(parse(value), decimalPlace);
	}
	private static String formatImpl(BigDecimal num, NumberFormat formata) {
		BigDecimal integral = num.divide(ONE, 0, BigDecimal.ROUND_DOWN);
		BigDecimal decimal = num.subtract(integral);

		String integralText = integral.abs().toString();
		String decimalText = formata.format(decimal.abs());

		StringBuffer integralTextBuffer = new StringBuffer(integralText);
		int startPos = integralText.length() % 3;

		if (startPos == 0) {
			startPos = 3;
		}

		for (int i = startPos; i < integralTextBuffer.length(); i += 4) {
			integralTextBuffer.insert(i, ',');
		}

		decimalText = decimalText.substring(1);

		return (num.signum() < 0 ? "-" : "") + integralTextBuffer.toString() + decimalText;
	}

	public static BigDecimal adjustRemainder(BigDecimal target, String modeStr, Number keta)
		throws Exception {

		int mode = 0;
		int scale = 0;
		BigDecimal tmpResult = null;

		Map config = (Map) ResourceLoader.getInstance().getConfig("itemname-config");
		String roundDown = (String) config.get("itemName.ratecalchasuskbn.down.value"); 
		String round = (String) config.get("itemName.ratecalchasuskbn.half.value"); 
		String roundUp = (String) config.get("itemName.ratecalchasuskbn.up.value"); 

		if (modeStr == null) {
			return target;

		} else if (modeStr.equals(roundDown)) {

			mode = BigDecimal.ROUND_DOWN;

		} else if (modeStr.equals(round)) {

			mode = BigDecimal.ROUND_HALF_UP;

		} else if (modeStr.equals(roundUp)) {

			mode = BigDecimal.ROUND_UP;

		} else {
			return target;

		}

		scale = keta.intValue();

		tmpResult = target.movePointLeft(scale);
		tmpResult = tmpResult.setScale(0, mode);
		tmpResult = tmpResult.movePointRight(scale);

		return tmpResult;
	}

	public static BigDecimal toBigDecimal(Object obj) throws Exception {
		if (obj == null || "".equals(obj)) {
			return ZERO;
		} else if (obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		} else {
			try {
				return new BigDecimal(obj.toString());
			} catch (Exception e) {
				return (BigDecimal) parse(obj.toString());
			}
		}
	}

	public static BigDecimal roundDivide(BigDecimal value1, BigDecimal value2, int scale) {

		BigDecimal rslt = new BigDecimal(0);

		if (value1 != null && value2 != null && value2.doubleValue() != 0) {
			rslt = value1.divide(value2, scale, BigDecimal.ROUND_HALF_UP);
		}
		return rslt;
	}

	public static BigDecimal[] proportDist(BigDecimal totalVal, int scale, BigDecimal[] base) {

		if (totalVal == null) {
			totalVal = new BigDecimal(0);
		}

		BigDecimal[] resultArr = null;
		BigDecimal[] rsltArr = null;

		if (base == null || base.length == 0) {
			resultArr = new BigDecimal[1];
			resultArr[0] = totalVal;
		} else {
			BigDecimal sumBase = new BigDecimal(0);
			for (int i = 0; i < base.length; i++) {
				sumBase = sumBase.add(base[i]);
			}
			rsltArr = new BigDecimal[base.length];
			for (int i = 0; i < rsltArr.length; i++) {
				rsltArr[i] = totalVal.multiply(base[i]).divide(sumBase, scale,
					BigDecimal.ROUND_HALF_UP);
			}
		}

		return rsltArr;
	}

	public static String nullToZero(Object str) {

		if (str == null || "".equals(str)) {
			return "0";
		}

		return str.toString();
	}

	public static String getHiritu(BigDecimal hijyoSu, BigDecimal jyoSu, int scale, String flg)
		throws Exception {

		Map config = (Map) ResourceLoader.getInstance().getConfig("itemname-config");

		BigDecimal hirituBigDecimal = new BigDecimal(0);
		BigDecimal hyaku = new BigDecimal(100);

		String strHiritu = "";

		if (!jyoSu.equals(new BigDecimal(0))) {

			if (Util.isEquals("0", flg)) {

				hirituBigDecimal = NumberUtil.roundDivide(hijyoSu.multiply(hyaku), jyoSu, scale);
			} else {

				hirituBigDecimal = NumberUtil.roundDivide(hijyoSu, jyoSu, scale);
			}

			if (hirituBigDecimal.toString().length() > 6) {

				strHiritu = (String) config.get("asutarisuku");
			} else {

				strHiritu = hirituBigDecimal.toString();
			}
		} else {

			strHiritu = (String) config.get("hyphen");
		}

		return strHiritu;
	}
}
