package nl.yogh.aerius.server.util;

public final class HashUtil {
  private HashUtil() {}

  public static String shorten(final String str) {
    return str == null ? "" : str.substring(0, Math.min(str.length(), 8));
  }
}
