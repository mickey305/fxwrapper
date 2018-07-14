package com.cm55.fxlib;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class FxVBox extends FxBox<FxVBox> {

  private VBox vbox;
  
  public FxVBox() {
    vbox = new VBox();
    setup(vbox, vbox.getChildren());
    vbox.setAlignment(Pos.CENTER);
  }

  public FxVBox(FxNode...nodes) {
    this();
    addAll(nodes);
  }
  
  public FxVBox setPrefWidth(double value) {
    vbox.setPrefWidth(value);
    return this;
  }
  
  public DoubleProperty spacingProperty() { return vbox.spacingProperty(); }
}