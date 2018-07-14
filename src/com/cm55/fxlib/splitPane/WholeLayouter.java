package com.cm55.fxlib.splitPane;

import java.util.*;

import com.cm55.fxlib.*;

import javafx.scene.*;

/**
 * 全体のレイアウトを行う
 * <p>
 * ノードが以前レイアウトされたことがあればそのサイズを尊重する。そうでない場合にはprefSizeを採用する。
 * ノードがどちらの値も持っていない場合には上記に割り振られた以外のあまり領域が割り振られる。複数ある場合は均等割。
 * が、ただしもちろんminSizeを割ってはいけない。
 * </p>
 * <p>
 * 上記の方針で入り切らない場合、各ノードの領域をminSizeまで縮められるかを考える。
 * </p>
 * 
 * @author admin
 */
public class WholeLayouter {
  
  OrientationAdapter adapter;
  List<Part> parts;
  boolean tracking;
  
  public WholeLayouter(OrientationAdapter adapter,List<Part> parts) {
    this.adapter = adapter;
    this.parts = parts;
  }
  
  public void setTracking(boolean value) {
    tracking = value;
  }

  void layout() {      
    double fixedSize = 0;
    double restMinSize = 0;
    List<Part>restParts = new ArrayList<Part>();
    for (Part part: parts) {
      if (part.layoutSize != null && part.layoutRatio == null) {
        part.layoutRatio = part.layoutSize / adapter.targetAvail;
      }
      if (part.layoutSize != null && part.resizeFixed) {
        fixedSize += part.layoutSize;
      } else {
        restParts.add(part);
        double min = adapter.minSize(part.node);
        if (min > 0) restMinSize += min;          
      }
    }
    if (tracking) System.out.println("fixedSize:" + fixedSize + ", restMinSize:" + restMinSize);
    if (fixedSize + restMinSize <= adapter.targetAvail) {
      ratioLayout(adapter, adapter.targetAvail - fixedSize, restParts);
    } else {
      ratioLayout(adapter, adapter.targetAvail, parts);
    }
    
    doLayout(adapter);
  }
  
  /** 比率でのレイアウトを行う */
  private void ratioLayout(OrientationAdapter adapter, double realAvail, List<Part>parts) {
    if (tracking) System.out.println("ratioLayut " + realAvail + "," + parts.size());
    // まずレイアウトサイズが格納されていない部分を適当に決める
    double totalRatio = 0;
    for (Part part: parts) {
      if (part.layoutSize != null) {
        if (tracking) System.out.println("layoutSize " + part.layoutSize + "," + part.layoutRatio);
        totalRatio += part.layoutRatio;
        continue;
      }
      if (adapter.prefSize(part.node) > 0) part.layoutSize = adapter.prefSize(part.node);
      else part.layoutSize = 100.0;
      part.layoutRatio = part.layoutSize / realAvail;
      totalRatio += part.layoutRatio;
      if (tracking) System.out.println("layoutSize 100.0, " + part.layoutRatio);
    }
    // 比率にしたがってレイアウトサイズを決める
    // しかしこのときmin, max制限を超えてしまう場合がある。
    List<Part>notModified = new ArrayList<Part>();
    double modifiedTotal = 0;
    for (Part part: parts) {
      FxNode node = part.node;
      part.layoutSize = realAvail * part.layoutRatio / totalRatio;
      double min = adapter.minSize(node);
      double max = adapter.maxSize(node);
      if (part.layoutSize < Consts.MIN_SIZE) {
        modifiedTotal += (part.layoutSize = Consts.MIN_SIZE);
        continue;
      }
      if (min > 0 && part.layoutSize < min) {
        modifiedTotal += (part.layoutSize = min);
        continue;
      } 
      if (max > 0 && part.layoutSize > max) {
        modifiedTotal += (part.layoutSize = max);
        continue;
      }
      notModified.add(part);
    }
    if (modifiedTotal > 0 && notModified.size() > 0) {
      ratioLayout(adapter, realAvail - modifiedTotal, notModified);
    }
  }

  /** レイアウトすべきサイズはlayoutedにあるのでそのとおりにレイアウトする */
  private void doLayout(OrientationAdapter adapter) {
    if (tracking) System.out.println("doLayout");
    double position = 0;
    for (int index = 0; index < parts.size(); index++) {
      Part part = parts.get(index);
      FxNode node = part.node;
      adapter.relocate(node, position);
      adapter.resize(node, part.layoutSize);
      if (tracking) System.out.println("" + position + "," + part.layoutSize);
      part.layoutRatio = part.layoutSize / adapter.targetAvail;
      position += part.layoutSize;
      
      if (index < adapter.dividers.size()) {
        Divider divider = adapter.dividers.get(index);
        adapter.layoutDivider(divider, position);
        position += adapter.dividerThickness.get();
      }
    }
  }
}
