package tools.android.pushnotify;

import java.io.Serializable;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Analyze PushData.
 * 
 * @author liu_chonghui
 * 
 */
@SuppressWarnings("serial")
public class PushAssistant {

	protected static final String ACTION_RECOMMENDATION = "com.mfashiongallery.push.RECOMMENDATION";
	protected static PushAssistant instance;

	public static final String ACTION_NEWSRECOMMENDATION = ACTION_RECOMMENDATION
			+ LaunchType.TYPE_NEWS.name();

	public static PushAssistant getInstance() {
		if (null == instance) {
			synchronized (PushAssistant.class) {
				if (null == instance) {
					instance = new PushAssistant();
				}
			}
		}
		return instance;
	}

	public static void setSingletonInstance(PushAssistant singleton) {
		synchronized (PushAssistant.class) {
			if (instance != null) {
				instance = null;
			}
			instance = singleton;
		}
	}

	protected PushAssistant() {
	}

	public boolean isRecommendAction(String action) {
		return action != null && action.contains(ACTION_RECOMMENDATION);
	}

	public LaunchType parseType(Extra extra) {
		if (extra == null || extra.getType() == null) {
			return null;
		}
		return parseRtype(extra.getType());
	}

	public LaunchType parseRtype(String type) {
		if (new String("1").equalsIgnoreCase(type)) {
			return LaunchType.TYPE_NEWS;
		} else if (new String("2").equalsIgnoreCase(type)) {
			return LaunchType.TYPE_VIDEO;
		} else if (new String("3").equalsIgnoreCase(type)) {
			return LaunchType.TYPE_GIFT;
		}
		return null;
	}

	public String parseRtype(LaunchType type) {
		if (LaunchType.TYPE_NEWS == type) {
			return new String("1");
		} else if (LaunchType.TYPE_VIDEO == type) {
			return new String("2");
		} else if (LaunchType.TYPE_GIFT == type) {
			return new String("3");
		}
		return "null";
	}

	public Recommendation createRecommendationByPushData(LaunchType type, Extra extra) {
//		if (LaunchType.TYPE_NEWS == type) {
//			return getNewsRecommendationFromPushData(extra);
//		} else {
			return null;
//		}
	}

//	public Recommendation getNewsRecommendationFromPushData(Extra extra) {
//		if (extra == null || extra.getNewsid() == null) {
//			return null;
//		}
//		String messageId = decode(extra.getNewsid());
//		String uuid = genUUID(messageId);
//		NewsRecommendation result = new NewsRecommendation();
//		result.rType = LaunchType.TYPE_NEWS;
//		result.uuid = uuid;
//		result.title = decode(extra.getTitle());
//		result.display = decode(extra.getDisplay());
//		result.mpsContent = decode(extra.getMpsContent());
//
//		result.type = decode(extra.getType());
//		result.messageId = decode(messageId);
//		result.url = decode(extra.getUrl());
//		result.content = decode(extra.getContent());
//		result.time = decode(extra.getTime());
//		result.column = decode(extra.getC());
//		result.mark = decode(extra.getMark());
//		result.channelId = decode(extra.getCh() == null
//				|| extra.getCh().length() == 0 ? "abcd123456" : extra.getCh());
//		return result;
//	}

	public boolean parseOnOrOff(String onOrOff) {
		boolean status = false;
		if (new String("1").equalsIgnoreCase(onOrOff)) {
			status = true;
		} else if (new String("0").equalsIgnoreCase(onOrOff)) {
			status = false;
		}
		return status;
	}

	public String parseOnOrOff(boolean onOrOff) {
		if (onOrOff) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public String parseOnOrOff(Boolean... switches) {
		StringBuilder sb = new StringBuilder();
		for (Boolean value : switches) {
			if (value == null) {
				continue;
			}
			sb.append(parseOnOrOff(value));
		}
		return sb.toString();
	}
	
	public String getDefaultSwitches() {
		return new String();
	}
	
	public Boolean[] decodeSwitches(String onOrOff) {
		if (onOrOff == null || onOrOff.length() == 0) {
			return null;
		}
		int defaultSwitchSize = 0;
		String defaultSwitches = getDefaultSwitches();
		if (defaultSwitches != null) {
			defaultSwitchSize = defaultSwitches.length();
		}
		int switchSize = onOrOff.length();
		if (switchSize < defaultSwitchSize) {
			String append = defaultSwitches.substring(switchSize,
					defaultSwitchSize);
			onOrOff = onOrOff + append;
		}
		Boolean[] switches = new Boolean[defaultSwitchSize];
		for (int i = 0; i < defaultSwitchSize; i++) {
			char state = onOrOff.charAt(i);
			if ('1' == state) {
				switches[i] = true;
			} else {
				switches[i] = false;
			}
		}
		return switches;
	}

	private static String prefix = null;

	private static long id = 0;

	protected static synchronized String nextID() {
		if (null == prefix) {
			prefix = randomString(5) + "-";
		}
		return prefix + Long.toString(id++);
	}

	protected static synchronized String genUUID(String messageId) {
		return nextID() + "-" + messageId;
	}

	protected static String decode(String input) {
		String output = null;
		if (input == null) {
			return output;
		}
		try {
			return URLDecoder.decode(input, "UTF-8");
		} catch (Exception e) {
			output = null;
		}
		return output;
	}

	protected static String genMD5(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		String str = input;
		md.update(str.getBytes());
		byte[] encodedPassword = md.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				sb.append("0");
			}

			sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return sb.toString();
	}

	private static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	private static Random randGen = new Random();

	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
}
