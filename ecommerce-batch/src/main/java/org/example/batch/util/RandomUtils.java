package org.example.batch.util;

import java.time.Instant;
import java.util.UUID;

public class RandomUtils {

  public static String generateRandomId() {
    // 현재 시간을 밀리초(long)로 가져옴
    return Instant.now().toEpochMilli() + "_" + UUID.randomUUID();
  }
}
