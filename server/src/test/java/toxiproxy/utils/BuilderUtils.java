package toxiproxy.utils;

import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

public class BuilderUtils {

  public static String getHostPortString(String prepend) {
    return prepend + UUID.randomUUID().toString().replaceAll("-", "") + ":" +
           String.valueOf(RandomUtils.nextLong(9999, Long.MAX_VALUE)).substring(0, 4);
  }

  public static String getRandPrependString(String prepend) {
    return prepend + UUID.randomUUID().toString();
  }
}
