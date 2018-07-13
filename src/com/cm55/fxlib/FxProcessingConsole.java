package com.cm55.fxlib;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.control.*;

/**
 * Read OnlyのTextAreaを持ち、そこに行を追加していくことができる。キャンセルボタンが押されたら閉じてコールバックする。
 * キャンセルボタン以外でもclose()呼び出しで閉じる。ウインドウのxボタンでは閉じない。
 * このダイアログはリサイズ可能。
 * @author admin
 *
 */
public class FxProcessingConsole extends FxModelessCancellableDialog {
  
  public static final int WIDTH = 640;
  public static final int HEIGHT = 200;
  
  private TextArea textArea;
  private Runnable cancelCallback;
  
  /**
   * タイトルを指定して作成する。
   * @param title
   */
  public FxProcessingConsole(Node node, String title) {
    super(node);
    dialog.setTitle(title);
    dialogPane.setPrefWidth(WIDTH);
    dialogPane.setPrefHeight(HEIGHT);
    dialog.setResizable(true);
    textArea = new TextArea();
    textArea.setEditable(false);
    dialogPane.setContent(textArea);
    dialogPane.focusedProperty().addListener(new ChangeListener<Boolean>() {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) return;
        Platform.runLater(()-> {
          node.getScene().getWindow().requestFocus();
        });
      }      
    });
  }

  /** キャンセルコールバックを設定する */
  public FxProcessingConsole setCancelCallback(Runnable runnable) {
    cancelCallback = runnable;
    return this;
  }
  
  /** キャンセルコールバックを呼び出す */
  @Override
  protected void cancelled() {    
    if (this.cancelCallback != null) cancelCallback.run();
  }

  /** ダイアログを取得する */
  public Dialog<?>getDialog() {
    return dialog;
  }
  
  /** 表示する。閉じられるまで制御は戻らない */
  public void open() {
    dialog.show();
  }
  
  /** キャンセルボタン以外で強制的に閉じる */
  public void close() {
    window.hide();
  }

  /** 表示行を追加する */
  public void addLine(String line) {
    textArea.appendText(line + "\n");
  }
}
