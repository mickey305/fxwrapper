package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FxTitledBorder implements FxParent  {

  private StackPane stackPane;
  private Label titleLabel = new Label();
  private StackPane contentPane = new StackPane();
  private Node content;

  public FxTitledBorder() {
    stackPane = new StackPane();
    titleLabel.setText("default title");
    titleLabel.getStyleClass().add("bordered-titled-title");
    StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);

    stackPane.getStyleClass().add("bordered-titled-border");
    stackPane.getChildren().addAll(titleLabel, contentPane);
  }
  
  public FxTitledBorder(String title, FxNode content) {
    this();
    setTitle(title);
    setContent(content);
  }
  
  public void setContent(FxNode content) {
    content.node().getStyleClass().add("bordered-titled-content");
    contentPane.getChildren().add(content.node());
  }

  public Node getContent() {
    return content;
  }

  public void setTitle(String title) {
    titleLabel.setText(" " + title + " ");
  }

  public String getTitle() {
    return titleLabel.getText();
  }

  @Override
  public StackPane node() {
    return stackPane;
  }
}