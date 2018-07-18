package com.cm55.fx;

import javafx.scene.*;

public interface FxParent extends FxNode {
  Parent node();
  
  public static class Wrapper implements FxParent {
    private final Parent node;
    public Wrapper(Parent node) {
      this.node = node;
    }
    public Parent node() {
      return node;
    }
  }
  
  public static FxParent wrap(Parent parent) {
    return new Wrapper(parent);
  }
}
