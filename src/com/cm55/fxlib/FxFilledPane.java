package com.cm55.fxlib;

import javafx.scene.*;
import javafx.scene.layout.*;

public class FxFilledPane extends BorderPane {
  
  public FxFilledPane(Node node) {
    this.setCenter(node);
  }  
}
