package net.smileycorp.atlas.api.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import javax.annotation.Nullable;

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

	public static String getOrdinalSuffix(int value) {
		int units = value%10;
		int tens = (int) Math.floor(value/10)%10;
		String suffix = "th";
		if (tens!=1) {
			if (units == 1) {
				suffix = "st";
			} else if (units == 2) {
				suffix = "nd";
			} else if (units == 3) {
				suffix = "rd";
			}
		}
		return String.valueOf(value) + suffix;
	}

	public static MutableComponent translatableComponent(String key, @Nullable String fallback, Object... values) {
		return MutableComponent.create(new TranslatableContents(key, fallback, new Object[]{values}));
	}
	
}
