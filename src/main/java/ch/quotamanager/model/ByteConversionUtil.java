package ch.quotamanager.model;

/**
 * Helper methods to calculate the number of bytes for different units (MB, GB...).
 */
public class ByteConversionUtil {
	/**
	 * Conversion factor 
	 * 
	 * TODO: Decide between 1000 and 1024.
	 *       See also: https://en.wikipedia.org/wiki/Megabyte
	 */
	private static final long FACTOR = 1000;
	
	/**
	 * @return the number of bytes in the passed number of megabytes.
	 */
	public static long MB(long megabytes) {
		return megabytes * FACTOR * FACTOR;
	}

	/**
	 * @return the number of bytes in the passed number of gigabytes.
	 */
	public static long GB(long gigabytes) {
		return gigabytes * FACTOR * FACTOR * FACTOR;
	}
}
