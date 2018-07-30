package com.cm55.fx;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public abstract class FxCloseDlg<I, R> {

  protected FxDialog<R> dialog;
  protected Window window;
  
  public FxCloseDlg() {
  }
  
  protected void initialize() {
    if (dialog != null) return;
    dialog = new FxDialog<R>();    
    dialog.setTitle(getTitle());
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.setContent(getContent());
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    dialog.setDialogPane(dialogPane);
    final Button btClose = (Button)dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
    btClose.addEventFilter(ActionEvent.ACTION, event -> {
      result = FxCloseDlg.this.getOutput();
      if (result == null)  event.consume();
    });
    window = dialogPane.getScene().getWindow();
    window.setOnShowing(e-> onShowing());
    window.setOnHiding(e->  onHiding());
  }
  
  private R result = null;
  
  public R showAndWait(FxNode node, I input) {
    initialize();
    setInput(input);
    result = null;
    dialog.showAndWait();
    return result;
  }
  
  protected abstract String getTitle();
  
  protected abstract Node getContent();

  protected abstract void setInput(I input);
  
  protected abstract R getOutput();

  protected void onShowing() {}
  protected void onHiding() {}
}
