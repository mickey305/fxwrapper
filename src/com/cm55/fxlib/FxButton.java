package com.cm55.fxlib;

import java.util.function.*;

import javafx.scene.control.*;
import javafx.scene.paint.*;

public class FxButton implements FocusControl<FxButton> {
  
  private Button button;
  private Consumer<FxButton> action;
  private boolean focussable = FocusControlPolicy.getDefaultFocusable();
  
  public FxButton(String label) {
    button = new Button(label) {
      @Override
      public void requestFocus() {
        if (focussable) super.requestFocus();
      }
    };
    button.setOnAction((value) -> {
      if (action != null) action.accept(FxButton.this);
    });
  }
  
  public FxButton(String label, Consumer<FxButton>action) {
    this(label);
    setAction(action);
  }

  public FxButton setPrefWidth(double width) {
    button.setPrefWidth(width);
    return this;
  }
  
  @Override
  public FxButton setFocusable(boolean value) {   
    focussable = value;
    return this;
  }
  
  public FxButton setTextColor(Color color) {
    button.setTextFill(color);
    return this;
  }
  
  public FxButton setText(String text) {
    button.setText(text);
    return this;
  }

  public FxButton setAction(Consumer<FxButton>action) {
    this.action = action;
    return this;
  }
  
  public FxButton setEnabled(boolean value) {
    button.setDisable(!value);
    return this;
  }
  
  public FxButton setVisible(boolean value) {
    button.setVisible(value);
    return this;
  }
  
  public FxButton setMaxWidth(double value) {
    button.setMaxWidth(value);
    return this;
  }
  public Button getControl() {
    return button;
  }
  
  private Styles styles = new Styles() {
    @Override
    protected void changed(String value) {
      button.setStyle(value);
    }
  };
  
  public FxButton setBackgroundColor(Color c) {
    String s = c.toString();
    styles.put("-fx-base",  "#" + s.substring(2));
    return this;
  }
  
  public FxButton removeBackgroundColor() {
    styles.remove("-fx-base");
    return this;
  }
}
