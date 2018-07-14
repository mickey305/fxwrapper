package com.cm55.fx;

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
  
  public static Wrapper wrap(Node node) {
    return new Wrapper(node);
  }
}
