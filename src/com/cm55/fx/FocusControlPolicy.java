package com.cm55.fx;

public class FocusControlPolicy {

  private static boolean defaultFocusable;
  public static boolean getDefaultFocusable() {
    return defaultFocusable;
  }
  
  public static void setDefaultNoFocusable(boolean value) {
    defaultFocusable = value;
  }
}
