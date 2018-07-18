package com.cm55.fx;

import java.util.function.*;
import java.util.stream.*;

import com.cm55.eventBus.*;
import com.cm55.eventBus.EventType;

import javafx.event.*;
import javafx.scene.control.*;

/**
 * {@link FxMenu}、{@link FxContextMenu}に共通する処理
 * @author ysugimura
 *
 * @param <T>
 */
public class AbstractMenu<T> {

  public static class SelectionEvent<T> {
    public final T node;
    public SelectionEvent(T node) {
      this.node = node;
    }
  }
  
  public enum MenuItemKind {
    LEAF,
    BRANCH,
    SELECTABLE_BRANCH;
  }
  
  /**
   * 任意のツリーノード構造からメニューを作成するためのアダプタ
   * @param <T>　任意のツリーノード型
   */
  public interface Adapter<T> {
    
    /** 指定ノードのラベルを取得する */
    public String getLabel(T object);
    
    /** このノードが子を持つか */
    public MenuItemKind getKind(T object);
    
    /** すべての子のストリームを取得する */
    public Stream<T>children(T object);
  }
  
  protected EventBus eventBus = new EventBus();  
  protected Adapter<T>adapter;
  
  /**
   * メニュークリエータ
   * @author ysugimura
   *
   * @param <T>
   */
  public static class MenuCreator<T> {
    Adapter<T>adapter;
    EventBus eventBus;
    MenuCreator(Adapter<T>adapter, EventBus eventBus) {
      this.adapter = adapter;
      this.eventBus = eventBus;
    }
    Menu createMenu(T node, boolean select) {
      
      String label = adapter.getLabel(node);
      //ystem.out.println("createContextMenu " + label);
      Menu menu = new Menu(label);
      
      if (select) {
        //ystem.out.println("child " + adapter.getLabel(node));
        MenuItem item = new MenuItem(adapter.getLabel(node));                          
        item.addEventHandler(ActionEvent.ACTION , e ->  eventBus.dispatchEvent(new SelectionEvent<T>(node)));
        menu.getItems().add(item);
        menu.getItems().add(new SeparatorMenuItem());
      }
      
      adapter.children(node).forEach(child-> {
        System.out.println("child " + adapter.getLabel(child));
        menu.getItems().add(createItem(child));
      });
      return menu;
    }
    ContextMenu createContextMenu(T node) {

      ContextMenu menu = new ContextMenu();
      adapter.children(node).forEach(child-> {
        menu.getItems().add(createItem(child));
      });
      return menu;
    }
    MenuItem createItem(T node) {
      MenuItemKind kind = adapter.getKind(node);
      switch (kind) {
      case LEAF:
        MenuItem item = new MenuItem(adapter.getLabel(node));                          
        item.addEventHandler(ActionEvent.ACTION , e ->  eventBus.dispatchEvent(new SelectionEvent<T>(node)));
        return item;
      case BRANCH:
        return createMenu(node, false);
      default:
        return createMenu(node, true);
      }      
    }
  }
  
  public Unlistener<SelectionEvent<T>>listenSelection(Consumer<SelectionEvent<T>>listener) {
    return eventBus.listen(new EventType<SelectionEvent<T>>() {}, listener);
  }
  

}
