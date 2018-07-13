package com.cm55.fxlib.splitPane;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

/* ディバイダ =========================================================================================================*/


public class Divider extends Pane {
  
  public interface DividerCallback {    
    public void startOperation(ViewMouseHandler handler);    
    public void dragging(double move);    
    public abstract void endOperation();    
  }

  private MouseState mouseState;
  private DividerCallback mouseCallback;
  private boolean horizontal;
  
  public Divider(boolean horizontal) {
    this.horizontal = horizontal;
    if (horizontal) getStyleClass().setAll("horizontal-divider");
    else getStyleClass().setAll("vertical-divider");
    
    mouseState = new MouseState();
    //setFill(Color.TRANSPARENT);
    setOnMouseEntered(me -> {      
      mouseState.setInDivider(true);
    });
    setOnMouseExited(me -> {
      mouseState.setInDivider(false);
    });
  }
  
  public void setCallback(DividerCallback connection) {
    this.mouseCallback = connection;
  }

  private ViewMouseHandler handler = new ViewMouseHandler() {
    
    /** マウス押下時の位置 */
    public double pressedPosition;
    
    @Override
    public void pressed(MouseEvent me) {     
      if (!mouseState.inDivider) return;
      mouseState.setDragging(true);
      pressedPosition = getPosition(me);
    }

    @Override
    public void dragged(MouseEvent me) {
      if (!mouseState.dragging) return;
      mouseCallback.dragging(getPosition(me) - pressedPosition);
    }

    private double getPosition(MouseEvent me) {
      if (horizontal)
       return me.getX();
      else 
        return me.getY();
    }
    
    @Override
    public void released(MouseEvent me) {
      mouseState.setDragging(false);        
    }    
  };
  
  /** マウス状態のトラッカ */
  class MouseState {
    
    /** カーソルがディバイダの中にある */
    private boolean inDivider;
    
    /** マウスドラッグ中 */
    private boolean dragging;
    
    /** マウスカーソル変更中 */
    private boolean cursorChanging;

    MouseState() {
    }
    
    /** カーソルがディバイダ中にあるかを指定 */
    void setInDivider(boolean value) {
      inDivider = value;
      changeCursor();
    }
    
    /** マウスドラッグ中であるかを指定 */
    void setDragging(boolean value) {
      dragging = value;
      changeCursor();
    }

    /** マウスカーソルの変更 */
    void changeCursor() {
      if (!cursorChanging) {
        if (inDivider || dragging) {
          if (horizontal)
            setCursor(Cursor.H_RESIZE);        
          else 
            setCursor(Cursor.V_RESIZE);
          cursorChanging = true;
          mouseCallback.startOperation(handler);
        }
      } else {
        if (!inDivider && !dragging) {
          setCursor(Cursor.DEFAULT); 
          cursorChanging = false;          
          mouseCallback.endOperation();
        }
      }
    }
  }
}