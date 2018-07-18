package com.cm55.fx.winBounds;

import static com.cm55.fx.winBounds.WindowBoundsUtil.*;

import com.cm55.fx.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * 
 * @author admin
 */
public abstract class WindowBounds {

  /** 最大化状態。シリアライズされる */
  protected boolean maximized;
  
  /** ウインドウの位置とサイズ。シリアライズされる */
  protected double x, y, width, height;

  /** スクリーンサイズを取得し、設定が存在しない場合のデフォルトを作成する */
  public WindowBounds() {        
    maximized = false;
    
    width = screenWidth * 0.8;
    height = screenHeight * 0.5;
    if (width < 800) width = screenWidth - 100;
    if (height < 600) height = screenHeight - 100;
    centering();
  }
  
  protected void centering() {
    x = (screenWidth - width) / 2;
    y = (screenHeight - height) / 2;  
  }
  
  /** 最大化状態を設定する */
  public void setMaximized(boolean value) {
    maximized = value;
  }

  /** 領域を設定する */
  public void setBounds(Rectangle2D bounds) {
    x = bounds.getMinX();
    y = bounds.getMinY();
    width = bounds.getWidth();
    height = bounds.getHeight();
  }
  
  /**
   * 現在の状態を{@link FxStage}に設定する。ただし、スクリーンの大きさを考慮して補正される。
   * @param stage
   */
  public void setToStage(FxStage stage) {
    setToStage(stage.getStage(), null);
  }
  
  /** 現在の状態を{@link FxDialog}に設定する。ただし、スクリーンの大きさを考慮して補正される */
  public void setToDialog(FxDialog<?>dialog) {
    DialogPane dialogPane = dialog.getDialogPane();
    setToStage((Stage)dialogPane.getScene().getWindow(), dialogPane);
  }
  

  private void setToStage(Stage stage, DialogPane dialogPane) {
    
    Rectangle2D r = fixWindowBounds(new Rectangle2D(x, y, width, height));
    x = r.getMinX();
    y = r.getMinY();
    width = r.getWidth();
    height = r.getHeight();
    
    stage.setX(x);
    stage.setY(y);
    if (dialogPane != null) {
      dialogPane.setPrefWidth(width);
      dialogPane.setPrefHeight(height);
    } else {
      stage.setWidth(width);
      stage.setHeight(height);
    }
    stage.setMaximized(maximized);
  }
  
  @Override
  public String toString() {
    return maximized + "," + x + "," + y + "," + width + "," + height;
  }
}
