package com.cm55.fx;

import com.cm55.fx.util.*;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class FxLineDialog extends FxOkCancelDlg<String, String> {

  private Label messageLabel;
  private TextField valueField;

  @Override
  protected String getTitle() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Node getContent() {    
    messageLabel = new Label("");
    valueField = new TextField("");
    valueField.setPrefWidth(200);
    valueField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      public void handle(KeyEvent event) {
        switch (event.getCode()) {
        case ENTER: setResult(getOutput()); break;
        case ESCAPE: setResult(null); break;
        default: break;
        }
      }      
    });
    
    VBox box = new VBox(10,
        messageLabel, 
        valueField
    );
    box.setPadding(new Insets(5, 5, 5, 5));
    return box;
  }
  
  private FxNode node;
  
  public String showAndWait(FxNode node, String title, String message, String input) {
    initialize(node);
    this.node = node;
    dialog.setTitle(title);
    messageLabel.setText(message);
    return super.showAndWait(node, input);
  }
  
  @Override
  protected void setInput(String input) {
    new RelocateAroundNode.ForDialog(node, dialog);    
    valueField.setText(input);
    Platform.runLater(()-> {
      valueField.requestFocus();
    });
  }

  @Override
  protected String getOutput() {
    return valueField.getText();
  }

  public FxLineDialog setFieldWidth(int width) {
    valueField.setPrefWidth(width);
    return this;
  }

  
  /*
  public String showAndWait() {
    new RelocateAroundNode.ForStage(parent, stage);
    stage.showAndWait();
    return result;
  }
  */

}