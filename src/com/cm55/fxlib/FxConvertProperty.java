package com.cm55.fxlib;

// Java9コンパイラはエラーを出すが使用可能
import com.sun.javafx.binding.*;

import javafx.beans.*;
import javafx.beans.value.*;

/**
 * ある型の値を格納するが、ObservableValueとして現れる値はそれとは異なる型になる。
 * 例えば、longの時刻値を確認し、ObservableValueとしてはString値として現れるとき
 * <pre>
 * public FxConvertPropert<Long, String>property = new FxConvertProperty<Long, String>() {
 *   public String getValue() {
 *     return String.format("%20x", get());
 *   }
 * };
 * </pre>
 * <p>
 * などとする。さらに、格納される値の取得・設定を外部から行えないようにするには、コンストラクタとしてAccessorを与えることができる。
 * </p>
 * <pre>
 * private Accessor<Long> accessor = new Accessor<Long>();
 * public FxConvertPropert<Long, String>property = new FxConvertProperty<Long, String>(accessor) {
 *   public String getValue() {
 *     return String.format("%20x", get());
 *   }
 * };
 * </pre>
 * <p>
 * この場合propertyのget(), set()メソッドにはアクセスできなくなる。
 * </p>
 * @author admin
 *
 * @param <S>
 * @param <T>
 */
public abstract class FxConvertProperty<S, T> implements ObservableValue<T> {

  public static class Accessor<S> {
    private FxConvertProperty<S, ?>property;
    public S get() {
      return property.internalGet();
    }
    public void set(S value) {
      property.internalSet(value);
    }
  }

  private Accessor<S> accessor;
  protected S value;

  public FxConvertProperty() {    
  }
  
  public FxConvertProperty(Accessor<S> accessor) {
    this.accessor = accessor;
    accessor.property = this;
  }
    
  public S get() {
    if (accessor != null) throw new IllegalStateException();
    return internalGet();
  }
  
  
  public void set(S value) {
    if (accessor != null) throw new IllegalStateException();
    internalSet(value);
  }

  protected S internalGet() {
    return value;
  }
  
  protected void internalSet(S value) {
    this.value = value;
    fireValueChangedEvent();
  }
  
  @SuppressWarnings("restriction")
  private ExpressionHelper<T> helper = null;
  
  @SuppressWarnings("restriction")
  public void addListener(ChangeListener<? super T> listener) {
    helper = ExpressionHelper.addListener(helper, this, listener);      
  }

  @SuppressWarnings("restriction")
  public void removeListener(ChangeListener<? super T> listener) {
    helper = ExpressionHelper.removeListener(helper, listener);
  }

  @SuppressWarnings("restriction")
  public void addListener(InvalidationListener listener) {
    helper = ExpressionHelper.addListener(helper, this, listener);
  }
  
  @SuppressWarnings("restriction")
  public void removeListener(InvalidationListener listener) {
    helper = ExpressionHelper.removeListener(helper, listener);
  }    

  @SuppressWarnings("restriction")
  protected void fireValueChangedEvent() {
    ExpressionHelper.fireValueChangedEvent(helper);
  }
  
  public abstract T getValue();
}
