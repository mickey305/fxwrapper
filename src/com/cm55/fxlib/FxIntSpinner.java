package com.cm55.fxlib;

import java.util.function.*;

import javafx.scene.control.*;

/**
 * 整数値スピナー
 * @author admin
 *
 */
public class FxIntSpinner implements FocusControl<FxIntSpinner> {

  private Spinner<Integer> spinner;
  private SpinnerValueFactory<Integer>valueFactory;
  private Consumer<Integer> valueChangedCallback;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  private boolean updating;
  
  public FxIntSpinner() {
    spinner = new Spinner<Integer>() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    spinner.setEditable(true);
    spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
      if (valueChangedCallback != null && !updating) valueChangedCallback.accept(newValue); 
    });
  }

  public FxIntSpinner(int min, int max, int value) {
    this();
    setRange(min, max, value);
  }
  
  public FxIntSpinner(int min, int max, int value, Consumer<Integer> callback) {
    this(min, max, value);
    setValueChangedCallback(callback);
  }

  @Override
  public FxIntSpinner setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  /** レンジと現在値を設定する */
  public FxIntSpinner setRange(int min, int max, int value) {
    valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    spinner.setValueFactory(valueFactory);    
    valueFactory.setValue(value);
    valueFactory.getValue();
    return this;
  }
  
  public FxIntSpinner setValue(int value) {
    updating = true;
    try {
      valueFactory.setValue(value);
    } finally {
      updating = false;
    }
    return this;
  }

  /** コールバックを設定する */
  public FxIntSpinner setValueChangedCallback(Consumer<Integer> callback) {
    this.valueChangedCallback = callback;
    return this;
  }
  
  /** 現在の値を取得する */
  public int getValue() {
    return valueFactory.getValue();
  }

  /** コントロールを取得する */
  public Spinner<Integer> getControl() {
    return spinner;
  }
}
