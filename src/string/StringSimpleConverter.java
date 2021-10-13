package string;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符的一些处理操作：如全半角转换等
 * */
public class StringSimpleConverter {

    /** 字符类型 */
    // 未识别
    public static final int CHAR_USELESS = 0;
    // 数字
    public static final int CHAR_ARABIC = 0X00000001;
    // 英文字符
    public static final int CHAR_ENGLISH = 0X00000002;
    // 中文字符
    public static final int CHAR_CHINESE = 0X00000004;
    // 日韩字符
    public static final int CHAR_OTHER_CJK = 0X00000008;

	// 全角空格
	public static final char FULL_SPACE = 12288;
	// 半角空格
	public static final char HALF_SPACE = 32;
	// ascii字符(33-126)， unicode字符(65281-65374)
	public static final char ASCII_START = 33;
	public static final char ASCII_END = 126;
	public static final char UNICODE_START = 65281;
	public static final char UNICODE_END = 65374;
	// 全角半角转换间隔
	public static final char HALF_FULL_STEP = 65248;
	// 匹配unicode编码的正则表达式
	private static Pattern UNICODE_PATTERN = Pattern.compile("\\\\u([0-9a-fA-F]{1,4})");
	
	
	// 声调字符(单字符)对应表
	private static Map<Character, int[]> toneCharTable = null;
	// 声调字符串(两个字符)对应表
	private static Map<String, int[]> toneStringTable = null;
	
	// unicode编码对应表
	private static String[] unicodeHexDigitTable = null;
	
	
	// 初始化unicode字符对应表
	private static void initUnicodeHexDigitTable() {
		if (unicodeHexDigitTable == null) {
			synchronized (StringSimpleConverter.class) {
				if (unicodeHexDigitTable == null) {
					unicodeHexDigitTable = new String[256];
					final char byteDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
							'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
					for (int i = 0; i < 256; i++) {
						char c = (char)i;
						char[] array = {byteDigit[(c >> 4) & 0x0f], byteDigit[c & 0x0f]};
						unicodeHexDigitTable[i] = new String(array);
					}
				}
			}
		}
	}

	// 初始化声调对应表
	private static void initToneTable() {
		if (toneCharTable == null || toneStringTable == null) {
			synchronized (StringSimpleConverter.class) {
				if (toneCharTable == null) {
					toneCharTable = new HashMap<Character, int[]>();
					toneCharTable.put('ā', new int[]{(int)'a', 1});
					toneCharTable.put('á', new int[]{(int)'a', 2});
					toneCharTable.put('ǎ', new int[]{(int)'a', 3});
					toneCharTable.put('à', new int[]{(int)'a', 4});
					toneCharTable.put('ē', new int[]{(int)'e', 1});
					toneCharTable.put('é', new int[]{(int)'e', 2});
					toneCharTable.put('ế', new int[]{(int)'e', 2});
					toneCharTable.put('ě', new int[]{(int)'e', 3});
					toneCharTable.put('è', new int[]{(int)'e', 4});
					toneCharTable.put('ề', new int[]{(int)'e', 4});
					toneCharTable.put('ê', new int[]{(int)'e', 5});
					toneCharTable.put('ī', new int[]{(int)'i', 1});
					toneCharTable.put('í', new int[]{(int)'i', 2});
					toneCharTable.put('ǐ', new int[]{(int)'i', 3});
					toneCharTable.put('ì', new int[]{(int)'i', 4});
					toneCharTable.put('ḿ', new int[]{(int)'m', 2});
					toneCharTable.put('ń', new int[]{(int)'n', 2});
					toneCharTable.put('ň', new int[]{(int)'n', 3});
					toneCharTable.put('ǹ', new int[]{(int)'n', 4});
					toneCharTable.put('ō', new int[]{(int)'o', 1});
					toneCharTable.put('ó', new int[]{(int)'o', 2});
					toneCharTable.put('ǒ', new int[]{(int)'o', 3});
					toneCharTable.put('ò', new int[]{(int)'o', 4});
					toneCharTable.put('ū', new int[]{(int)'u', 1});
					toneCharTable.put('ú', new int[]{(int)'u', 2});
					toneCharTable.put('ǔ', new int[]{(int)'u', 3});
					toneCharTable.put('ù', new int[]{(int)'u', 4});
					toneCharTable.put('ǘ', new int[]{(int)'ü', 2});
					toneCharTable.put('ǚ', new int[]{(int)'ü', 3});
					toneCharTable.put('ǜ', new int[]{(int)'ü', 4});
				}
				if (toneStringTable == null) {
					toneStringTable = new HashMap<String, int[]>();
					toneStringTable.put("ê̄", new int[]{(int)'e', 1});
					toneStringTable.put("ê̌", new int[]{(int)'e', 3});
					toneStringTable.put("ḿ̀", new int[]{(int)'m', 4});
				}
			}
		}
	}
	
