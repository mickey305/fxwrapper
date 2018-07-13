package com.cm55.fxlib.richTextArea;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;
import org.fxmisc.richtext.model.TwoDimensional.*;
import org.reactfx.value.*;

import javafx.application.*;

/** 指定行を画面センターにするべくスクロールする。 */
public class CenterScroller {
  private static final boolean DEBUG = false;
  private StyleClassedTextArea textArea;
  private double viewHeight;
  private LineNumbers lineNumber;
  private int lineCount;
  private int targetIndex;
  private Runnable finished;
  private VisibleRange lastRange;
  
  public CenterScroller(StyleClassedTextArea textArea, LineNumbers lineNumber, int targetIndex) {
    this.textArea = textArea;
    this.viewHeight = textArea.getHeight();
    this.lineNumber = lineNumber;
    this.targetIndex = targetIndex;      
    int offset = textArea.getText().length();
    Position p = textArea.offsetToPosition(offset, TwoDimensional.Bias.Forward);
    lineCount = p.getMajor();
    //ystem.out.println("lineCount " + lineCount);
  }
  
  public void start(Runnable finished) {
    this.finished = finished;
    if (viewHeight < 1) {
      finished.run();
      return;
    }
    if (lineCount <= targetIndex) {
      if (DEBUG) System.out.println("p1");
      finished.run();
      return;
    }
    scroll();      
  }
  
  private void scroll() {
    boolean result = doScroll();
    if (DEBUG) System.out.println("doScroll result " + result);
    if (!result) finished.run();
  }
  
  private boolean doScroll() {
    
    VisibleRange range = lineNumber.getVisibleRange();    
    if (lastRange != null && range.equals(lastRange)) {
      if (DEBUG) System.out.println("range equals " + range + "," + lastRange);
      return false;
    }    
    if (range.count > 0) {
      lastRange = range;
      if (DEBUG) System.out.println("scroll range " + range + ", line " + targetIndex);    

      // 指定行が現在の中心行から何行分離れているかを計算。3行程度であれば何もしない。
      int distance = targetIndex - range.center;
      if (DEBUG) System.out.println("distance from center " + distance);
      if (Math.abs(distance) <= 2) return false;
      if (distance < 0 && range.min == 0) return false;
      if (distance > 0 && lineCount <= range.min) return false;
    
      // 
      double scrollMove = distance * viewHeight / range.count;
      if (DEBUG) System.out.println("scrollMove " + scrollMove);
    
      Var<Double>y = textArea.estimatedScrollYProperty();
      double newValue = Math.max(0,  y.getValue() + scrollMove);  
      if (DEBUG) System.out.println("scoller " + y.getValue() + " to " + newValue);
      y.setValue(newValue);
    }
    
    Platform.runLater(()-> {
      scroll();
    });
    return true;
  }
}