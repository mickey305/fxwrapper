package com.cm55.fxlib;

import java.util.function.*;

import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class FxTextField implements FocusControl<FxTextField>, FxNode {

  private TextField textField;
  private Consumer<String>textChangedCallback;
  private Consumer<KeyEvent>keyPressedCallback;
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
        if (textChangedCallback != null) textChangedCallback.accept(newValue);
      }
    });  
    textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        if (keyPressedCallback != null) keyPressedCallback.accept(event);
      }      
    });
  }
  
  public TextField node() {
    return textField;
  }
  
  public FxTextField setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  public FxTextField setTextChangedCallback(Consumer<String>callback) {
    this.textChangedCallback = callback;
    return this;
  }
  
  public FxTextField setKeyPressedCallback(Consumer<KeyEvent>callback) {
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
