package com.cm55.fx;

import javafx.beans.property.*;
import javafx.stage.*;

public abstract class FxWindow<W> {

  public abstract StringProperty titleProperty();
  public abstract W initModality(Modality value);
  public abstract ReadOnlyDoubleProperty widthProperty();
  public abstract ReadOnlyDoubleProperty heightProperty();
  public abstract boolean isShowing();
  public abstract W setX(double x);
  public abstract W setY(double y);
  public abstract void show();
  public abstract void showAndWait();
  public abstract W initOwner(Window window);
  public abstract W setTitle(String title);
  public abstract W setResizable(boolean value);
  public abstract W setWidth(double value);
  public abstract W setHeight(double value);
}
