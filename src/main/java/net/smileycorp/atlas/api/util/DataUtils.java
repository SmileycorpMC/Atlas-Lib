package net.smileycorp.atlas.api.util;

import java.util.UUID;

public class DataUtils {

	public static boolean isValidUUID(String uuid) {
		try  {
			UUID.fromString(uuid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