	public static String encodeToUnicodeString(char input) {
		initUnicodeHexDigitTable();
		int hi = input >>> 8;
		int lo = input & 0x0ff;
		return "\\u" + unicodeHexDigitTable[hi] + unicodeHexDigitTable[lo];
	}
	
	public static String encodeUnicodeString(String input) {
		if (input == null || input.length() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c < 256) {
				result.append(c);
			} else {
				result.append(encodeToUnicodeString(c));
			}
		}
		return result.toString();
	}
	
	public static String decodeUnicodeString(String unicode) {
		if (unicode == null || unicode.length() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		Matcher matcher = UNICODE_PATTERN.matcher(unicode);
		int begin = 0;
		while (matcher.find()) {
			int start = matcher.start();
			if (start > 0 && unicode.charAt(start - 1) == '\u0003') {
				if (start - 1 > begin) {
					result.append(unicode, begin, start - 1);
				}
				begin = start;
				continue;
			}
			result.append(unicode, begin, start);
			String mc = matcher.group(1);
			try {
				char value = (char) Integer.parseInt(mc, 16);
				result.append(value);
				begin = matcher.end();
			} catch (NumberFormatException exp) {
			}
		}
		if (begin < unicode.length()) {
			result.append(unicode, begin, unicode.length());
		}
		return result.toString();
	}

	public static int identifyCharType(char input) {
		if ('0' <= input && input <= '9') {
			return CHAR_ARABIC;
		} else if ('a' <= input && input <= 'z'
				|| 'A' <= input && input <= 'Z') {
			return CHAR_ENGLISH;
		}  else {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);
			if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
				return CHAR_CHINESE;
			} else if (
					// 全角数字与字符
					ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
					// 韩文字符
					|| ub == Character.UnicodeBlock.HANGUL_SYLLABLES
					|| ub == Character.UnicodeBlock.HANGUL_JAMO
					|| ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
					// 日文字符
					|| ub == Character.UnicodeBlock.HIRAGANA // 平假名
					|| ub == Character.UnicodeBlock.KATAKANA // 片假名
					|| ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS) {
				return CHAR_OTHER_CJK;
			}
		}
		return CHAR_USELESS;
	}

	/**
	 * 带声调字符转换为原始字符及声调编号
	 * @param tone
	 * @return [originalChar, toneNumber]
	 */
	public static int[] toneToOriginal(char tone) {
		initToneTable();
		if (toneCharTable.containsKey(tone)) {
			return toneCharTable.get(tone);
		}
		return null;
	}
	
	/**
	 * 带声调的字符转换为原始字符及声调编号（特指两个长度的声调字符）
	 * @param tone 两个长度的字符串
	 * @return
	 */
	private static int[] toneToOriginal(String tone) {
		initToneTable();
		if (toneStringTable.containsKey(tone)) {
			return toneStringTable.get(tone);
		}
		return null;
 	}
	
	/**
	 * 全角字符转半角字符
	 * @param src
	 * @return
	 */
	public static char fullToHalf(char src) {
		if (src == FULL_SPACE) {
			return HALF_SPACE;
		}
		if (UNICODE_START <= src && src <= UNICODE_END) {
			return (char)(src - HALF_FULL_STEP);
		}
		return src;
	}
	
	/**
	 * 半角字符转全角字符
	 * @param src
	 * @return
	 */
	public static char halfToFull(char src) {
		if (src == HALF_SPACE) {
			return FULL_SPACE;
		}
		if (ASCII_START <= src && src <= ASCII_END) {
			return (char)(src + HALF_FULL_STEP);
		}
		return src;
	}
	
	/**
	 * 带声调字符串转为原始字符
	 * @param tone
	 * @return
	 */
	public static String toneToOriginalCase(String tone) {
		if (tone == null || tone.length() == 0) {
			return "";
		}
		// 由于声调字符有两个字符的可能，此处一次循环同时考虑一个字符和两个字符的情况
		int index = 0;
		StringBuilder result = new StringBuilder();
		do {
			// 先处理两个字符的情况
			if (index < tone.length() - 1) {
				String ts = tone.substring(index, index + 2);
				int[] original = toneToOriginal(ts);
				if (original != null) {
					result.append((char)original[0]);
					index += 2;
					continue;
				}
			}
			// 再处理单个字符的情况
			char t = tone.charAt(index);
			int[] original = toneToOriginal(t);
			if (original != null) {
				result.append((char)original[0]);
			} else {
				result.append(t);
			}
			index += 1;
		} while (index < tone.length());
		
		return result.toString();
	}
	
	/**
	 * 半角字符串转全角字符串
	 */
	public static String halfToFullCase(String half) {
		if (half == null || half.length() == 0) {
			return "";
		}
		char[] halfs = half.toCharArray();
		for (int i = 0; i < halfs.length; i++) {
			halfs[i] = halfToFull(halfs[i]);
		}
		return new String(halfs);
	}
	
	/**
	 * 全角字符串转半角字符串
	 */
	public static String fullToHalfCase(String full) {
		if (full == null || full.length() == 0) {
			return "";
		}
		char[] fulls = full.toCharArray();
		for (int i = 0; i < fulls.length; i++) {
			fulls[i] = fullToHalf(fulls[i]);
		}
		return new String(fulls);
	}
	
	/**
	 * 判断是否能通过指定的charset对内容编码
	 * @param content 待判断的内容
	 * @param charsetName 指定的编码类型
	 * @return
	 */
	public static boolean canEncodeWithCharset(String content, String charsetName) {
		try {
		CharsetEncoder encoder = Charset.forName(charsetName).newEncoder();
		return encoder.canEncode(content);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String half = "我爱北京 天安门，ABCabczｄｅｆ_\"'.,，。：:！……？$#";
		String full = halfToFullCase(half);
		// 我爱北京　天安门，ＡＢＣａｂｃｚｄｅｆ＿＂＇．，，。：：！……？＄＃
		System.out.println(full); 
		
		half = fullToHalfCase(full);
		// 我爱北京 天安门,ABCabczdef_"'.,,。::!……?$#
		System.out.println(half);
		
		String tones = "ḿ̀我爱北京 天安门,ǎéòabcf_ê̄";
		String original = toneToOriginalCase(tones);
		// m我爱北京 天安门,aeoabcf_e
		System.out.println(original);
		
		System.out.println(identifyCharType('1')); // CHAR_ARABIC
		System.out.println(identifyCharType('a')); // CHAR_ENGLISH
		System.out.println(identifyCharType('我')); // CHAR_CHINESE
		System.out.println(identifyCharType('Ａ')); // CHAR_OTHER_CJK
		
		String unicode = "123我爱北京天安门xyz";
		// 123\u6211\u7231\u5317\u4eac\u5929\u5b89\u95e8xyz
		System.out.println(encodeUnicodeString(unicode));
		// 123我爱北京天安门xyz
		System.out.println(decodeUnicodeString(unicode));
		
		String canEncodeContent = "我爱北京tiananmen";
		System.out.println(canEncodeWithCharset(canEncodeContent, "GBK")); // true
		System.out.println(canEncodeWithCharset(canEncodeContent, "UTF-8")); // true
		canEncodeContent = "جۇڭگو خەلقى";
		System.out.println(canEncodeWithCharset(canEncodeContent, "GBK")); // false
		System.out.println(canEncodeWithCharset(canEncodeContent, "UTF-8")); // true
	}
}
