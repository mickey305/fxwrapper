package com.cm55.fxlib;

import java.util.function.*;

import org.fxmisc.flowless.*;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;
import org.fxmisc.richtext.model.TwoDimensional.*;

import com.cm55.fxlib.richTextArea.*;

import javafx.beans.value.*;
import javafx.css.*;
import javafx.scene.layout.*;

/**
 * RichTextAreaはタイマで何かをやっているため、managed=falseとしなくてはいけない。
 * どこかにaddする場合はgetRegion()を取得すること。
 * @author admin
 */
public class FxRichTextArea implements FxNode {

  private StyleClassedTextArea textArea;
  private LineNumbers lineNumber;
  @SuppressWarnings("rawtypes")
  private VirtualizedScrollPane scrollPane;
  private FxUnmanageChild unmanageChild;
  private Consumer<String>textChangedCallback;
  private Consumer<Integer>caretPositionCallback;
  private Consumer<FxRichTextArea>pureScrollCallback;
  private boolean textUpdating;
  private PureScrolling pureScrolling = new PureScrolling();
  
  public FxRichTextArea() {
    createTextArea();
    createScrollPane();
    unmanageChild = new FxUnmanageChild(scrollPane);
  }

  /**
   * 
   * @return
   */
  public Region getRegion() {
    return unmanageChild;
  }
  
  /** フォントサイズを設定する */
  public FxRichTextArea setFontSize(int size) {
    textArea.setStyle(String.format("-fx-font-size: %d;", size));    
    return this;
  }
  
  /** キャレットの位置を指定する */
  public FxRichTextArea setCaretPosition(int offset) {
    textArea.moveTo(offset);
    return this;
  }
  
  public void setTextSelected(int start, int end) {
    textArea.selectRange(start,  end);
  }
  
  /** キャレットの位置変更コールバックを受ける */
  public FxRichTextArea setCaretPositionCallback(Consumer<Integer>callback) {
    this.caretPositionCallback = callback;    
    return this;
  }
  
  /** テキスト変更通知を受ける */
  public FxRichTextArea setTextChangedCallback(Consumer<String>callback) {
    this.textChangedCallback = callback;
    return this;
  }

  /** キャレットの動きによらないピュアなスクロールコールバックを設定する */
  public FxRichTextArea setPureScrollCallback(Consumer<FxRichTextArea>callback) {
    this.pureScrollCallback = callback;
    return this;
  }
  
  public String getText() {
    return textArea.getText();
  }
  
  public int getVisibleCenter() {
    return lineNumber.getVisibleRange().center;
  }
  
  private CenterScroller currentCenterScroller;
  
  /** 指定行をセンターにするようスクロールする */
  public void makeLineCentered(int targetIndex) {
    if (currentCenterScroller != null) return;
    currentCenterScroller = new CenterScroller(this.textArea, this.lineNumber, targetIndex);
    currentCenterScroller.start(()-> {
      currentCenterScroller = null;
    });
  }

  
  public StyleClassedTextArea getTextArea() {
    return textArea;
  }
  
  public StyleClassedTextArea node() {
    return textArea;
  }
  
  
  @SuppressWarnings("rawtypes")
  public VirtualizedScrollPane getScrollPane() {
    return scrollPane;
  }
  
  public FxRichTextArea setEditable(boolean value) {
    textArea.setEditable(value);
    return this;
  }
  
  /** 
   * テキストを設定する。
   * UNDO履歴を忘れる。 */
  public void setText(String text) {
    textUpdating = true;
    try {
      textArea.clear();
      textArea.replaceText(0, 0, text);
      textArea.getUndoManager().forgetHistory();
    } finally {
      textUpdating = false;
    }
  }
  
  /**
   * テキスト領域を作成する
   */
  private void createTextArea() {
    textArea = new StyleClassedTextArea();    
    textArea.setParagraphGraphicFactory(lineNumber = new LineNumbers(textArea));
    textArea.wrapTextProperty().set(true);    
    textArea.caretPositionProperty().addListener(position -> {
      pureScrolling.caretDetect();
      if (caretPositionCallback == null) return;
      caretPositionCallback.accept(getCaretLine());      
    });
    textArea.textProperty().addListener(new ChangeListener<String>() {
      public void changed(final ObservableValue<? extends String> observableValue, final String oldValue,
          final String newValue) {
        if (textUpdating) return;
        if (textChangedCallback != null) textChangedCallback.accept(newValue);
      }
    });  
    
    // これに加えてスタイルシートの変更も必要
    PseudoClass FOCUSED = PseudoClass.getPseudoClass("focused");
    textArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
        scrollPane.pseudoClassStateChanged(FOCUSED, newVal);
    });
    
  }

  /** スクロールペインを作成する */
  @SuppressWarnings("unchecked")
  private void createScrollPane() {
    scrollPane = new VirtualizedScrollPane<>(textArea);      
    scrollPane.estimatedScrollYProperty().addListener((ob, o, n)-> {
      if (!pureScrolling.isPureScrolling()) return;
      if (this.pureScrollCallback != null) pureScrollCallback.accept(FxRichTextArea.this);
    });    
  }
  
  /** キャレット位置を取得する */
  public int getCaretLine() {
    int offset = textArea.getCaretPosition();
    Position p = textArea.offsetToPosition(offset, TwoDimensional.Bias.Forward);
    return p.getMajor();
  }

}
