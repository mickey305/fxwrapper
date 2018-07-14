package com.cm55.fx;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class FxVerBorderPane implements FxNode {

  private BorderPane pane;
  private FxNode[]nodes;
  
  public FxVerBorderPane(FxNode top, FxNode center, FxNode bottom) {
    pane = new BorderPane();
    nodes = new FxNode[] { top, center, bottom };
    if (top != null) pane.setTop(top.node());
    if (center != null) pane.setCenter(center.node());
    if (bottom != null) pane.setBottom(bottom.node());
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
    pane.setMargin(nodes[index].node(),  new Insets(value, value, value, value));
  }
  
  
  public BorderPane getPane() {
    return pane;
  }
  public BorderPane node() {
    return pane;
  }
}
