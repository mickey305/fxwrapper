package com.cm55.fx;

import javafx.geometry.*;
import javafx.scene.control.*;

/**
 * コンテキストメニュー
 * 
 * <p>
 * メニューが選択されると、EventBusに{@link SelectionEvent}が発行される。
 * </p>
 * @author ysugimura
 *
 * @param <T>
 */
public class FxContextMenu<T> extends AbstractMenu<T> {

  protected ContextMenu menu;
  
  /** ノード変換アダプタを指定する */
  public FxContextMenu(Adapter<T>adapter) {
    this.adapter = adapter;
  }

  /** ノード変換アダプタとルートを指定する */
  public FxContextMenu(Adapter<T>adapter, T root) {
    this(adapter);
    setRoot(root);
  }
  
  /** ルートを指定する */
  public void setRoot(T root) {
    if (root == null) 
      menu = null;
    else
      menu = new MenuCreator<T>(adapter, eventBus).createContextMenu(root);
  }
  
  /** このコンテキストメニューを指定ノードのside側に表示する。未作成の場合は何もしない */
  public void show(FxNode node, Side side) {
    if (menu == null) return;
    menu.show(node.node(), side, 0,  0);
  }

}
