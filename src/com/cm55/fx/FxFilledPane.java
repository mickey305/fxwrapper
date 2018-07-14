package com.cm55.fx;

import javafx.scene.*;
import javafx.scene.layout.*;

public class FxFilledPane implements FxNode {
  
  private BorderPane borderPane;
  
  public FxFilledPane(FxNode node) {
    borderPane = new BorderPane();    
    borderPane.setCenter(node.node());
  }  
  
  public BorderPane node() {
    return borderPane;
  }
}
