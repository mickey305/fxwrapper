package com.cm55.fxlib;

import javafx.beans.value.*;
import javafx.scene.control.*;

/**
 * コンボボックス
 * @author admin
 *
 * @param <T>
 */
public class FxComboBox<T> implements FocusControl<FxComboBox<T>> {

  /** フォーカス可能か */
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  
  /** JavaFXのコンボボックス */
  private ComboBox<String> comboBox;
  
  /** 選択モデル */
  private SingleSelectionModel<String> selectionModel;
  
  /** 選択肢 */
  private FxSelections<T> selections;

  /** 選択時のコールバック */
  private FxCallback<T> selectionCallback;
  
  /** 自身が発生したイベントを示すフラグ */
  private boolean selfEvent;

  /** コンボボックスを作成する */
  public FxComboBox() {
    comboBox = new ComboBox<String>() {
      @Override
      public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    selectionModel = comboBox.getSelectionModel();
    selectionModel.selectedIndexProperty().addListener((ChangeListener<Number>) (a, o, n) -> {
      if (selfEvent)
        return;
      if (selectionCallback != null) {
        selectionCallback.callback(getSelection());
      }
      // selectionChange(n);
    });
  }

  /** 選択肢付でコンボボックスを作成する */
  public FxComboBox(FxSelections<T>selections) {
    this();
    setSelections(selections);
  }

  /** 選択肢を設定する */
  public FxComboBox<T>setSelections(FxSelections<T>selections) {
    this.selections = selections;
    comboBox.getItems().clear();
    comboBox.getItems().addAll(selections.labels);
    selectionModel.select(0);
    return this;
  }
  
  /** 選択時コールバックを設定する */
  public FxComboBox<T> setSelectionCallback(FxCallback<T> callback) {
    this.selectionCallback = callback;
    return this;
  }

  /** 現在の選択肢を取得する */
  public T getSelection() {
    int index = selectionModel.getSelectedIndex();
    if (index < 0)
      return null;
    if (selections.noneLabel != null) {
      if (index == 0)
        return null;
      index--;
    }
    return selections.list.get(index);
  }

  /** 選択肢を設定する */
  public void setSelection(T selection) {
    selfEvent = true;
    try {
      if (selection == null) {
        if (selections.noneLabel != null) {
          selectionModel.select(0);
          return;
        }
        throw new NullPointerException();
      }
      Object comparator = selections.toComparator.get(selection);
      for (int i = 0; i < selections.list.size(); i++) {
        if (comparator.equals(selections.toComparator.get(selections.list.get(i)))) {
          if (selections.noneLabel != null)
            selectionModel.select(i + 1);
          else
            selectionModel.select(i);
          return;
        }
      }
      selectionModel.clearSelection();
    } finally {
      selfEvent = false;
    }
  }

  public ComboBox<String> getControl() {
    return comboBox;
  }
  

  /** フォーカス可能かを設定する */
  public FxComboBox<T>setFocusable(boolean value) {
    focusable = value;
    return this;
  }

}
