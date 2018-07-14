package com.cm55.fx;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.layout.*;

public class FxHBox extends FxBox<FxHBox> {

  private HBox hbox;
  
  public FxHBox() {
    hbox = new HBox();
    setup(hbox, hbox.getChildren());
    hbox.setAlignment(Pos.CENTER);
  }

  public FxHBox(FxNode...nodes) {
    this();
    addAll(nodes);
  }
  
  public DoubleProperty spacingProperty() { return hbox.spacingProperty(); }
}
