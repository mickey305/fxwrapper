package com.cm55.fxlib;

import javafx.collections.*;
import javafx.scene.control.*;

public class FxList<E> implements FxNode {

  private ListView<E> listView;
  private FxSingleSelectionModel<E> selectionModel;
  //private RowVisibleEnsurer.List makeVisible;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  
  public FxList() {
    listView = new ListView<E>() {
      @Override
      public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    //makeVisible = new RowVisibleEnsurer.List(listView);
    selectionModel = new FxSingleSelectionModel<E>(listView.getSelectionModel());
  }
  
  public FxList<E> setFocusable(boolean value) {
    focusable = value;
    return this;
  }

  public FxSingleSelectionModel<E> getSelectionModel() {
    return selectionModel;
  }

  public ObservableList<E>getItems() {
    return listView.getItems();
  }
  
  public ListView<E> node() {
    return listView;
  }

  public interface TextGetter<E> {
    public String get(E e);
  }
  
  /** 指定インデックス行を可視にする */
  public void makeVisible(int index) {
    // 必要であればTableViewを参考に行う。
    
//    makeVisible.makeVisible(index);
  }
  
  public FxList<E> setTextGetter(TextGetter<E> textGetter) {
    listView.setCellFactory(param -> new ListCell<E>() {
      @Override
      public void updateItem(E item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) setText(null);
        else       setText(textGetter.get(item));
      }
    });
    return this;
  }
 
}
