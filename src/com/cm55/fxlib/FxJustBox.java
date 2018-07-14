package com.cm55.fxlib;

import javafx.scene.*;
import javafx.scene.layout.*;

public class FxJustBox implements FxNode {
  BorderPane borderPane;
  public FxJustBox(FxNode node) {
    borderPane = new BorderPane();
    borderPane.setCenter(node.node());
  }
  
  public BorderPane node() {
    return borderPane;
  }
}
