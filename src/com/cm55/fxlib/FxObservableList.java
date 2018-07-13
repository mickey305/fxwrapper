package com.cm55.fxlib;

import java.util.*;
import java.util.function.*;

//Java9コンパイラはエラーを出すが使用可能
import com.sun.javafx.collections.*;

import javafx.beans.property.*;
import javafx.collections.*;

@SuppressWarnings("restriction")
public class FxObservableList<E> extends ObservableListWrapper<E> {

  private FxEventType<FxObservableListChanged>LIST_CHANGED = new FxEventType<>();
  private FxEventType<FxObservableListAboutToRemove>ABOUT_TO_REMOVE = new FxEventType<>();
  private FxEventer eventer = new FxEventer();
  
  private boolean noEvent;
  
  public FxObservableList() {
    super(new ArrayList<E>());

    addListener(new ListChangeListener<E>() {
      @Override
      public void onChanged(javafx.collections.ListChangeListener.Change<? extends E> c) {
        //ystem.out.println("onChanged " + System.identityHashCode(c));
        if (noEvent) return;
        while (c.next()) {
          if (c.wasPermutated()) {
            for (int i = c.getFrom(); i < c.getTo(); ++i) {
              // permutate
            }
          } else if (c.wasUpdated()) {
            // update item
            eventer.fire(LIST_CHANGED, new FxObservableListChanged(ChangedType.UPDATE, c.getFrom(), null));
          } else {
            for (E remitem : c.getRemoved()) {
              eventer.fire(LIST_CHANGED, new FxObservableListChanged(ChangedType.REMOVE, null, remitem));
            }
            for (E additem : c.getAddedSubList()) {
              eventer.fire(LIST_CHANGED, new FxObservableListChanged(ChangedType.ADD, indexOf(additem), additem));
            }
          }
        }
      }
    });
  }

  public void listenListChanged(Consumer<FxObservableListChanged>l) {
    eventer.add(LIST_CHANGED, l);
  }

  public void listenAboutToRemove(Consumer<FxObservableListAboutToRemove>l) {
    eventer.add(ABOUT_TO_REMOVE, l);
  }
  
  /**
   * 注意：イベント処理中に呼び出してはいけない。イベントが重複してしまう。
   * ※このイベントは実際には必要無い、なぜならこのイベントを{@link FxTable}側で捉えて行内容を更新することは無い。
   * 行フィールドを{@link SimpleStringProperty}等にしておけば、そのオブジェクトに値が設定された時点で更新される。
   * @param index
   */
  public void fireUpdate(int index) {
    super.beginChange();
    super.nextUpdate(index);
    super.endChange();
  }

  public static enum ChangedType {
    ADD, REMOVE, UPDATE;
  }
  
  public static class FxObservableListChanged {
    public final ChangedType type;
    public final Integer index;
    public final Object object;
    
    public FxObservableListChanged(ChangedType type, Integer index, Object object) {
      this.type = type;
      this.index = index;
      this.object = object;
    }

    @Override
    public String toString() {
      return type + "," + index;
    }
  }
  
  /**
   * 削除前通知イベント
   * @author admin
   */
  public static class FxObservableListAboutToRemove {    
  }
  
  /*================================================================================================================== 
   * 削除動作を削除前に事前通知させる。これは、以下の問題に対処するため、
   * 1. ある行を選択中に、その行を削除すると、その前の行が自動選択され、選択イベントが発行されてしまう。
   * 2. 選択中の行より前の行を削除すると、やはり選択行が自動移動するため、選択イベントが発行されてしまう。
   * 3. 選択中の行より後の行を削除すると、この現象は起こらないが、1,2に対処するために、行削除イベントをとらえて事前に「選択無し」状態にしようとすると、
   * 1,2とはイベントの通知順が異なるらしく、想定通りにいかない。
   * このため、行変更イベントや選択イベントの起こる以前に、「削除が起ころうとしている」という事象をイベントとして通知させる。
   */
  
  @Override
  public boolean retainAll(Collection<?> c) {
    fireAboutToRemoveEvent();
    return super.retainAll(c);
  }
  
  @Override
  public boolean removeAll(Collection<?> c) {
    fireAboutToRemoveEvent();
    return super.removeAll(c);
  }
  
  @Override
  public E remove(int index) {
    fireAboutToRemoveEvent();
    return super.remove(index);
  }
  
  @Override
  public boolean remove(Object o) {
    fireAboutToRemoveEvent();
    return super.remove(o);
  }
  
  @Override
  public void clear() {
    fireAboutToRemoveEvent();
    super.clear();
  }
  
  private void fireAboutToRemoveEvent() {
    eventer.fire(ABOUT_TO_REMOVE, new FxObservableListAboutToRemove());
  }
  
  /** イベントを発行しない */
  public void setNoEvent(boolean value) {
    noEvent = value;
  }
}
