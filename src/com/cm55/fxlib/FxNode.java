package com.cm55.fxlib;

import javafx.scene.*;

public interface FxNode {
  public Node node();
  
  public static class Wrapper implements FxNode {
    private final Node node;
    public Wrapper(Node node) {
      this.node = node;
    }
    public Node node() {
      return node;
    }
  }
}
