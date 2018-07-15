package com.cm55.fx;

public class FocusControlPolicy {

  private static boolean defaultFocusable = true;
  public static boolean getDefaultFocusable() {
    return defaultFocusable;
  }
  
  public static void setDefaultFocusable(boolean value) {
    defaultFocusable = value;
  }
}
