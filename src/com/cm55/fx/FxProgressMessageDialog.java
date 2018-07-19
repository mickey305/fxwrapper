package com.cm55.fx;

import java.util.concurrent.*;
import java.util.function.*;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.*;

/**
 * 進行中のモードレスダイアログを表示する。
 * @author admin
 */
public class FxProgressMessageDialog {
  
  private Alert dialog;
  private boolean programaticallyClosed;
  
  public FxProgressMessageDialog(FxNode node, String message) {
    this(node, message, null);
  }
  
  public FxProgressMessageDialog(FxNode node, String message, Consumer<ButtonType> cancelCallback) {
    dialog = new Alert(AlertType.INFORMATION, message, ButtonType.CANCEL);
    dialog.setTitle("Waiting");
    dialog.setHeaderText("Please wait for a while");
    dialog.initOwner(node.node().getScene().getWindow());
    dialog.initModality(Modality.APPLICATION_MODAL);
    if (cancelCallback != null) handleCancel(cancelCallback);
  }
  
  public FxProgressMessageDialog setTitle(String title) {
    dialog.setTitle(title);
    return this;
  }
  
  public FxProgressMessageDialog setHeaderText(String value) {
    dialog.setHeaderText(value);
   return this; 
  }
  
  
  public FxProgressMessageDialog handleCancel(Consumer<ButtonType> cancelCallback) {
    dialog.resultProperty().addListener(new ChangeListener<ButtonType>() {
      public void changed(ObservableValue<? extends ButtonType> observable, ButtonType oldValue, ButtonType newValue) {        
        // 明示的にクローズされた場合にもこのコールバックは呼び出されてしまう。キャンセルボタン押下以外は処理しない。
        if (programaticallyClosed) return;
        
        // 自動でclose
        cancelCallback.accept(newValue);
      }      
    });    
    return this;
  }
  
  public FxProgressMessageDialog show() {
    dialog.show();
    return this;
  }

  public <T>void show(Callable<T>callable, Consumer<T>returns, Consumer<Exception>exception) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(()-> {
      try {
        T r;
        try {
          r = callable.call();
        } finally {
          Platform.runLater(()->close());
        }
        Platform.runLater(()->returns.accept(r));      
      } catch (Exception ex) {
        Platform.runLater(()->exception.accept(ex));
      }      
    });
    service.shutdown();
    dialog.show();
  }
  
  /** キャンセルボタン以外で明示的に閉じる場合に使用する */
  public FxProgressMessageDialog close() {
    this.programaticallyClosed = true;
    dialog.close();
    return this;
  }
}
