package com.cm55.fxlib.splitPane;

import com.cm55.fxlib.*;

import javafx.scene.*;

/**
 * マウスのドラッグでレイアウトを行う。レイアウト対象はディバイダの左右あるいは上下に限られる。それ以外は何もされない 両者のminSize,
 * maxSizeが加味される。prefSizeは無視される。
 */
public class DragLayouter {
  
  private Part leftPart;
  private Part rightPart;
  private FxNode left;
  private double leftInit;
  private FxNode divider;
  private FxNode right;
  private double rightInit;
  private double move;
  private OrientationAdapter adapter;
  
  public DragLayouter(OrientationAdapter adapter, Part leftPart, FxNode divider, Part rightPart) {
    this.adapter = adapter;
    this.leftPart = leftPart;
    this.rightPart = rightPart;
    this.left = leftPart.node;
    this.divider = divider;
    this.right = rightPart.node;
    
    leftInit = leftPart.layoutSize;
    rightInit = rightPart.layoutSize;
  }

  public void setMove(double move) {
    this.move = move;
  }
  
  void layout() {
    if (move < 0)
      shrink();
    else
      expand();
    leftPart.layoutRatio = leftPart.layoutSize / adapter.targetAvail;
    rightPart.layoutRatio = rightPart.layoutSize / adapter.targetAvail;
  }

  /** 左側あるいは上側が縮小する */
  private void shrink() {     
    double moving = -move; // 左方向への変異
    
    double position = 0;
    if (leftInit - moving < 0) {
      
      moving = leftInit;
    }
    double leftMin = Consts.MIN_SIZE;
    leftMin = Math.max(adapter.minSize(left), Consts.MIN_SIZE);
    if (leftInit - moving < leftMin) {
      
      moving = leftInit - leftMin;
    }
    double rightMax = adapter.maxSize(right);
    if (rightInit + moving > rightMax) {
      
      moving = rightMax - rightInit;
    }
    leftPart.layoutSize = leftInit - moving;
    adapter.resize(left, leftPart.layoutSize);
    position = adapter.getPosition(left) + leftPart.layoutSize;
    adapter.relocate(divider, position);
    position += adapter.dividerThickness.get();
    adapter.relocate(right, position);
    adapter.resize(right, rightPart.layoutSize = rightInit + moving);
  }

  /** 左側あるいは上側が拡大する */
  private void expand() {
    double moving = move;
    double position = 0;

    if (rightInit - moving < 0) {
      moving = rightInit;
      
    }
    double rightMin = Math.max(Consts.MIN_SIZE, adapter.minSize(right));
    if (rightInit - moving < rightMin) {
      moving = rightInit - rightMin;
      
    }
    double leftMax = adapter.maxSize(left);
    if (leftInit + moving > leftMax) {
      
      moving = leftMax - leftInit;
    }
    

    leftPart.layoutSize = leftInit + moving;
    adapter.resize(left,  leftPart.layoutSize);
    position = adapter.getPosition(left) + leftPart.layoutSize;
    adapter.relocate(divider, position);
    position += adapter.dividerThickness.get();
    adapter.relocate(right, position);
    adapter.resize(right, rightPart.layoutSize = rightInit - moving);
  }
}