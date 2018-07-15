package com.cm55.fx;

import com.cm55.fx.util.*;

import javafx.scene.control.*;
import javafx.stage.*;

/** 
 * モーダルダイアログ。これはWindowモーダルのみ。
 * OK・キャンセルボタン等を一切つけず、独自のボタンで選択する場合のテンプレート。
 * キャンセル等はできず、必ずいずれかのボタンが選択される。
 * ただし、cancellableを呼び出したときはキャンセル可
 */
public class FxModalButtonsDialog {

  protected FxNode node;
  protected Dialog<Integer> dialog;
  protected Window window;
  protected DialogPane dialogPane;
  
  public void setup(FxNode node, String title, String message, String[]buttonLabels) {
    this.node = node;
    FxHBox hbox = new FxHBox();
    for (int i = 0; i < buttonLabels.length; i++) {
      int index = i;
      FxButton button = new FxButton(buttonLabels[i], b->select(index));
      hbox.add(button);
    }
    hbox.setSpacing(10);
    dialog = new Dialog<Integer>();
    dialog.setTitle(title);
    dialog.initOwner(node.node().getScene().getWindow());
    dialog.initModality(Modality.WINDOW_MODAL);   
    dialogPane = dialog.getDialogPane();    
    dialogPane.setContent(
        new FxVBox(new FxLabel(message), hbox).setSpacing(10).node()
    );
    window = dialogPane.getScene().getWindow();
  }

  public FxModalButtonsDialog setCancellable() {
    window.setOnCloseRequest(e-> {});
    return this;
  }
  
  private int result = -1;
  
  private void select(int index) {
    this.result = index;
    window.hide();
  }
  
  /**
   * 何番目のボタンを選択したかを知らせる。キャンセル時は-1
   */
  protected int showModal() {
    result = -1;
    new RelocateAroundNode.ForDialog(node,  dialog);
    dialog.showAndWait();
    return result;
  }
}
