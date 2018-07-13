package com.cm55.fxlib;

import java.util.function.*;

import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.*;

/**
 * 整数値スピナー
 * @author admin
 *
 */
public class FxDoubleSpinner implements FocusControl<FxDoubleSpinner> {

  private Spinner<Double> spinner;
  private DoubleSpinnerValueFactory valueFactory;
  private Consumer<Double> valueChangedCallback;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  
  public FxDoubleSpinner() {
    spinner = new Spinner<Double>() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    spinner.setEditable(true);
    spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
      if (valueChangedCallback != null) valueChangedCallback.accept(newValue);
 
    });
  }

  public FxDoubleSpinner(double min, double max, double step, double value) {
    this();
    setRange(min, max, step, value);
  }
  
  public FxDoubleSpinner(double min, double max, double step, double value, Consumer<Double> callback) {
    this(min, max, step, value);
    setValueChangedCallback(callback);
  }

  @Override
  public FxDoubleSpinner setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  /** レンジと現在値を設定する */
  public FxDoubleSpinner setRange(double min, double max, double step, double value) {
    valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, value);
    valueFactory.setAmountToStepBy(step);
    spinner.setValueFactory(valueFactory);    
    valueFactory.setValue(value);
    valueFactory.getValue();
    return this;
  }
  
  public FxDoubleSpinner setValue(double value) {
    valueFactory.setValue(value);
    return this;
  }

  /** コールバックを設定する */
  public FxDoubleSpinner setValueChangedCallback(Consumer<Double> callback) {
    this.valueChangedCallback = callback;
    return this;
  }
  
  /** 現在の値を取得する */
  public double getValue() {
    return valueFactory.getValue();
  }

  /** コントロールを取得する */
  public Spinner<Double> getControl() {
    return spinner;
  }
}
