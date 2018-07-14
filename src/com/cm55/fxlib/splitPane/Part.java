package com.cm55.fxlib.splitPane;

import com.cm55.fxlib.*;

import javafx.scene.*;

/** 
 * {@link WholeLayouter}の方針を決定するノードごとのレイアウト情報
 * Paneのサイズが変更された場合の各ノードのサイズ保持の優先順としては以下。
 * <ul>
 * <li>過去にレイアウトされたことがあり、かつresizeFixedである。これは最優先でサイズ保持される。
 * <li>過去にレイアウトされたことがある。
 * <li>レイアウトされたことがなく、推奨サイズが指定されている。
 * <li>レイアウトされたことがなく、推奨サイズが指定されていない。
 * </ul>
 * <p>
 * ただし、上記の方針にかかわらず、Paneのサイズによってはサイズ保持されないことがある。
 * サイズが小さすぎて保持できず、やむなく小さくせざるをえない場合や、
 * 大きすぎても他のノードのmaxSizeがあり、サイズを大きくせざるをえない場合である。
 * </p>
 * @author admin
 *
 */
public class Part {
  
  public int order;
  
  /** ノード */
  public FxNode node;
  
  /** 画面のリサイズに際してサイズ固定 */
  public boolean resizeFixed;
  
  /** レイアウトサイズを保持する。なければnull */
  public Double layoutSize;

  /** レイアウト比率を保持する。なければnull */
  public Double layoutRatio;
  
  public Part(int order, FxNode node) {
    this.order = order;
    if (node == null) throw new NullPointerException();
    this.node = node;
  }
  
  @Override
  public String toString() {
    return resizeFixed + "," + layoutSize + "," + layoutRatio;
  }
}
