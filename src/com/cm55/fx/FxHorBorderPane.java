package com.cm55.fx;

import java.util.*;
import java.util.stream.*;

import javafx.geometry.*;
import javafx.scene.layout.*;

public class FxHorBorderPane implements FxNode {

  private BorderPane pane;
  private FxNode[]nodes;
  
  public FxHorBorderPane(FxNode left, FxNode center, FxNode right) {
    pane = new BorderPane();
    nodes = new FxNode[] { left, center, right };
    if (left != null) pane.setLeft(left.node());
    if (center != null) pane.setCenter(center.node());
    if (right != null) pane.setRight(right.node());
  }
  
  public FxHorBorderPane setLeftMargin(int value) {
    setMargin(0, value);
    return this;
  }
  
  public FxHorBorderPane setCenterMargin(int value) {
    setMargin(1, value);
    return this;
  }
  
  public FxHorBorderPane setRightMargin(int value) {
    setMargin(2, value);
    return this;
  }

  public FxHorBorderPane setRightMargin(Insets insets) {
    setMargin(2, insets);
    return this;
  }
  
  private void setMargin(int index, int value) {
    setMargin(index,  new Insets(value, value, value, value));
  }
  
  private void setMargin(int index, Insets insets) {
    BorderPane.setMargin(nodes[index].node(), insets);
  }
  
  public FxHorBorderPane setSpacing(int value) {
    pane.setPadding(new Insets(value, value, value, value));
    List<FxNode>list = Arrays.stream(nodes).filter(n->n != null).collect(Collectors.toList());
    for (int i = 0; i < list.size() - 1; i++)
      BorderPane.setMargin(list.get(i).node(), new Insets(0, value, 0, 0));   
    return this;
    
  }
  
  public BorderPane getPane() {
    return pane;
  }
  
  public BorderPane node() {
    return pane;
  }
}
