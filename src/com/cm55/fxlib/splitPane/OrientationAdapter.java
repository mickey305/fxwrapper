package com.cm55.fxlib.splitPane;

import com.cm55.fxlib.*;

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
  abstract double prefSize(FxNode node);
  abstract double minSize(FxNode node);
  abstract double maxSize(FxNode node);
  abstract double getPosition(FxNode node);
  abstract void relocate(FxNode node, double position);
  abstract void resize(FxNode node, double size);
  abstract void layoutDivider(Divider divider, double position);
  int getDividerCount() { return dividers.size(); }
  abstract void recalcPaneSize();

  public static class HorizontalAdapter extends OrientationAdapter {
    public HorizontalAdapter(Pane pane, ObservableList<Divider> dividers, 
        IntegerProperty dividerThickness, IntegerProperty spacing) {
      super(pane, dividers, dividerThickness, spacing);
    }
    double prefSize(FxNode node) { return node.node().prefWidth(counterSize); }
    double minSize(FxNode node) { return node.node().minWidth(counterSize); }
    double maxSize(FxNode node) { return node.node().maxWidth(counterSize); }
    double getPosition(FxNode node) { return node.node().getLayoutX(); }
    void relocate(FxNode node, double position) { node.node().relocate(position, 0); }
    void resize(FxNode node, double size) { node.node().resize(size, counterSize); }
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
    double prefSize(FxNode node) { return node.node().prefHeight(counterSize); }
    double minSize(FxNode node) { return node.node().minHeight(counterSize); }
    double maxSize(FxNode node) { return node.node().maxHeight(counterSize); }
    double getPosition(FxNode node) { return node.node().getLayoutY(); }
    void relocate(FxNode node, double position) { 
      node.node().relocate(0,  position); }
    void resize(FxNode node, double size) { node.node().resize(counterSize,  size); }
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
