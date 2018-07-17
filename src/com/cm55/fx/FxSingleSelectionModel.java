package com.cm55.fx;


import java.util.function.*;

import com.cm55.eventBus.*;

import javafx.scene.control.*;

/**
 * 単一選択モデル
 * @author ysugimura
 *
 * @param <E>
 */
public class FxSingleSelectionModel<E> {

  private MultipleSelectionModel<E> selectionModel;

  private EventBus eventBus = new EventBus();
  
  public static class FxSingleSelection {
    public final int value;
    FxSingleSelection(int value) {
      this.value = value;
    }
  }
  
  /** イベント発行不可フラグ */
  private boolean suppressEvent;
  
  public FxSingleSelectionModel(MultipleSelectionModel<E> selectionModel) {
    this.selectionModel = selectionModel;
    selectionModel.setSelectionMode(SelectionMode.SINGLE);
    selectionModel.selectedIndexProperty().addListener((ov, old, current) -> {
      if (suppressEvent) return;
      int index = selectionModel.getSelectedIndex();
      eventBus.dispatchEvent(new FxSingleSelection(index));
    });    
  }
  
  public int getSelectedIndex() {
    return selectionModel.getSelectedIndex();
  }
  
  /**
   * 指定インデックスを選択する。イベントは発生しない。
   * @param index
   */
  public void select(int index) {
    select(index, false);
  }
  
  public void select(int index, boolean event) {
    if (!event) suppressEvent = true;
    try {
      selectionModel.select(index);
    } finally {
      if (!event) suppressEvent = false;
    }
  }
  
  /**
   * 選択無し状態にする。イベントは発生しない。
   */
  public void clearSelection() {
    suppressEvent = true;
    try {
      selectionModel.clearSelection();
    } finally {
      suppressEvent = false;
    }
  }
  
  public void listenSelection(Consumer<FxSingleSelection>l) {
    eventBus.listen(FxSingleSelection.class,  l);
  }

}
