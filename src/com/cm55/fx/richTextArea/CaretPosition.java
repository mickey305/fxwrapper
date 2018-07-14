package com.cm55.fx.richTextArea;



/** キャレットの位置情報 */
public  class CaretPosition {
  public final int offset;
  public final int line;
  public final int column;
  public CaretPosition(int offset, int line, int column) {
    this.offset = offset;
    this.line = line;
    this.column = column;
  }
  @Override
  public String toString() {
    return offset + "," + line + "," + column;
  }
}
