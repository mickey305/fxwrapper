package com.cm55.fx;

import java.util.*;

public abstract class Styles {

  private Map<String, String>map = new HashMap<String, String>();
  
  public void put(String name, String value) {
    map.put(name, value);
    doChanged();
  }
  
  public void remove(String name) {
    map.remove(name);
    doChanged();
  }
  
  private void doChanged() {
    String s = toString();
    changed(s);
  }
  
  protected abstract void changed(String s);
  
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Map.Entry<String, String>e: map.entrySet()) {
      s.append(e.getKey() + ":" + e.getValue() + ";");
    }
    return s.toString();
  }
}
