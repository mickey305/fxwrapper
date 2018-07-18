package com.cm55.fx;

import javafx.scene.control.*;

public class FxMenu<T>  extends AbstractMenu<T> {

  protected Menu menu;
  
  public FxMenu(Adapter<T>adapter) {
    this.adapter = adapter;
  }
  
  public FxMenu(Adapter<T>adapter, T root) {
    this(adapter);
    set(root);
  }
  
  public void set(T root) {
    menu = new MenuCreator<T>(adapter, eventBus).createMenu(root, false);
  }
  
  
  public Menu node() {
    return menu;
  }
}
