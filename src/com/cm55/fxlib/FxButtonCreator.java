package com.cm55.fxlib;

import com.google.inject.*;

import javafx.event.*;
import javafx.scene.control.*;

/**
 * Convenient method for creating button, setting label and action listener
 * @author ysugimura
 */
@Singleton
public class FxButtonCreator {
  
  public Button create(String label) {
    return create(label, null);
  }
  
  public Button create(String label, Runnable actionHandler) {
    Button button = new Button(label);
    if (actionHandler != null) {
      button.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
          actionHandler.run();
        }            
      });
    }
    return button;
  }
  
}
