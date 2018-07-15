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
  
  public static class Hor extends FxBox<Hor> {

    private HBox hbox;
    
    public Hor() {
      hbox = new HBox();
      setup(hbox, hbox.getChildren());
      hbox.setAlignment(Pos.CENTER);
    }

    public Hor(FxNode...nodes) {
      this();
      addAll(nodes);
    }
    
    public DoubleProperty spacingProperty() { return hbox.spacingProperty(); }
  }

  public static class Ver extends FxBox<Ver> {

    private VBox vbox;
    
    public Ver() {
      vbox = new VBox();
      setup(vbox, vbox.getChildren());
      vbox.setAlignment(Pos.CENTER);
    }

    public Ver(FxNode...nodes) {
      this();
      addAll(nodes);
    }
    
    public Ver setPrefWidth(double value) {
      vbox.setPrefWidth(value);
      return this;
    }
    
    public DoubleProperty spacingProperty() { return vbox.spacingProperty(); }
  }
}