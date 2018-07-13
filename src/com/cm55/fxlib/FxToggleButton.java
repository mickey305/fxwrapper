package com.cm55.fxlib;

import java.util.function.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;

public class FxToggleButton implements FocusControl<FxToggleButton>, FxNode {

  private ToggleButton button;
  private Consumer<Boolean>callback;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  private SimpleBooleanProperty bindedProperty;
  
  /** プログラムから選択した場合はイベントを発行しない */
  private boolean programaticalSelection;
  
  public FxToggleButton() {
    
    button = new ToggleButton() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    
    // ユーザ操作によって値が変更されたとき、バインドプロパティがあればそれを操作、ユーザ用のイベントを発行する
    button.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        // プログラムから操作された場合
        if (programaticalSelection) return;
        boolean value = button.isSelected();
        if (bindedProperty != null) bindedProperty.set(value);
        if (callback != null) callback.accept(value);
      }      
    });
  }
  
  public FxToggleButton setVisible(boolean value) {
    this.button.setVisible(value);
    return this;
  }
  
  public FxToggleButton(String text) {
    this();
    setText(text);
  }

  public FxToggleButton(String text, Consumer<Boolean>callback) {
    this(text);
    setCallback(callback);
  }

  /** プログラムから選択状態を操作する */
  public FxToggleButton setSelected(boolean value) {
    if (button.isSelected() == value) return this;
    programaticalSelection = true;
    try {
      button.setSelected(value);
    } finally {
      programaticalSelection = false;
    }
    return this;
  }

  /** 選択状態か */
  public boolean isSelected() {
    return button.isSelected();
  }

  /** バインドされたBooleanPropertyのリスナ */
  private ChangeListener<Boolean>bindedPropertyListener =
    new ChangeListener<Boolean>() {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        //ystem.out.println("" + isSelected() + "," + newValue);
        if (isSelected() == newValue) return;
        setSelected(newValue);
      }      
    };

  /** BooleanPropertyにバインドする。このとき、当該プロパティの値に設定されるが、イベントは発行しない */
  public FxToggleButton bind(SimpleBooleanProperty property) {
    if (bindedProperty != null) {
      bindedProperty.removeListener(bindedPropertyListener);
    }
    bindedProperty = property;
    if (bindedProperty == null) return this;
    bindedProperty.addListener(bindedPropertyListener);
    if (bindedProperty.get() != button.isSelected()) {
      button.setSelected(bindedProperty.get());
    }    
    return this;    
  }
  
  /** 有効状態を設定する */
  public FxToggleButton setEnabled(boolean value) {
    button.setDisable(!value);
    return this;
  }

  /** フォーカス可能状態を設定する */
  @Override
  public FxToggleButton setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  /** テキストを設定 */
  public FxToggleButton setText(String text) {
    button.setText(text);
    return this;
  }
  
  public FxToggleButton setCallback(Consumer<Boolean>callback) {
    this.callback = callback;
    return this;
  }
    
  public ToggleButton node() {
    return button;
  }
}
