package com.cm55.fxlib;

import java.util.*;

import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * ウインドウ最大化以前のウインドウ領域を求める。
 * maximizedPropertyのイベントの前後に位置とサイズ変更のイベントが発生するので、それ以前の最後の領域を保持する。
 * 例えば、ウインドウ最大化ボタンを押すと、以下のような順序でイベントが発行されてしまう。
 * <ul>
 * <li>ウインドウのx位置変更が例えば-5で発生
 * <li>ウインドウ最大化イベントが発生
 * <li>ウインドウの幅変更がスクリーンサイズとして発生
 * </ul>
 * <p>
 * このため、最大化前の正しい領域を取得するには、各イベントによる領域を時間順に保持しておき、最大化イベントの前後のものを除去し、
 * さらに、保持されたリストの中の最後のものを選択しなければならない。
 * </p>
 * <pre>
 * FxStageBoundsHolder holder = new FxStageBoundsHolder(stage);
 * Rectangle2D bounds = older.getLastBounds();
 * </pre>
 * @author admin
 */
public class FxStageBoundsHolder {

  private Stage stage;
  private DialogPane dialogPane;
  private Long lastMaximizedTime;

  /** 最大化の影響が及ぼす前後時間範囲 */
  private static final int TIME_MARGIN = 5;

  /**
   * 通常のステージの場合
   * @param stage
   */
  public FxStageBoundsHolder(Stage stage) {
    this(stage, null);
  }

  /**
   * ダイアログの場合
   * @param dialog
   */
  public FxStageBoundsHolder(Dialog<?>dialog) {
    this((Stage)dialog.getDialogPane().getScene().getWindow(),
        dialog.getDialogPane());    
  }
  
  /**
   * ダイアログの場合には、Stage(Window)のサイズを取得しても無意味。DialogPaneのサイズを記録する。
   * @param stage
   * @param dialogPane
   */
  private FxStageBoundsHolder(Stage stage, DialogPane dialogPane) {  
    this.stage = stage;
    this.dialogPane = dialogPane;
    
    // 最大化していなければ現在の領域を取得
    if (!stage.isMaximized()) {
      boundsWithTimeHistory.add(getCurrentBoundsWithTime());
    }

    // 最大化状態を監視
    stage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
        if (stage.isMaximized()) {
          // 最大化状態になったら、その時刻を記録
          lastMaximizedTime = System.currentTimeMillis();
        }
        releaseNeedless();
      }
    });

    // 領域変更を監視
    ChangeListener<Number> boundsListener = new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        boundsWithTimeHistory.add(getCurrentBoundsWithTime());
        releaseNeedless();
      }
    };
    stage.xProperty().addListener(boundsListener);
    stage.yProperty().addListener(boundsListener);
    stage.widthProperty().addListener(boundsListener);
    stage.heightProperty().addListener(boundsListener);
  }

  /** 最大化前の最後の領域を取得する */
  public Rectangle2D getLastBounds() {
    releaseNeedless();
    if (boundsWithTimeHistory.size() == 0)
      return null;
    return boundsWithTimeHistory.get(0).bounds;
  }

  /** 現在の領域を取得する */
  private BoundsWithTime getCurrentBoundsWithTime() {
    double width, height;
    if (dialogPane == null) {
      width = stage.getWidth();
      height = stage.getHeight();
    } else {
      width = dialogPane.getWidth();
      height = dialogPane.getHeight();
    }
    return new BoundsWithTime(new Rectangle2D(stage.getX(), stage.getY(), width, height));
  }

  /** イベントによる変更領域とその時刻 */
  private List<BoundsWithTime> boundsWithTimeHistory = new ArrayList<BoundsWithTime>();

  /** 時刻とともに領域を記録 */
  private static class BoundsWithTime {
    final Rectangle2D bounds;
    final long time;

    BoundsWithTime(Rectangle2D bounds) {
      this.bounds = bounds;
      this.time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
      return time + ":" + bounds;
    }
  }

  /** 最大化時刻前後のスロットを削除する */
  private void releaseNeedless() {
    
    // 最大化時刻前後の記録を削除
    releaseAroundMaximizedTime();
    
    // 最後の有効なスロットを残す
    leaveLastValidElement();

    /*
    if (boundsWithTimeHistory.size() > 0) {
      System.out.println("" + boundsWithTimeHistory.get(0));
    } else {
      System.out.println("no bounds");
    }
    */
  }

  /** 最後の最大化時刻があるとき、そのTIME_MARGIN前後の記録を削除する。これらは最大化によるイベントであるため */
  private void releaseAroundMaximizedTime() {
    if (lastMaximizedTime == null) return;
    
    for (int i = boundsWithTimeHistory.size() - 1; i >= 0; i--) {
      BoundsWithTime timeBounds = boundsWithTimeHistory.get(i);
      //ystem.out.println(i + ":" + timeBounds);
      long time = timeBounds.time;
      if (time < lastMaximizedTime - TIME_MARGIN) continue;
      if (lastMaximizedTime + TIME_MARGIN < time) continue;
      boundsWithTimeHistory.remove(i);
    }
    
    //ystem.out.println("after remove " + boundsWithTimeHistory.size());
  }

  /**
   * 現在時刻からTIME_MARGIN経過していないものはすべて残し、経過しているもののうちの最後のもの一つを残す。
   * これはリストの最初の要素になる。
   */
  private void leaveLastValidElement() {
    long currentTime = System.currentTimeMillis();
    int validIndex = -1;
    for (int i = boundsWithTimeHistory.size() - 1; i >= 0; i--) {
      BoundsWithTime slot = boundsWithTimeHistory.get(i);
      if (slot.time < currentTime - TIME_MARGIN) {
        validIndex = i;
        break;
      }
    }
    if (validIndex <= 0) return;
    
    for (int remove = validIndex; remove > 0; remove--) 
      boundsWithTimeHistory.remove(0);
        
  }
}
