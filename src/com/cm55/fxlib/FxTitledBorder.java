package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FxTitledBorder extends StackPane {

  private Label titleLabel = new Label();
  private StackPane contentPane = new StackPane();
  private Node content;

  public FxTitledBorder() {
    titleLabel.setText("default title");
    titleLabel.getStyleClass().add("bordered-titled-title");
    StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);

    getStyleClass().add("bordered-titled-border");
    getChildren().addAll(titleLabel, contentPane);
  }
  
  public FxTitledBorder(String title, Node content) {
    this();
    setTitle(title);
    setContent(content);
  }
  
  public void setContent(Node content) {
    content.getStyleClass().add("bordered-titled-content");
    contentPane.getChildren().add(content);
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
}
