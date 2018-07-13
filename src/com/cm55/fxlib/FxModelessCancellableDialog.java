package com.cm55.fxlib;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * モードレスでキャンセルボタンだけがある。キャンセルが押されると閉じるが、ウインドウの☓ボタンでは閉じない。
 * @author admin
 */
public class FxModelessCancellableDialog {

  protected final Dialog<Object> dialog;
  protected final Window window;
  protected final DialogPane dialogPane;
  
  public FxModelessCancellableDialog(Node node) {
    dialog = new Dialog<Object>();
    dialog.initModality(Modality.NONE);  
    dialog.initOwner(node.getScene().getWindow());
    dialogPane = dialog.getDialogPane();    
    dialogPane.getButtonTypes().addAll(ButtonType.CANCEL);
    window = dialogPane.getScene().getWindow();  
    window.setOnCloseRequest(event -> windowCloseRequest(event));
    ((Button)dialogPane.lookupButton(ButtonType.CANCEL)).addEventFilter(ActionEvent.ACTION, e -> {
      if (!canCancel()) {
        e.consume(); 
        return;
      }
      cancelled();
    });
  }
  
  /** ウインドウのXボタンが押されたとき、無視する */
  protected void windowCloseRequest(WindowEvent e) {
    e.consume();
  }

  /** キャンセルボタンが押されたとき。キャンセル可能か ? */
  protected boolean canCancel() {
    return true;
  }

  /** キャンセルボタンで閉じられた */
  protected void cancelled() {    
  }
}
