package com.cm55.fxlib;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FxRadioButtons implements FocusControl<FxRadioButtons> {

  public static class Hor extends FxRadioButtons {
    public Hor(String...captions) {
      super(true, captions);
    }
  }
  
  public static class Ver extends FxRadioButtons {
    public Ver(String...captions) {
      super(false, captions);
    }
  }
  
  private FxCallback<Integer>callback;
  private RadioButton[]radioButtons;
  private ToggleGroup buttonGroup;
  private FxBox<?> box;
  private DoubleProperty spacingProperty;
  private boolean selecting;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
 
  /** 作成する */
  public FxRadioButtons(boolean horizontal) {
  
    if (horizontal) {
      box = new FxHBox();
    } else {
      box = new FxVBox();
    }
    spacingProperty = box.spacingProperty();
    setSpacing(10);
  }
  
  public FxRadioButtons(boolean horizontal, String ...captions) {
    this(horizontal);
    setCaptions(captions);
  }
  
  public FxRadioButtons setCaptions(String...captions) {
    box.clear();
    buttonGroup = new ToggleGroup();    
    radioButtons = new RadioButton[captions.length];
    for (int i = 0; i < radioButtons.length; i++) {
      RadioButton button = new RadioButton(captions[i]) {
        @Override public void requestFocus() {
          if (focusable) super.requestFocus();
        }
      };
      radioButtons[i] = button;
      button.setToggleGroup(buttonGroup);
      box.add(button);
    }
    
    buttonGroup.selectedToggleProperty().addListener((ov, old_toggle,new_toggle) -> {
      if (selecting) return;
      for (int i = 0; i < radioButtons.length; i++) {
        if (new_toggle == radioButtons[i]) {
          if (callback != null) callback.callback(i);
          return;
        }
      }
    });    
    return this;
  }
  
  /** コールバックを設定する */
  public FxRadioButtons setCallback(FxCallback<Integer>callback) {
    this.callback = callback;
    return this;
  }
  
  public String getCaption(int index) {
    return radioButtons[index].getText();
  }
  
  @Override
  public FxRadioButtons setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  /**
   * パディングを設定する
   */
  public FxRadioButtons setPadding(Insets insets) {
    box.setPadding(insets);
    return this;
  }
  
  /**
   * ボタン間のスペース長さを設定する
   * @param value
   * @return
   */
  public FxRadioButtons setSpacing(double value) {
    spacingProperty.set(value);
    return this;
  }
  
  /** プログラムより選択する。イベントは発生しない */
  public void select(int index) {
    selecting = true;
    try {
      if (index < 0) buttonGroup.selectToggle(null);
      else buttonGroup.selectToggle(radioButtons[index]);
    } finally {
      selecting = false;
    }
  }
  
  public int getSelectionIndex() {
    Toggle toggle = buttonGroup.getSelectedToggle();
    for (int i = 0; i < radioButtons.length; i++) {
      if (toggle == radioButtons[i]) return i;
    }
    return -1;
  }

  /** Paneを取得する */
  public Region getPane() {
    return box.getRegion();
  }
}
