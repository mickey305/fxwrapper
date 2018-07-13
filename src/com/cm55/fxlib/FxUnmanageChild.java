package com.cm55.fxlib;

import javafx.scene.*;
import javafx.scene.layout.*;

/**
 * ただ一つの子を持ち、自身に挿入し、自身のサイズを与えるがマネージしない。
 * これにより子からのレイアウト要求を遮断する。
 */
public class FxUnmanageChild extends Region {

  private Node node;  
  public FxUnmanageChild(Node node) {
    this.node = node;
    getChildren().add(node);
    node.setManaged(false);
  }

  @Override
  protected void layoutChildren() {        
    node.relocate(0, 0);
    node.resize(getWidth(), getHeight());
  }
  
  @Override protected double computeMinWidth(double height)  { return node.minWidth(height); }
  @Override protected double computeMaxWidth(double height)  { return node.maxWidth(height); }
  @Override protected double computeMinHeight(double width)  { return node.minHeight(width); }
  @Override protected double computeMaxHeight(double width)  { return node.maxHeight(width); }
  @Override protected double computePrefWidth(double height) { return node.prefWidth(height); }
  @Override protected double computePrefHeight(double width) { return node.prefHeight(width); }
}
