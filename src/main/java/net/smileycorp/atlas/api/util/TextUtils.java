package net.smileycorp.atlas.api.util;

public class TextUtils {

	public static String toProperCase(String string) {
		char[] chars = string.replace('_', ' ').toCharArray();
		chars[0] = String.valueOf(chars[0]).toUpperCase().charAt(0);
		for (int n = 0; n<chars.length; n++) {
			if (chars[n]==' ') {
				chars[n+1] = String.valueOf(chars[n+1]).toUpperCase().charAt(0);
			}
		}
		return String.valueOf(chars);
	}
	
}
