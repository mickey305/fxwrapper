package com.cm55.fxlib;

import java.util.*;
import java.util.stream.*;

/**
 * コンボボックス等で使用される選択肢。
 * 要素としては任意で良いが、その要素から文字列ラベルを取得するインターフェース、その要素から比較用オブジェクトを取り出すインターフェースが必要。
 * @author admin
 *
 * @param <T> 要素型
 */
public class FxSelections<T> {

  /** ラベルを取り出すインターフェース */
  public interface ToLabel<T> {
    public String get(T t);
  }

  /** 比較用オブジェクトを取り出すインターフェース */
  public interface ToComparator<T> {
    public Object get(T t);
  }

  ToLabel<T> toLabel;
  ToComparator<T> toComparator;
  String noneLabel;
  List<T> list;
  List<String> labels;
  
  /** 各種パラメータを指定する。noneLabelはnullでも良い。noneLabelは何も選択されていない状態nullを表す */
  public FxSelections(ToLabel<T> toLabel, ToComparator<T> toComparator, String noneLabel, @SuppressWarnings("unchecked") T...selections) {
    this(toLabel, toComparator, noneLabel, 
        Arrays.stream(selections).collect(Collectors.toList()));
  }
  
  /** 各種パラメータを指定する。noneLabelはnullでも良い。noneLabelは何も選択されていない状態nullを表す */
  public FxSelections(ToLabel<T> toLabel, ToComparator<T> toComparator, String noneLabel, List<T>selections) {
    this.toLabel = toLabel;
    this.toComparator = toComparator;
    this.noneLabel = noneLabel;
    this.list = selections;
    
    labels = selections.stream().map(s -> toLabel.get(s)).collect(Collectors.toList());
    if (noneLabel != null)
      labels.add(0, noneLabel);
  }
}
