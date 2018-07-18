package com.cm55.fx;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * モードレスでキャンセルボタンだけがある。キャンセルが押されると閉じるが、ウインドウの☓ボタンでは閉じない。
 * @author admin
 */
public class FxModelessCancellableDialog {

  protected final FxDialog<Object> dialog;
  protected final Window window;
  protected final DialogPane dialogPane;
  
  public FxModelessCancellableDialog(FxNode node) {
    dialog = new FxDialog<Object>();
    dialog.initModality(Modality.NONE);  
    dialog.initOwner(node.node().getScene().getWindow());
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
