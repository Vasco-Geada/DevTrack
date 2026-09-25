package devtrack.utils;

import java.util.UUID;

public class MainUtils {
	///Methods

	public static String generateId() {		
		return UUID.randomUUID().toString();
	}
}
