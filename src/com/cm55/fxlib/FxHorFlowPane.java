package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class FxHorFlowPane {

  private FlowPane flowPane;
  
  public FxHorFlowPane() {
    flowPane = new FlowPane();
    flowPane.setOrientation(Orientation.HORIZONTAL);
  }

  public FxHorFlowPane(Node...nodes) {
    this();
    flowPane.getChildren().addAll(nodes);
  }
  
  public FxHorFlowPane setSpacing(int value) {
    flowPane.setPadding(new Insets(value, value, value, value));
    flowPane.setHgap(value);
    flowPane.setVgap(value);    
    return this;
  }
  
  public FlowPane getPane() {
    return flowPane;
  }
}
