package com.google.sps.util;

import java.util.ArrayList;

import com.google.gson.Gson;

public class JsonUtil {

  private static final Gson GSON = new Gson();

  public static String convertToJson(ArrayList<String> content) {
    return GSON.toJson(content);
  }

}
