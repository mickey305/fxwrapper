package com.cm55.fxlib;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

public class FxLabel implements FxNode {

  public static final OverrunStyle[]OVERRUN_STYLES = new OverrunStyle[] {
      OverrunStyle.WORD_ELLIPSIS, OverrunStyle.CENTER_ELLIPSIS, OverrunStyle.LEADING_ELLIPSIS        
  };
  
  public static final TextAlignment[]TEXT_ALIGNMENTS = new TextAlignment[] {
      TextAlignment.LEFT, TextAlignment.CENTER, TextAlignment.RIGHT
  };
  
  private Label label;
  public FxLabel() {
    label = new Label();
    label.setAlignment(Pos.CENTER);
  }
  
  public FxLabel(String text) {
    this();
    setText(text);
  }

  public FxLabel setText(String text) {
    label.setText(text);
    return this;
  }
  
  public String getText() {
    return label.getText();
  }
  
  public FxLabel setAlign(int align) {
    label.setTextOverrun(OVERRUN_STYLES[align]);
    label.setTextAlignment(TEXT_ALIGNMENTS[align]);
    return this;
  }
  
  private Tooltip tooltip;
  
  public FxLabel setTooltip(String tooltipText) {
    if (tooltip != null) {
      Tooltip.uninstall(label, tooltip);
    }
    if (tooltipText == null) return this;
    tooltip = new Tooltip(tooltipText);
    Tooltip.install(label,  tooltip);
    return this;
  }
  
  public Label node() {
    return label;
  }
}
