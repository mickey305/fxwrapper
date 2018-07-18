package com.cm55.fx;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class FxDialog<R> extends FxWindow<FxDialog<R>> {

  public final Dialog<R>dialog;
  
  public FxDialog() {
    dialog = new Dialog<R>();
  }

  public FxDialog(Dialog<R>dialog) {
    this.dialog = dialog;
  }

  
  public DialogPane getDialogPane() {
    return dialog.getDialogPane();
  }
  
  public FxDialog<R>setDialogPane(DialogPane dialogPane) {
    dialog.setDialogPane(dialogPane);
    return this;
  }

  
  public FxDialog<R> setResult(R value) {
    dialog.setResult(value);
    return this;
  }

  
  // Overriding FxWindow ///////////////////////////////////////////////////////

  @Override
  public StringProperty titleProperty() {
    return dialog.titleProperty();
  }
  
  @Override
  public FxDialog<R>initModality(Modality modality) {
    dialog.initModality(modality);
    return this;
  }
  
  @Override
  public FxDialog<R>setResizable(boolean value) {
    dialog.setResizable(value);
    return this;
  }
  

  @Override
  public FxDialog<R>initOwner(Window window) {
    dialog.initOwner(window);
    return this;
  }
  
  @Override
  public FxDialog<R> setTitle(String title) {
    dialog.setTitle(title);
    return this;
  }
  
  @Override
  public void showAndWait() {
    dialog.showAndWait();
  }
  
  @Override
  public void show() {
    dialog.show();
  }
  
  @Override
  public ReadOnlyDoubleProperty widthProperty() {
    return dialog.widthProperty();
  }
  
  @Override
  public ReadOnlyDoubleProperty heightProperty() {
    return dialog.heightProperty();
  }
  
  @Override
  public FxDialog<R> setX(double x) {
    dialog.setX(x);
    return this;
  }
  
  @Override
  public FxDialog<R> setY(double y) {
    dialog.setY(y);
    return this;
  }
  
  @Override
  public boolean isShowing() {
    return dialog.isShowing();
  }
  
  @Override
  public FxDialog<R> setWidth(double width) {
    dialog.setWidth(width);
    return this;
  }

  @Override
  public FxDialog<R> setHeight(double height) {
    dialog.setHeight(height);
    return this;
  }
}
