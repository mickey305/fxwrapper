package com.cm55.fxlib;

import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class FxTextField implements FocusControl<FxTextField> {

  private TextField textField;
  private FxCallback<String>textChangedCallback;
  private FxCallback<KeyEvent>keyPressedCallback;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  private boolean updating = false;
  
  public FxTextField() {
    textField = new TextField() {
      @Override
      public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    textField.textProperty().addListener(new ChangeListener<String>() {
      public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
          final String newValue) {
        if (updating) return;
        if (textChangedCallback != null) textChangedCallback.callback(newValue);
      }
    });  
    textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        if (keyPressedCallback != null) keyPressedCallback.callback(event);
      }      
    });
  }
  
  public TextField getControl() {
    return textField;
  }
  
  public FxTextField setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  public FxTextField setTextChangedCallback(FxCallback<String>callback) {
    this.textChangedCallback = callback;
    return this;
  }
  
  public FxTextField setKeyPressedCallback(FxCallback<KeyEvent>callback) {
    this.keyPressedCallback = callback;
    return this;
  }
  
  
  public FxTextField setText(String value) {
    updating = true;
    try {
      textField.setText(value);
    } finally {
      updating = false;
    }
    return this;
  }
  
  public String getText() {
    return textField.getText();
  }

}
