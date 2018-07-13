package com.cm55.fxlib;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class FxHBox extends FxBox<FxHBox> {

  private HBox hbox;
  
  public FxHBox() {
    hbox = new HBox();
    setup(hbox, hbox.getChildren());
    hbox.setAlignment(Pos.CENTER);
  }

  public FxHBox(Node...nodes) {
    this();
    addAll(nodes);
  }
  
  public DoubleProperty spacingProperty() { return hbox.spacingProperty(); }
}
