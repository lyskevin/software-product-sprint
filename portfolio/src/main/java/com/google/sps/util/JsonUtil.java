package com.google.sps.util;

import com.google.gson.Gson;
import java.util.ArrayList;

public class JsonUtil {
  private static Gson gson = new Gson();

  public static String convertToJson(ArrayList<String> content) {
    return gson.toJson(content);
  }
}
