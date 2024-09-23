package com.ggb.graduationgoodbye.global.util;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomValueGenerator {

  private static final Random random = new Random();

  public static String getRandomString() {
    byte[] array = getRandomByte();
    Charset charset = getCharset();
    return new String(array, charset);
  }

  public static int getRandomInt() {
    return random.nextInt();
  }

  public static long getRandomLong() {
    return random.nextLong();
  }

  public static boolean getRandomBoolean() {
    return random.nextBoolean();
  }

  public static double getRandomDouble() {
    return random.nextDouble();
  }

  public static LocalDateTime getRandomLocalDateTime() {
    return LocalDateTime.now().minusDays(random.nextInt(365));
  }

  private static byte[] getRandomByte() {
    int randomByteLength = random.nextInt(0, 200);
    byte[] array = new byte[randomByteLength];
    random.nextBytes(array);
    return array;
  }

  public static <T> T getRandomEnum(Class<T> clazz) {
    T[] enumConstants = clazz.getEnumConstants();
    return enumConstants[random.nextInt(enumConstants.length)];
  }





  private static Charset getCharset() {
    Set<String> charset = Charset.availableCharsets().keySet();
    List<String> list = new ArrayList<>(charset);
    int randomIndex = random.nextInt(list.size());
    return Charset.forName(list.get(randomIndex));
  }
}
