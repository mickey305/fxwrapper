package com.cm55.fxlib.splitPane;

import java.util.*;

import javafx.beans.property.*;
import javafx.scene.layout.*;

public class SplitPaneView extends Pane {
  
  private ViewMouseHandler mouseHandler;
  private DragLayouter dragLayouter;
  private WholeLayouter wholeLayouter;
  private boolean horizontal;
  List<Part>parts;
  List<Divider>dividers;
  private IntegerProperty dividerThickness;

  public void setWholeLayouter(WholeLayouter wholeLayouter) {
    this.wholeLayouter = wholeLayouter;
  }
  
  public void setMouseHandler(ViewMouseHandler mouseHandler) {
    this.mouseHandler = mouseHandler;
  }
  
  public void setDragLayouter(DragLayouter dragLayouter) {
    this.dragLayouter = dragLayouter;
  }
  
  public void setDragMove(double move) {
    dragLayouter.setMove(move);
    requestLayout();
  }
  
  public SplitPaneView(boolean horizontal, List<Part>parts, List<Divider>dividers, IntegerProperty dividerThickness) {
    this.horizontal = horizontal;
    this.parts = parts;
    this.dividers = dividers;
    this.dividerThickness = dividerThickness;
    setOnMousePressed(me -> {
      if (mouseHandler != null)
        mouseHandler.pressed(me);
    });
    setOnMouseDragged(me -> {
      if (mouseHandler != null)
        mouseHandler.dragged(me);
    });
    setOnMouseReleased(me -> {
      if (mouseHandler != null)
        mouseHandler.released(me);
    });
  }
  
  @Override
  protected void layoutChildren() {        
    if (dragLayouter != null) {
      dragLayouter.layout();
    } else {
      wholeLayouter.layout();
    }
  }
  @Override
  protected double computeMinWidth(double height) {
    if (horizontal) 
      return parts.stream().mapToDouble(part->part.node.minWidth(height)).sum() + dividers.size() * dividerThickness.get();
    else
      return parts.stream().mapToDouble(part->part.node.minWidth(height)).max().orElse(0);
  }
  @Override
  protected double computeMaxWidth(double height) {
    if (horizontal)
      return parts.stream().mapToDouble(part->part.node.maxWidth(height)).sum() +  + dividers.size() * dividerThickness.get();
    else 
      return parts.stream().mapToDouble(part->part.node.maxWidth(height)).max().orElse(0);
  }
  @Override
  protected double computeMinHeight(double width) {
    if (horizontal)
      return parts.stream().mapToDouble(part->part.node.minHeight(width)).max().orElse(0);
    else 
      return parts.stream().mapToDouble(part->part.node.minHeight(width)).sum() + dividers.size() * dividerThickness.get();
  }
  @Override
  protected double computeMaxHeight(double width) {
    if (horizontal)
      return parts.stream().mapToDouble(part->part.node.maxHeight(width)).max().orElse(0);
    else
      return parts.stream().mapToDouble(part->part.node.maxHeight(width)).max().orElse(0);
  }
  @Override
  protected double computePrefWidth(double height) {
    if (horizontal)
      return parts.stream().mapToDouble(part->part.node.prefWidth(height)).sum() + dividers.size() * dividerThickness.get();
    else
      return parts.stream().mapToDouble(part->part.node.prefWidth(height)).max().orElse(0);
  }
  @Override
  protected double computePrefHeight(double width) {
    if (horizontal)
      return parts.stream().mapToDouble(part->part.node.prefHeight(width)).max().orElse(0);
    else
      return parts.stream().mapToDouble(part->part.node.prefHeight(width)).sum() + dividers.size() * dividerThickness.get();
  }      
}
