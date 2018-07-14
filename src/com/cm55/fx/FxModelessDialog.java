package com.cm55.fx;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.stage.*;

/** 
 * モードレスダイアログ
 * <p>
 * モードレスなのでOKボタン等は付けない。Return Typeも存在しない。
 * BooleanPropertyにてオープン・クローズができるようにする。
 * </p>
 */
public class FxModelessDialog {

  protected final Dialog<Object> dialog;
  protected final Window window;
  protected final DialogPane dialogPane;
  protected final SimpleBooleanProperty showingProperty = new SimpleBooleanProperty();
  
  public FxModelessDialog() {
    dialog = new Dialog<Object>();
    dialog.initModality(Modality.NONE);   
    dialogPane = dialog.getDialogPane();    
    window = dialogPane.getScene().getWindow();
    window.setOnCloseRequest(event -> showingProperty.set(false));
    window.setOnShowing(e-> onShowing());
    window.setOnHiding(e->  onHiding());
    showingProperty.addListener((ob,o,n)-> {
      if (n) dialog.show();
      else window.hide();
    });
  }
  
  protected void onShowing() {}
  protected void onHiding() {}
}
