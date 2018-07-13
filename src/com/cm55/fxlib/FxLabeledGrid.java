package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FxLabeledGrid {

  private GridPane grid;
  private int row;
  private Double labelWidth;
  
  public FxLabeledGrid() {
    grid = new GridPane();
  }

  public FxLabeledGrid setLabelWidth(double width) {
    labelWidth = width;
    return this;
  }
  
  public FxLabeledGrid setSpacing(int spacing) {
    grid.setHgap(spacing);
    grid.setVgap(spacing);
    grid.setPadding(new Insets(spacing, spacing, spacing, spacing));
    return this;
  }
  
  public FxLabeledGrid add(String title, Node node) {
    Label label = new Label(title);
    if (labelWidth != null) 
      label.setPrefWidth(labelWidth);
    grid.add(label,  0, row);
    grid.add(node,  1, row);
    row++;
    return this;
  }

  public Pane getPane() {
    return grid;
  }
}
