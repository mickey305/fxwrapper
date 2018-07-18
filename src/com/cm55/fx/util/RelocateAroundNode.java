package com.cm55.fx.util;

import com.cm55.fx.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.stage.*;

public abstract class RelocateAroundNode {
  
  public static class ForStage extends RelocateAroundNode {
    private FxStage stage;
    public ForStage(FxNode node, FxStage stage) {
      super(node, stage.widthProperty(), stage.heightProperty());
      this.stage = stage;
      if (stage.isShowing()) {
        relocateWindow();
        return;
      }
      listenWindowSizeChange();
    }
    protected void setX(double x) { stage.setX(x); }  
    protected void setY(double y) { stage.setY(y); }  
  }
  
  public static class ForDialog extends RelocateAroundNode {
    private FxDialog<?> dialog;
    public ForDialog(FxNode node, FxDialog<?> dialog) {
      super(node, dialog.widthProperty(), dialog.heightProperty());
      this.dialog = dialog;
      if (dialog.isShowing()) {
        relocateWindow();
        return;
      }
      listenWindowSizeChange();
    }
    protected void setX(double x) { dialog.setX(x); }  
    protected void setY(double y) { dialog.setY(y); }  
  }
  
  private FxNode node;
  private ReadOnlyDoubleProperty windowWidthProperty;
  private ReadOnlyDoubleProperty windowHeightProperty;
  private ChangeListener<Number>changeListener = null;
  
  protected abstract void setX(double x);
  protected abstract void setY(double y);
  
  protected RelocateAroundNode(FxNode node, ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height) {
    this.node = node;
    this.windowWidthProperty = width;
    this.windowHeightProperty = height;
  }
    
  protected void listenWindowSizeChange() {
    changeListener = new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        windowSizeChanged();
      }    
    };
    windowWidthProperty.addListener(changeListener);
    windowHeightProperty.addListener(changeListener);
  }
  
  protected void windowSizeChanged() {
    if (Double.isNaN(windowWidthProperty.get())) return;
    if (Double.isNaN(windowHeightProperty.get())) return;
    relocateWindow();    
    windowWidthProperty.removeListener(changeListener);
    windowHeightProperty.removeListener(changeListener);
    changeListener = null;
  }
  
  protected void relocateWindow() {
    getScreenSize();
    
    double windowWidth = windowWidthProperty.get();
    double windowHeight = windowHeightProperty.get();
    
    Bounds nodeB = node.node().localToScreen(node.node().getBoundsInLocal());
    double nodeCenterX = (nodeB.getMinX() + nodeB.getMaxX()) / 2;
    double nodeCenterY = (nodeB.getMinY() + nodeB.getMaxY()) / 2;
    
    double windowNewX = nodeCenterX - windowWidth / 2;
    double windowNewY = nodeCenterY - windowHeight / 2;

    windowNewX = Math.max(0, windowNewX);
    windowNewX = Math.min(windowNewX,  screenWidth - windowWidth);
    
    windowNewY = Math.max(0, windowNewY);
    windowNewY = Math.min(windowNewY,  screenHeight - windowHeight);
    
    setX(windowNewX);
    setY(windowNewY); 
  }

  private static void getScreenSize() {
    if (vb == null) {
      vb = Screen.getPrimary().getVisualBounds();
      screenWidth = vb.getWidth();
      screenHeight = vb.getHeight();
    }    
  }
  
  private static Rectangle2D vb;
  private static double screenWidth;
  private static double screenHeight;
}
