package com.cm55.fxlib.splitPane;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public abstract class OrientationAdapter {
  Pane pane;
  public Double targetAvail;
  Double counterSize;
  IntegerProperty dividerThickness;
  ObservableList<Divider>dividers;
  IntegerProperty spacing;
  OrientationAdapter(Pane pane, ObservableList<Divider> dividers, 
      IntegerProperty dividerThickness, IntegerProperty spacing) {
    this.pane = pane;      
    this.dividers = dividers;
    this.dividerThickness = dividerThickness;
    pane.widthProperty().addListener(n-> recalcPaneSize());
    pane.heightProperty().addListener(n-> recalcPaneSize());
    dividers.addListener((ListChangeListener<Divider>)(d)-> recalcPaneSize());      
    dividerThickness.addListener((ChangeListener<Number>)(ob, o, n)-> recalcPaneSize());
  }
  abstract double prefSize(Node node);
  abstract double minSize(Node node);
  abstract double maxSize(Node node);
  abstract double getPosition(Node node);
  abstract void relocate(Node node, double position);
  abstract void resize(Node node, double size);
  abstract void layoutDivider(Divider divider, double position);
  int getDividerCount() { return dividers.size(); }
  abstract void recalcPaneSize();

  public static class HorizontalAdapter extends OrientationAdapter {
    public HorizontalAdapter(Pane pane, ObservableList<Divider> dividers, 
        IntegerProperty dividerThickness, IntegerProperty spacing) {
      super(pane, dividers, dividerThickness, spacing);
    }
    double prefSize(Node node) { return node.prefWidth(counterSize); }
    double minSize(Node node) { return node.minWidth(counterSize); }
    double maxSize(Node node) { return node.maxWidth(counterSize); }
    double getPosition(Node node) { return node.getLayoutX(); }
    void relocate(Node node, double position) { node.relocate(position, 0); }
    void resize(Node node, double size) { node.resize(size, counterSize); }
    void layoutDivider(Divider divider, double position) {
      /*
      divider.setLayoutX(position);
      divider.setLayoutY(0);
      divider.setWidth(dividerSize.get());
      divider.setHeight(counterSize);
      */
      divider.relocate(position,  0);
      divider.resize(dividerThickness.get(), counterSize);
    }
    void recalcPaneSize() {
      targetAvail = pane.getWidth() - getDividerCount() * dividerThickness.get();
      counterSize = pane.getHeight();
    }
  }
  
  public static class VerticalAdapter extends OrientationAdapter {
    public VerticalAdapter(Pane pane, ObservableList<Divider> dividers, 
        IntegerProperty dividerThickness, IntegerProperty spacing) {
      super(pane, dividers, dividerThickness, spacing);
    }
    double prefSize(Node node) { return node.prefHeight(counterSize); }
    double minSize(Node node) { return node.minHeight(counterSize); }
    double maxSize(Node node) { return node.maxHeight(counterSize); }
    double getPosition(Node node) { return node.getLayoutY(); }
    void relocate(Node node, double position) { 
      node.relocate(0,  position); }
    void resize(Node node, double size) { node.resize(counterSize,  size); }
    void layoutDivider(Divider divider, double position) {
      /*
      divider.setLayoutX(0);
      divider.setLayoutY(position);
      divider.setWidth(counterSize);
      divider.setHeight(dividerSize.get());
      */
      divider.relocate(0,  position);
      divider.resize(counterSize,  dividerThickness.get());
    }
    void recalcPaneSize() {
      targetAvail = pane.getHeight() - getDividerCount() * dividerThickness.get();
      counterSize = pane.getWidth();
    }
  }
}
