package com.cm55.fx;

import java.util.function.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.stage.*;

public class FxStage extends FxWindow<FxStage> {

  private final Stage stage;
  
  private SimpleBooleanProperty showingProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty showingProperty() { return showingProperty; }

  private boolean hidingEvent;
  private Consumer<FxStage>onShowingCallback;
  private Consumer<FxStage>onHidingCallback;
  private Consumer<Boolean>onFocusChanged;
  
  public FxStage(Stage stage) {
    this.stage = stage;
    init();
  }
  
  public FxStage() { 
    stage = new Stage();
    init();
  }
  
  private void init() {
    // 表示直前のコールバック
    stage.setOnShowing(e-> {
      if (onShowingCallback != null) onShowingCallback.accept(this);
    });

    // 消去直前のコールバック
    stage.setOnHiding(e-> {
      if (onHidingCallback != null) onHidingCallback.accept(this);
      hidingEvent = true;
      try {
        showingProperty.set(false);
      } finally {
        hidingEvent = false;
      }
    });

    // shoingPropertyのハンドリング
    showingProperty.addListener((ChangeListener<Boolean>)(a, o, n)-> {
      if (n) {
        stage.show(); 
      } else {
        if (hidingEvent) return;
        stage.hide();
      }
    });
    
    // フォーカス変更のハンドリング
    this.stage.focusedProperty().addListener((ChangeListener<Boolean>)(ob, o, n)-> {
      if (onFocusChanged != null) onFocusChanged.accept(n);
    });
  }

  /** 表示直前に呼ばれるコールバックを登録 */
  public FxStage setOnShowing(Consumer<FxStage>callback) {
    this.onShowingCallback = callback;
    return this;
  }

  /** 消去直前に呼ばれるコールバックを登録 */
  public FxStage setOnHiding(Consumer<FxStage>callback) {
    this.onHidingCallback = callback;
    return this;
  }

  /** フォーカス変更時に呼ばれるコールバックを登録 */
  public FxStage setOnFocusChanged(Consumer<Boolean>callback) {
    this.onFocusChanged = callback;
    return this;
  }
  
  /** シーンを設定*/
  public FxStage setScene(FxScene scene) {
    stage.setScene(scene.scene);
    return this;
  }
  
  /** Stageを取得 */
  public Stage getStage() {
    return stage;
  }

  public FxStage setOnCloseRequest(EventHandler<WindowEvent> value) {
    stage.setOnCloseRequest(value);
    return this;
  }
  
  // Overriding FxWindow ///////////////////////////////////////////////////////
  
  @Override
  public StringProperty titleProperty() {
    return stage.titleProperty();
  }
  
  @Override
  public FxStage setResizable(boolean value) {
    stage.setResizable(value);
    return this;
  }
  
  /** タイトルを設定 */
  @Override
  public FxStage setTitle(String title) {
    stage.setTitle(title);
    return this;
  }
  
  /** オーナーを設定 */
  @Override
  public FxStage initOwner(Window window) {
    stage.initOwner(window);
    return this;
  }

  
  @Override
  public void show() {
    stage.show();
  }
  
  @Override
  public void showAndWait() {
    stage.showAndWait();
  }

  @Override
  public ReadOnlyDoubleProperty widthProperty() {
    return stage.widthProperty();
  }

  @Override
  public ReadOnlyDoubleProperty heightProperty() {
    return stage.heightProperty();
  }
  
  @Override
  public boolean isShowing() {
    return stage.isShowing();
  }
  
  @Override
  public FxStage setX(double x) {
    stage.setX(x);
    return this;
  }

  @Override
  public FxStage setY(double y) {
    stage.setY(y);
    return this;
  }
  
  @Override
  public FxStage setWidth(double width) {
    stage.setWidth(width);
    return this;
  }

  @Override
  public FxStage setHeight(double height) {
    stage.setHeight(height);
    return this;
  }

  @Override
  public FxStage initModality(Modality value) {
    stage.initModality(value);
    return this;
  }
  
  
}
