package com.cm55.fx;

import java.util.*;
import java.util.stream.*;

import javafx.geometry.*;
import javafx.scene.layout.*;

public class FxFlowPane implements FxNode {

  private FlowPane flowPane;
  
  public FxFlowPane() {
    flowPane = new FlowPane();
    flowPane.setOrientation(Orientation.HORIZONTAL);
  }

  public FxFlowPane(FxNode...nodes) {
    this();
    flowPane.getChildren().addAll(
      Arrays.stream(nodes).map(n->n.node()).collect(Collectors.toList())
    );
  }
  
  public FxFlowPane setPaddingSpacing(int value) {
    flowPane.setPadding(new Insets(value, value, value, value));
    flowPane.setHgap(value);
    flowPane.setVgap(value);    
    return this;
  }
  
  public FlowPane node() {
    return flowPane;
  }
}
