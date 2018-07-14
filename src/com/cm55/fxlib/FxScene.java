package com.cm55.fxlib;

import javafx.scene.*;

public class FxScene {
  public final Scene scene;
  public FxScene(FxParent parent) {
    this.scene = new Scene(parent.node());
  }
}