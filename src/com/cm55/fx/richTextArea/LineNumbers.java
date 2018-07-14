package com.cm55.fx.richTextArea;

import java.util.*;
import java.util.function.*;

import org.fxmisc.richtext.*;
import org.reactfx.collection.*;
import org.reactfx.value.*;

import javafx.beans.*;
import javafx.beans.Observable;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

/**
 * Graphic factory that produces labels containing line numbers. To customize
 * appearance, use {@code .lineno} style class in CSS stylesheets.
 */
public class LineNumbers implements IntFunction<Node> {

  private static final Insets DEFAULT_INSETS = new Insets(0.0, 5.0, 0.0, 5.0);
  private static final Paint DEFAULT_TEXT_FILL = Color.web("#666");
  private static final Font DEFAULT_FONT = Font.font("monospace", FontPosture.ITALIC, 13);
  private static final Background DEFAULT_BACKGROUND = new Background(
      new BackgroundFill(Color.web("#ddd"), null, null));

  public LineNumbers(StyledTextArea<?, ?> area) {
    this(area, digits -> "%0" + digits + "d");
  }

  private final Val<Integer> nParagraphs;
  private final IntFunction<String> format;

  public LineNumbers(StyledTextArea<?, ?> area, IntFunction<String> format) {
    nParagraphs = LiveList.sizeOf(area.getParagraphs());
    this.format = format;
  }

  @Override
  public Node apply(int idx) {
    Val<String> formatted = nParagraphs.map(n -> format(idx + 1, n));

    Label lineNo = new Label();
    lineNo.setFont(DEFAULT_FONT);
    lineNo.setBackground(DEFAULT_BACKGROUND);
    lineNo.setTextFill(DEFAULT_TEXT_FILL);
    lineNo.setPadding(DEFAULT_INSETS);
    lineNo.getStyleClass().add("lineno");

    // bind label's text to a Val that stops observing area's paragraphs
    // when lineNo is removed from scene
    lineNo.textProperty().bind(formatted.conditionOnShowing(lineNo));

    add(idx);
    lineNo.textProperty().addListener(new InvalidationListener() {
      boolean created = true;
      public void invalidated(Observable observable) {
        if (created)
          created = false;
        else
          remove(idx);
      }
    });

    return lineNo;
  }

  private String format(int x, int max) {
    int digits = (int) Math.floor(Math.log10(max)) + 1;
    return String.format(format.apply(digits), x);
  }
  
  private Set<Integer>lines = new HashSet<Integer>();
  private VisibleRange range = null;
  
  private void add(int idx) {
    if (idx == 0) return;
    //ystem.out.println("add " + idx);
    lines.add(idx);
    range = null;
  }
  
  private void remove(int idx) {
    //ystem.out.println("remove " + idx);
    lines.remove(idx);
    range = null;
  }
  
  static class VisibleRangeBuilder {
    private int min = Integer.MAX_VALUE;
    private int max = 0;
    private void check(int value) {
      min = Math.min(min, value);
      max = Math.max(max,  value);
    }
    private VisibleRange build() {
      return new VisibleRange(min, max);
    }
  }
  
  public VisibleRange getVisibleRange() {
    if (range != null) return range;
    VisibleRangeBuilder builder = new VisibleRangeBuilder();
    lines.stream().forEach(idx->builder.check(idx));
    //if (builder.min == 1) builder.min = 0;
    return builder.build();
  }
  
}
