package com.cm55.fx;

import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public abstract class FxBox<T extends FxBox<T>> implements FxParent {

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
  public T add(FxNode node) {
    children.add(node.node());
    return (T)this;
  }
  
  @SuppressWarnings("unchecked")
  public T addAll(FxNode...nodes) {
    Arrays.stream(nodes).forEach(node->children.add(node.node()));
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
  
  public Region node() {
    return region;
  }
}