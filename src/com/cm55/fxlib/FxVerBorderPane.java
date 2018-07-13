package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class FxVerBorderPane {

  private BorderPane pane;
  private Node[]nodes;
  
  public FxVerBorderPane(Node top, Node center, Node bottom) {
    pane = new BorderPane();
    nodes = new Node[] { top, center, bottom };
    if (top != null) pane.setTop(top);
    if (center != null) pane.setCenter(center);
    if (bottom != null) pane.setBottom(bottom);
  }
  
  public FxVerBorderPane setTopMargin(int value) {
    setMargin(0, value);
    return this;
  }
  
  public FxVerBorderPane setCenterMargin(int value) {
    setMargin(1, value);
    return this;
  }
  
  public FxVerBorderPane setBottomMargin(int value) {
    setMargin(2, value);
    return this;
  }
  
  @SuppressWarnings("static-access")
  private void setMargin(int index, int value) {
    pane.setMargin(nodes[index],  new Insets(value, value, value, value));
  }
  
  
  public BorderPane getPane() {
    return pane;
  }
}
