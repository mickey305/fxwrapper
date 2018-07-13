package com.cm55.fxlib;

import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.control.*;

public class FxSlider implements FocusControl<FxSlider> {

  private Slider slider;
  
  private boolean setting = false;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  
  public FxSlider(boolean horizontal) {
    slider = new Slider() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    if (horizontal) {
      slider.setOrientation(Orientation.HORIZONTAL);
    } else {
      slider.setOrientation(Orientation.VERTICAL);
    }
    slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldValue, Number newValue) -> {
      if (setting) return;
      if (positionChangedCallback != null) positionChangedCallback.callback(newValue.doubleValue());
    });
    slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean changing) {
        if (changeStartEndCallback != null) changeStartEndCallback.callback(changing);
      }
    });
  }
  
  public FxSlider(boolean horizontal, double min, double max) {
    this(horizontal);
    this.setMin(min);
    this.setMax(max);
  }
  
  public FxSlider(boolean horizontal, double min, double max, double tickUnit) {
    this(horizontal, min, max);
    slider.setMajorTickUnit(tickUnit);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
  }

  @Override
  public FxSlider setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  public Control getControl() {
    return slider;
  }
  
  public void setMin(double value) {
    setting = true; try { slider.setMin(value); } finally { setting = false; }
  }
  
  public void setMax(double value) {
    setting = true; try { slider.setMax(value); } finally { setting = false; }
  }
  
  public void setValue(double value) {
    setting = true; try { slider.setValue(value); } finally { setting = false; }
  }
  
  public void setWidth(double width) {
    slider.setPrefWidth(width);
    slider.setMaxWidth(width);
  }
  
  private FxCallback<Double>positionChangedCallback;
  private FxCallback<Boolean>changeStartEndCallback;
  
  public FxSlider setOnPositionChanged(FxCallback<Double>positionChangedCallback) {
    this.positionChangedCallback = positionChangedCallback;
    return this;
  }

  public FxSlider setOnChangeStartEnd(FxCallback<Boolean>changeStartEndCallback) {
    this.changeStartEndCallback = changeStartEndCallback;
    return this;
  }
}
