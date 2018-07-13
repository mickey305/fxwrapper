package com.cm55.fxlib.richTextArea;


/** 
 * キャレットの動きによらないピュアなスクロールのみを取り出す。
 * キャレットの動きによってもスクロールが発生するが、ここではマウスウィール、スクロールバーによるスクロールだけを取り出す。
 * そのため、キャレット移動イベント時刻以降のある程度の時間のスクロールイベントを排除する。
 */
public class PureScrolling {
  private long caretDetectTime;
  public void caretDetect() {
    caretDetectTime = System.currentTimeMillis();
  }
  public boolean isPureScrolling() {
    return (System.currentTimeMillis() - caretDetectTime) > 50;
  }
}