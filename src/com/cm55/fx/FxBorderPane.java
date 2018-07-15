package com.cm55.fx;

import java.util.*;
import java.util.stream.*;

import javafx.geometry.*;
import javafx.scene.layout.*;

public abstract class FxBorderPane<T extends FxBorderPane<T>> implements FxParent {

  public static enum Position {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    CENTER;
    public static final int COUNT = Position.values().length;
  }
  
  protected BorderPane pane;
  protected FxNode[]nodes = new FxNode[Position.COUNT];
  
  protected FxBorderPane() {
    pane = new BorderPane();
  }

  /** 指定位置に{@link FxNode}を設定する */
  protected T set(Position position, FxNode node) {
    return set(position, node == null? null: node.node());
  }

  /** 指定位置に{@link Node}を設定する */
  @SuppressWarnings("unchecked")
  private T set(Position position, javafx.scene.Node node) {
    switch (position) {
    case TOP: pane.setTop(node); break;
    case BOTTOM: pane.setBottom(node); break;
    case LEFT: pane.setLeft(node); break;
    case RIGHT: pane.setRight(node); break;
    default: pane.setCenter(node); break;
    }
    return (T)this;
  }
  
  public T setMargin(Position position, int value) {    
    return setMargin(position,  new Insets(value, value, value, value));    
  }
  
  @SuppressWarnings("unchecked")
  public T setMargin(Position position, Insets insets) {
    FxNode node = nodes[position.ordinal()];
    if (node != null) BorderPane.setMargin(node.node(), insets);
    return (T)this;
  }

  /*
  public FxBorderPane setSpacing(int value) {
    pane.setPadding(new Insets(value, value, value, value));
    List<FxNode>list = Arrays.stream(nodes).filter(n->n != null).collect(Collectors.toList());
    for (int i = 0; i < list.size() - 1; i++)
      BorderPane.setMargin(list.get(i).node(), new Insets(0, value, 0, 0));   
    return this;    
  }
  */

  protected Stream<FxNode>stream(Position...positions) {
    return Arrays.stream(positions).map(p->nodes[p.ordinal()]).filter(n->n != null);
  }

  @SuppressWarnings("unchecked")
  public T setPadding(int value) {
    pane.setPadding(new Insets(value, value, value, value));
    return (T)this;
  }
  
  public abstract T setSpacing(int value);
  
  @SuppressWarnings("unchecked")
  public T setPaddingSpacing(int value) {
    setPadding(value);
    setSpacing(value);
    return (T)this;
  }
  
  public BorderPane node() {
    return pane;
  }
  
  public static class Hor extends FxBorderPane<Hor> {
    public Hor() {}
    public Hor(FxNode left, FxNode center, FxNode right) {
      set(Position.LEFT, left);
      set(Position.CENTER, center);
      set(Position.RIGHT, right);
    }
    public Hor setSpacing(int value) {
      List<FxNode>list = stream(Position.LEFT, Position.CENTER, Position.RIGHT).collect(Collectors.toList());
      for (int i = 0; i < list.size() - 1; i++)
        BorderPane.setMargin(list.get(i).node(), new Insets(0, value, 0, 0));   
      return this;                
    }
  }
  
  public static class Ver extends FxBorderPane<Ver> {
    public Ver() {}
    public Ver(FxNode top, FxNode center, FxNode bottom) {
      set(Position.TOP, top);
      set(Position.CENTER, center);
      set(Position.BOTTOM, bottom);
    }
    public Ver setSpacing(int value) {
      List<FxNode>list = stream(Position.TOP, Position.CENTER, Position.BOTTOM).collect(Collectors.toList());
      for (int i = 0; i < list.size() - 1; i++)
        BorderPane.setMargin(list.get(i).node(), new Insets(0, 0, value, 0));   
      return this;                
    }
  }
}
