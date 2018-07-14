package com.cm55.fx;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.control.*;

/**
 * Convinient control for just selecting one of Strings.
 * @author ysugimura
 */
public class FxSimpleComboBox {

  /** 
   * hide the functionality of {@link ChoiceBox}. 
   * forcing usage of this class methods only.
   */
  private final ChoiceBox<String>box;
  
  private final SelectionModel<String>selectionModel;

  private final ObservableList<String>list;
  
  /** if the selection is programmatically set, don't fire selection event */
  private boolean noEvent;
  
  public FxSimpleComboBox() {
    box = new ChoiceBox<String>();
    selectionModel = box.getSelectionModel();
    list = box.getItems();
    selectionModel.selectedIndexProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable,
          Number oldValue, Number newValue) {
        if (noEvent) return;
        if (notifier != null) notifier.run();
      }           
    });
  }
  
  /**
   * 表示ラベルを指定する。
   * @param labels 表示ラベル配列
   */
  public FxSimpleComboBox(String...labels) {
    this();
    setLabels(labels);
  }
  
  public FxSimpleComboBox(final String[]labels, Runnable notifuder) {
    this(labels);
    setNotifier(notifier);
  }

  /** 有効・無効を設定する */
  public void setEnabled(boolean value) {
    box.setDisable(!value);
  }
  
  /**
   * ラベル配列を設定する
   * @param labels
   */
  public void setLabels(final String[]labels) {
    list.clear();
    list.addAll(labels);
  }
  
  /** ウィジェットを取得する */
  public Control asControl() {
    return box;
  }
  
  /** 
   * set selection index.
   * event in the disabled state, you can change the index programmatically
   */
  public void setSelection(int index) {
    if (getSelection() == index) return;
    // no event when programmatically set
    noEvent = true;
    try {
      selectionModel.select(index);
    } finally {
      noEvent = false;
    }
  }
  
  /** 選択インデックスを取得する */
  public int getSelection() {
    return selectionModel.getSelectedIndex();
  }
  
  /** 選択notifier */
  private Runnable notifier;

  /** notifierをセットする */
  public void setNotifier(Runnable notifier) {
    this.notifier = notifier;
  }
}

