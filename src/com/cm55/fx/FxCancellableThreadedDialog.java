package com.cm55.fx;

import javafx.application.*;

/**
 * 時間のかかる処理、あるいはハングアップの可能性のある処理を別スレッドで実行し、その間キャンセルダイアログを表示する。
 * 結果には三通りある。成功、例外、キャンセル。
 * @author admin
 */
public abstract class FxCancellableThreadedDialog {

  /**
   * メッセージを指定して実行する
   * @param message
   */
  public FxCancellableThreadedDialog(FxNode node, String message) {
    
    FxProgressMessageDialog progress = new FxProgressMessageDialog(node, message);
    
    // キャンセルされた場合のフラグ
    boolean[]cancelled = new boolean[1];
    
    // スレッド
    Thread thread = new Thread() { public void run() {
      try {
        procedure();
        Platform.runLater(()-> {
          // ダイアログを明示的に閉じる
          progress.close();
          success();
        });
      } catch (Exception ex) {
        if (cancelled[0]) return;
        Platform.runLater(()-> {
          // ダイアログを明示的に閉じる
          progress.close();
          exception(ex);
        });
      }
    }};
    
    // キャンセルボタン押下のハンドリング
    progress.handleCancel(b -> {
      cancelled[0] = true;
      // キャンセルボタンが押されたとき、ダイアログは自動で閉じる。
      thread.interrupt();      
      try {
        thread.join();
      } catch (InterruptedException ex) {
      }          
      canceled();
    }).show();
    
    // スレッド実行
    thread.start();    
  }
  
  protected abstract void procedure() throws Exception;
  protected abstract void success();
  protected abstract void exception(Exception ex);
  protected abstract void canceled();
}
