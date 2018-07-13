package com.cm55.fxlib.richTextArea;

/** 可視レンジ */
public class VisibleRange {
  public final int min;
  public final int max;
  public final int center;
  public final int count;
  public VisibleRange(int min, int max) {
    this.min = min;
    this.max = max;
    count = Math.max(0, max - min + 1);
    center = Math.max(0, (max + min) / 2);
  }
  @Override
  public String toString() {
    return min + "," + max + "," + center + "," + count;
  }
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof VisibleRange)) return false;
    VisibleRange that = (VisibleRange)o;
    return this.min == that.min && this.max == that.max;
  }
}