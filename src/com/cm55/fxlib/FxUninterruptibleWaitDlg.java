package com.cm55.fxlib;

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.*;

/**
 * 中断できない処理とその表示
 * <p>
 * 中断不可能な処理を別スレッドで動作させ、その間にダイアログを表示する。当然ダイアログは閉じることもできないし、ボタンも無い。
 * 外部から閉じることもできず、閉じるには「中断不可能処理」が終了するしかない。
 * </p>
 * @author admin
 *
 * @param <T>
 */
public class FxUninterruptibleWaitDlg {
  
  public interface UninterruptibleExecuter<T> {
    public T exec();
  }
  
  private Alert dialog;
  
  /**
   * 基本的なセットアップを行う
   * @param node 対象とするWindowを指定するノード
   * @param message メッセージ
   * @param appModal true:アプリケーションモーダル、false:ウインドウモーダル
   */
  public FxUninterruptibleWaitDlg(FxNode node, String message, boolean appModal) {      
    dialog = new Alert(AlertType.INFORMATION, message);
    dialog.getDialogPane().getButtonTypes().clear();
    dialog.initOwner(node.node().getScene().getWindow());
    dialog.setTitle("Waiting");
    dialog.setHeaderText("Please wait for a while");
    if (appModal) dialog.initModality(Modality.APPLICATION_MODAL);
    else dialog.initModality(Modality.WINDOW_MODAL);
  }
  
  public FxUninterruptibleWaitDlg setTitle(String title) {
    dialog.setTitle(title);
    return this;
  }
  
  public FxUninterruptibleWaitDlg setHeaderText(String value) {
    dialog.setHeaderText(value);
   return this; 
  }
  
  /**
   * 中断不可能処理を指定し、ダイアログを表示する。呼び出しスレッドとは別スレッドで処理が実行される。
   * 処理が終了すると自動的にダイアログが閉じられる。
   * @param runner
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T start(UninterruptibleExecuter<T> runner) {
    T[]result = (T[])new Object[1];
    new Thread() {
      public void run() {
        result[0] = (T)runner.exec();
        Platform.runLater(()-> {
          close();
        });          
      }
    }.start();
    showModal();
    return result[0];
  }
  
  public void showModal() {
    dialog.showAndWait();    
  }
  
  public void close() {
    dialog.resultProperty().set(ButtonType.OK);
  }
}
