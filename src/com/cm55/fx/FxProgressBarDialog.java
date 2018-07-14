package com.cm55.fx;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * プログレスバー付ダイアログ。基本的にアプリスレッドとは別スレッドで処理をさせ、その間に表示するモードレスのダイアログ。
 * キャンセルボタンだけがあり、それで閉じるか、あるいはclose()呼び出しで閉じる。
 * @author admin
 */
public class FxProgressBarDialog extends FxModelessCancellableDialog {

  private double total;
  private Runnable cancelCallback;
  private ProgressBar bar;
  
  /**
   * タイトルを指定して作成する。
   * @param title
   */
  @SuppressWarnings("static-access")
  public FxProgressBarDialog(FxNode node, String title, double total) {
    super(node);
    this.total = total;
    dialog.setTitle(title);
    BorderPane borderPane = new BorderPane();
    bar = new ProgressBar();
    bar.setPrefWidth(400);
    borderPane.setCenter(bar);
    borderPane.setMargin(bar,  new Insets(10, 10, 10, 10));
    dialogPane.focusedProperty().addListener(new ChangeListener<Boolean>() {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) return;
        Platform.runLater(()-> {
          node.node().getScene().getWindow().requestFocus();
        });
      }      
    });    
    dialogPane.setContent(borderPane);
  }

  /** キャンセルボタンによってキャンセルされた */
  @Override
  protected void cancelled() {
    if (cancelCallback != null) cancelCallback.run();
  }
  
  /** 表示する。モードレスなのでただちに制御が戻る */
  public void open() {
    dialog.show();
  }

  /** キャンセルコールバックを設定する */
  public void setCancelCallback(Runnable runnable) {
    cancelCallback = runnable;
  }
  
  /** 強制的に閉じる */
  public void close() {
    window.hide();
  }

  /** 進捗数を設定 */
  public void setProgress(double value) {
    bar.setProgress(value / total);
  }
}
