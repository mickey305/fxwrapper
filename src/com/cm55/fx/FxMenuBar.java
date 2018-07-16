package com.cm55.fx;

import java.util.*;

import javafx.collections.*;
import javafx.scene.control.*;

/** 
 * メニューバー
 * @author ysugimura
 */
public class FxMenuBar implements FxNode {
  
  MenuBar menuBar;
  ObservableList<Menu> menuList;
  
  public FxMenuBar(FxMenu<?>...menus) {
    menuBar = new MenuBar();
    menuList = menuBar.getMenus();
    Arrays.stream(menus).forEach(menu->add(menu));
  }

  /** メニューを追加する */
  public void add(FxMenu<?> menu) {
    menuList.add(menu.node());
  }
  
  public MenuBar node() {
    return menuBar;
  }
}
