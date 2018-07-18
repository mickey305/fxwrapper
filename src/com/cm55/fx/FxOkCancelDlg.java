package com.cm55.fx;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * タスクバーアイコンを避ける方法が無い。
 * https://stackoverflow.com/questions/24564136/javafx-can-you-create-a-stage-that-doesnt-show-on-the-task-bar-and-is-undecora
 * @author ysugimura
 *
 * @param <I>
 * @param <R>
 */
public abstract class FxOkCancelDlg<I, R> {

  protected FxDialog<R> dialog;
  protected Window window;
  
  public FxOkCancelDlg() {
  }
  
  protected boolean initialize(FxNode node) {
    if (dialog != null) return false;
    dialog = new FxDialog<R>();    
    dialog.setTitle(getTitle());
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.setContent(getContent());
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    dialog.setDialogPane(dialogPane);
    final Button btOk = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
    btOk.addEventFilter(ActionEvent.ACTION, event -> {
      result = FxOkCancelDlg.this.getOutput();
      if (result == null)  event.consume();
    });
    window = dialogPane.getScene().getWindow();
    window.setOnShowing(e-> onShowing());
    window.setOnHiding(e->  onHiding());
    return true;
  }
  
  private R result = null;
  
  protected R showAndWait(FxNode node, I input) {
    initialize(node);
    setInput(input);
    result = null;
    dialog.showAndWait();
    return result;
  }

  protected void onShowing() {}
  protected void onHiding() {}
  
  protected void setResult(R value) {
    dialog.setResult(value);
    result = value;
  }
  
  protected abstract String getTitle();
  
  protected abstract Node getContent();

  protected abstract void setInput(I input);
  
  protected abstract R getOutput();

}
