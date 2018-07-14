package com.cm55.fx;

import java.util.function.*;

//Java9コンパイラはエラーを出す。実行時エラーは無いが、Java9で仕様変更されており、機能しない。
import com.sun.javafx.scene.control.behavior.*;
import com.sun.javafx.scene.control.skin.*;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;

public class FxTextArea implements FocusControl<FxTextArea>, FxNode {

  private TextArea textArea;
  private Consumer<String>textChangedCallback;
  private Consumer<String>copiedCallback;
  private Consumer<FxTextArea>mouseReleasedCallback;
  private Consumer<FxTextArea>mousePressedCallback;
  private Consumer<KeyEvent>keyPressedCallback;
  private boolean textSetting = false;
  private boolean focusable;
  
  public FxTextArea setCopiedCallback(Consumer<String>copiedCallback) {
    this.copiedCallback = copiedCallback;
    return this;
  }
  
  public FxTextArea setMouseReleasedCallback(Consumer<FxTextArea>callback) {
    this.mouseReleasedCallback = callback;
    return this;
  }
  
  public FxTextArea setMousePressedCallback(Consumer<FxTextArea>callback) {
    this.mousePressedCallback = callback;
    return this;
  }
  
  public FxTextArea() {
    textArea = new TextArea() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
      @Override
      public void paste() {
        /*
          Clipboard clipboard = Clipboard.getSystemClipboard();
          if (clipboard.hasString()) {
              replaceSelection(clipboard.getString().toUpperCase());
          }
        */
        super.paste();
      }
      @Override
      public void copy() {
        String selectedText = getSelectedText();
        super.copy();
        if (copiedCallback != null) copiedCallback.accept(selectedText);
      }
    };
        
    textArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
      @SuppressWarnings("restriction")
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() != KeyCode.TAB) return;
        if (!tabTraverse) return;
        try {
          TextAreaSkin skin = (TextAreaSkin)textArea.getSkin();
          BehaviorBase<?> bb = skin.getBehavior();
          if (!(bb instanceof TextAreaBehavior)) return;
          TextAreaBehavior behavior = (TextAreaBehavior)bb;
          if (event.isControlDown()) {
            behavior.callAction("InsertTab");
          } else if (event.isShiftDown()) {
            behavior.callAction("TraversePrevious");
          } else {
            behavior.callAction("TraverseNext");
          }
          event.consume();     
        } catch (NoClassDefFoundError ex) {
          // Java9でJavaFXの仕様変更。代替策なし
          //ystem.out.println("" + ex.getMessage());
        }   
      }
    });
    
    textArea.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (mousePressedCallback != null) mousePressedCallback.accept(FxTextArea.this);
      }
    });
    textArea.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (mouseReleasedCallback != null) mouseReleasedCallback.accept(FxTextArea.this);
      }
    });
    
    textArea.setEditable(true);
    textArea.setWrapText(true);
    textArea.textProperty().addListener(new ChangeListener<String>() {
      public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
          final String newValue) {
        textChanged(newValue);
      }
    });  
    textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        if (keyPressedCallback != null) keyPressedCallback.accept(event);
      }      
    });
  }

  private boolean tabTraverse;
  
  public int getCaretPosition() {
    return textArea.getCaretPosition();
  }
  
  public String getSelectedText() {
    return textArea.getSelectedText();
  }
  
  public FxTextArea setTabTraverse(boolean value) {
    this.tabTraverse = value;
    return this;
  }
  
  public void setTabAction(String tabAction) {
    
  }
  
  public FxTextArea setEditable(boolean value) {
    textArea.setEditable(value);
    return this;
  }
  
  @Override
  public FxTextArea setFocusable(boolean value) {
    this.focusable = value;
    return this;
  }
  
  public FxTextArea positionCaret(int position) {
    Platform.runLater(() -> {
      textArea.positionCaret(position);
    });
    return this;
  }
  
  public FxTextArea(Consumer<String>callback) {
    this();
    setTextChangedCallback(callback);
  }
  
  public FxTextArea setFont(Font font) {
    textArea.setFont(font);
    return this;
  }
  
  public FxTextArea setWrapText(boolean value) {
    textArea.setWrapText(value);
    return this;
  }
  
  public FxTextArea setKeyPressedCallback(Consumer<KeyEvent>callback) {
    this.keyPressedCallback = callback;
    return this;
  }
  
  public FxTextArea setTextChangedCallback(Consumer<String>callback) {
    this.textChangedCallback = callback;
    return this;
  }
  
  public FxTextArea setText(String text) {
    textSetting = true;
    try {
      textArea.setText(text);
    } finally {
      textSetting = false;
    }
    return this;
  }
  
  public FxTextArea clear() {
    return setText("");
  }
  
  public String getText() {
    return textArea.getText();
  }

  public FxTextArea setPrefHeight(double height) {
    textArea.setPrefHeight(height);
    return this;
  }

  public FxTextArea setMaxHeight(double height) {
    textArea.setMaxHeight(height);
    return this;
  }
  
  private void textChanged(String text) {
    if (textSetting) return;
    if (textChangedCallback != null) textChangedCallback.accept(text);
  }
  
  public TextArea node() {
    return textArea;
  }

}
