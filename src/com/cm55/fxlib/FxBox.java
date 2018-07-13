package com.cm55.fxlib;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public abstract class FxBox<T extends FxBox<T>> {

  private Region region;
  private ObservableList<Node>children;
  
  protected void setup(Region region, ObservableList<Node>children) {
    this.region = region;
    this.children = children;
  }
  
  @SuppressWarnings("unchecked")
  public T setSpacing(double value) {
    spacingProperty().set(value);
    region.setPadding(new Insets(value, value, value, value));
    return (T)this;
  }
  
  @SuppressWarnings("unchecked")
  public T add(Node node) {
    children.add(node);
    return (T)this;
  }
  
  @SuppressWarnings("unchecked")
  public T addAll(Node...nodes) {
    children.addAll(nodes);
    return (T)this;
  }
  
  @SuppressWarnings("unchecked")
  public T clear() {
    children.clear();
    return (T)this;
  }

  @SuppressWarnings("unchecked")
  public T setPadding(Insets insets) {
    region.setPadding(insets);
    return (T)this;
  }
  
  public abstract DoubleProperty spacingProperty();
  
  public Region getRegion() {
    return region;
  }
}