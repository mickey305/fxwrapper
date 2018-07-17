package com.cm55.fx;

import static com.cm55.fx.FxLabel.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import com.cm55.fx.util.*;

import javafx.application.*;
import javafx.beans.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.util.converter.*;

/**
 * テーブル
 * 
 * <h2>テーブルの作成と列の指定</h2>
 * <p>
 * 型パラメータEには行として用いるモデルクラスを指定する。いまこれをSampleとする
 * </p>
 * <pre>
 * public class Sample {
 *   String name;
 *   int age;
 *   String address;
 * }
 * </pre>
 * <p>
 * 以下のようにテーブルを作成する
 * </p>
 * <pre>
 * FxTable<Sample>table = new FxTable<>();
 * <pre>
 * <p>
 * モデルからどのデータを取得し、どのような列を表示するかを指定する。
 * </p>
 * <pre>
 * table.setColumns(
 *   new FxTable.TextColumn<Sample>("列名称", row->FixedValue.w(row.name)),
 *   ...
 * );
 * </pre>
 * <p>
 * 列を表現するクラスとしては他種類あるが、上記のFxTable.TextColumnが最も簡単なものである。列名称と表示データを指定するだけである。
 * が、見てのとおり、表示データの指定は単純ではない。
 * </p>
 * <h2>テーブルには再表示動作が無い</h2>
 * <p>
 * 例えば、表示中の行データが変更された場合、Swing等では、その行の再表示を指示することになっているのだが、JavaFxではそうではない。
 * JavaFxでは、各セルのデータ値はObservableValueであり、テーブル中に表示されると、テーブル側がObservableValueからの変更通知を
 * 受けるようにセットアップされる。このObservableValueに新たな値を設定すると、それがテーブル側に通知され、表示が更新されるという仕組みになっている。
 * </p>
 * <p>
 * 先にあげた例の中では、「row->FixedValue.w(row.name)」としているが、これは特定の行データが与えられた場合に、そこからObservableValueを
 * 取得するということである。しかし、この例の場合にはSampleのnameは単純なStringであるので、ObservableValueではない。
 * </p>
 * <p>
 * この例の場合には、単純に固定した値として表示することにしている。つまり、nameに新たな値が設定されても表示上は変更されない。
 * </p>
 * <h2>自動再表示を行わせる方法</h2>
 * <p>
 * 自動再表示を行わせる一つの方法としては、Sample#nameをObservableにすることである。つまり、
 * </p>
 * <pre>
 * public class Sample {
 *   SimpleObjectProperty<String> name = new SimpleObjectProperty<>();
 * }
 * </pre>
 * 
 * @param <E>
 */
public class FxTable<E> implements FocusControl<FxTable<E>>, FxNode {

  private TableView<E> tableView;
  private FxObservableList<E> rows;
  private FxSingleSelectionModel<E> selectionModel;
  private TableRowVisible<E> tableRowVisible;
  private Consumer<FxTable<E>>focusedCallback;
  private boolean focusable = FocusControlPolicy.getDefaultFocusable();
  private ObservableList<TableColumn<E, ?>> columns;
  
  public FxTable() {
    tableView = new TableView<E>() {
      @Override public void requestFocus() {
        if (focusable) super.requestFocus();
      }
    };
    setRows(new FxObservableList<E>());
    selectionModel = new FxSingleSelectionModel<E>(tableView.getSelectionModel());
    tableView.setEditable(true);
    tableRowVisible = new TableRowVisible<E>(tableView);
    EventHandler<? super MouseEvent>handler = new EventHandler<MouseEvent> ()  {
      public void handle(MouseEvent event) {
        if (focusedCallback != null) {
          Platform.runLater(()-> {
            focusedCallback.accept(FxTable.this);
          });
        }
      }      
    };
        
    tableView.setOnMouseClicked(handler);
    columns = tableView.getColumns();
  }

  /** フォーカス可能かを設定 */
  @Override
  public FxTable<E>setFocusable(boolean value) {
    focusable = value;
    return this;
  }
  
  public FxTable<E> setFocusedCallback(Consumer<FxTable<E>>focusedCallback) {
    this.focusedCallback = focusedCallback;
    return this;
  }

  /** 列を設定 */
  @SafeVarargs
  public final FxTable<E> setColumns(Column<E, ?>... cols) {
    columns.clear();
    Set<String>textSet = new HashSet<String>();
    Arrays.stream(cols).forEach(col-> {
      String text = col.col.getText();
      if (textSet.contains(text)) throw new RuntimeException("duplicated text");
      textSet.add(text);
      columns.add(col.col);
    });
    return this;
  }

  public ColumnState[]getColumnStates() {
    return columns.stream()
      .map(c-> new ColumnState(c.getText(), c.getWidth()))
      .collect(Collectors.toList()).toArray(new ColumnState[0]);
  }
  
  public void setColumnStates(ColumnState[]states) {
    Map<String, TableColumn<E,?>>map = columns.stream().collect(Collectors.toMap(c-> c.getText(), c->c));
    columns.clear();
    Arrays.stream(states).forEach(state-> {      
      TableColumn<E,?>col = map.remove(state.text);
      if (col == null) return;
      col.setPrefWidth(state.width);
      columns.add(col);      
    });
    map.values().forEach(col->columns.add(col));
  }
  
  /*
  public double[]getColumnWidths() {
    double[]widths = new double[columns.size()];
    for (int i = 0; i < widths.length; i++) widths[i] = columns.get(i).getWidth();
    return widths;
  }
  
  public void setColumnPrefWidths(double[]widths) {
    int count = Math.min(widths.length, columns.size());
    for (int i = 0; i < count; i++) {
      columns.get(i).setPrefWidth(widths[i]);
    }
  }
  */
  
  /** 行リストを設定 */
  public FxTable<E> setRows(FxObservableList<E> rows) {
    tableView.itemsProperty().set(this.rows = rows);

    // 行UPDATEで再描画されないバグがある。
    rows.listenListChanged(e-> {
        /*
         * この方法は時間がかかりすぎるので不可
         * 対象フィールドをSimpleStringProperty等にして、そこに値を入れれば自動で更新される。 if
         * (!e.type.equals(ChangedType.UPDATE)) return; for (TableColumn<E, ?>
         * column : tableView.getColumns()) { column.setVisible(false);
         * column.setVisible(true); }
         */
      
    });
    return this;
  }

  /** コントロールを取得 */
  public TableView<E> getTableView() {
    return tableView;
  }
  
  /** 指定インデックス行を可視にする */
  public void makeVisible(int index) {
    tableRowVisible.makeVisible(index);
  }

  /** プレースホルダーを指定する */
  public FxTable<E> setPlaceholder(Node value) {
    tableView.setPlaceholder(value);
    return this;
  }

  /** 行リストモデルを取得 */
  public FxObservableList<E> getRows() {
    return rows;
  }

  /** 選択モデルを取得 */
  public FxSingleSelectionModel<E> getSelectionModel() {
    return selectionModel;
  }

  /** 現在の選択行を取得 */
  public E getSelection() {
    int index = selectionModel.getSelectedIndex();
    if (index < 0) return null;
    return getRows().get(index);
  }
  
  /** TableViewコントロールを取得  */
  public Control node() {
    return tableView;
  }

  public static class ColumnState {
    public final String text;
    public final double width;
    public ColumnState(String text, double width) {
      this.text = text;
      this.width = width;
    }
  }
  
  /*==================================================================================================================
   * 
   *  ドラッグドロップ
   * 
   *================================================================================================================*/

  private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

  public void setDragDropEnabled() {
    tableView.setRowFactory(tv -> {
      TableRow<E> row = new TableRow<>();

      row.setOnDragDetected(event -> {
        if (!row.isEmpty()) {
          Integer index = row.getIndex();
          Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
          db.setDragView(row.snapshot(null, null));
          ClipboardContent cc = new ClipboardContent();
          cc.put(SERIALIZED_MIME_TYPE, index);
          db.setContent(cc);
          event.consume();
        }
      });

      row.setOnDragOver(event -> {
        Dragboard db = event.getDragboard();
        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
          if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
          }
        }
      });

      row.setOnDragDropped(event -> {
        Dragboard db = event.getDragboard();
        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
          int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
          E draggedRow = tableView.getItems().remove(draggedIndex);

          int dropIndex;

          if (row.isEmpty()) {
            dropIndex = tableView.getItems().size();
          } else {
            dropIndex = row.getIndex();
          }

          tableView.getItems().add(dropIndex, draggedRow);

          event.setDropCompleted(true);
          tableView.getSelectionModel().select(dropIndex);
          event.consume();
        }
      });

      return row;
    });
  }
  
  /*==================================================================================================================
   * 
   * 列定義
   * 
   *================================================================================================================*/
  
  /**
   * 
   * @author admin
   *
   * @param <E>
   *          行タイプ
   * @param <T>
   *          列フィールドタイプ
   */
  public static class Column<E, T> {
    TableColumn<E, T> col;
    protected FxAlign align;
    
    public Column(String title, PropertyGetter<E, T>propertyGetter) {
      if (title == null)
        throw new NullPointerException();
      col = new TableColumn<E, T>(title);
      col.setSortable(false);
      if (propertyGetter != null) {
        col.setCellValueFactory(t->propertyGetter.get(t.getValue()));
      }
    }
    
    public Column(String title, PropertyGetter<E, T>propertyGetter, FxAlign align) {
      this(title, propertyGetter);
      setAlign(align);
    }

    public void setSortable(boolean value) {
      col.setSortable(value);
    }
    
    public TableColumn<E, T> getColumn() {
      return col;
    }

    public Column<E, T> setAlign(FxAlign align) {
      this.align = align;
      switch (align.ordinal()) {
      case 1:
        col.setStyle("-fx-alignment: CENTER;");
      case 2:
        col.setStyle("-fx-alignment: CENTER-RIGHT;");
        break;
      }
      return this;
    }

    public Column<E, T> setPrefWidth(int value) {
      col.setPrefWidth(value);
      return this;
    }

    /*
     * public Column<E, T> setGraphic(Node node) { col.setGraphic(node); return
     * this; }
     * 
     * public Column<E, T> setCellFactory(Callback<TableColumn<E, T>,
     * TableCell<E, T>> value) { col.setCellFactory(value); return this; }
     */
  }

  /** テキスト表示列 */
  public static class TextColumn<E> extends Column<E, String> {    
    
    public TextColumn(String title, PropertyGetter<E, String>propertyGetter) {
      this(title, propertyGetter, FxAlign.LEFT);
    }

    /** 列タイトル、フィールド名を指定する */
    public TextColumn(String title, PropertyGetter<E, String>propertyGetter, FxAlign align) {
      super(title, propertyGetter, align);
      setCellFactory();
    }
    
    private void setCellFactory() {
      col.setCellFactory(param -> {
        TableCell<E, String>cell = new TableCell<E, String>() {
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setText(null);
              return;
            }
            this.setText(item);
          }
        };
        //cell.setStyle("-fx-background-color: yellow");
        cell.setTextOverrun(OVERRUN_STYLES[align.ordinal()]);
        cell.setTextAlignment(TEXT_ALIGNMENTS[align.ordinal()]);
        return cell;
      });      
    }
  }
  
  public static class ColoredText {
    public String text;
    public Color foreground;
    public Color background;
    
    public ColoredText() {
      text = "";
    }
    public ColoredText(String text) {
      this.text = text;
    }
    
    @Override
    public String toString() {
      return text + "," + background + "," + foreground;
    }
  }
  
  /** テキスト表示列 */
  public static class ColoredTextColumn<E> extends Column<E, ColoredText> {    
    
    public ColoredTextColumn(String title, PropertyGetter<E, ColoredText>propertyGetter) {
      this(title, propertyGetter, FxAlign.LEFT);
    }

    /** 列タイトル、フィールド名を指定する */
    public ColoredTextColumn(String title, PropertyGetter<E, ColoredText>propertyGetter, FxAlign align) {
      super(title, propertyGetter, align);
      setCellFactory();
    }
    
    private void setCellFactory() {
      col.setCellFactory(param -> {
        TableCell<E, ColoredText>cell = new TableCell<E, ColoredText>() {
          public void updateItem(ColoredText item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setText(null);
              return;
            }
            if (item.background != null) {
              /* うまくいかない
              getStyleClass().add("custom");
              this.setStyle(
                "-fx-control-inner-background: #" + 
                item.background.toString().substring(2) + ";"
              );
              */              
              setStyle("-fx-background-color: #" + 
                item.background.toString().substring(2));              
            }            
            if (item.foreground != null) {
              throw new RuntimeException("not supported yet");
            }
            this.setText(item.text);
          }
        };
        //cell.setStyle("-fx-background-color: yellow");
        cell.setTextOverrun(OVERRUN_STYLES[align.ordinal()]);
        cell.setTextAlignment(TEXT_ALIGNMENTS[align.ordinal()]);
        return cell;
      });      
    }
  }
  
  
  /**
   * チェックボックス表示用
   * 
   * @author admin
   * @param <E>
   */
  public static class CheckColumn<E> extends Column<E, Boolean> {
    
    private Consumer<Integer> actionCallback;
    
    public CheckColumn(String title, PropertyGetter<E, Boolean>propertyGetter) {
      this(title, propertyGetter, FxAlign.LEFT);
    }

    /** 列タイトル、フィールド名を指定する */
    public CheckColumn(String title, PropertyGetter<E, Boolean>propertyGetter, FxAlign align) {
      super(title, propertyGetter, align);
      col.setCellFactory(column -> new TableCell<E, Boolean>() {
        public void updateItem(Boolean check, boolean empty) {
          super.updateItem(check, empty);
          if (check == null || empty) {
            setGraphic(null);
          } else {
            CheckBox box = new CheckBox() {
              @Override public void requestFocus() {}
            };
            BooleanProperty checked = (BooleanProperty) column.getCellObservableValue(getIndex());
            box.setSelected(checked.get());
            box.selectedProperty().bindBidirectional(checked);
            setGraphic(box);
            box.setOnAction(b-> {
              if (actionCallback != null) actionCallback.accept(getIndex());
            });
          }
        }
      });
    }
    
    public CheckColumn<E> setCallback(Consumer<Integer>callback) {
      actionCallback = callback;
      return this;
    }
  }

  /** ボタン列。単に列タイトルとボタンテキストを指定し、ボタンが押されたらアクションが起こるだけ */
  public static class ButtonColumn<E> extends Column<E, String> {

    public ButtonColumn(String title, String buttonText) {
      this(title, buttonText, null);
    }

    /** 列タイトル、フィールド名を指定する */
    public ButtonColumn(String title, String buttonText, Consumer<Integer>callback) {
      super(title, null);
      col.setCellFactory(param -> {
        TableCell<E, String> cell = new TableCell<E, String>() {          
          FxButton button = new FxButton(buttonText, b-> {
            callback.accept(getIndex());
          });
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  
            if (empty) {
              setGraphic(null);
              return;       
            }
            setGraphic(button.node());                
          }
        };        
        return cell;
      });
    }
  }
  
  /** Booleanフィールドの状態によって有効・無効を変更するボタン列。
   * それ以外は{@link ButtonColumn}と同じ。 */
  public static class EnabledButtonColumn<E> extends Column<E, Boolean> {

    public EnabledButtonColumn(String title, PropertyGetter<E, Boolean>propertyGetter, String buttonText) {
      this(title, propertyGetter, buttonText, null);
    }

    /** 列タイトル、フィールド名を指定する */
    public EnabledButtonColumn(String title, PropertyGetter<E, Boolean>propertyGetter, String buttonText, Consumer<Integer>callback) {
      super(title, propertyGetter);
      col.setCellFactory(column -> new TableCell<E, Boolean>() {          
        FxButton button = new FxButton(buttonText, b-> {
          callback.accept(getIndex()); 
        });
        @Override
        public void updateItem(Boolean item, boolean empty) {
          super.updateItem(item, empty);
          setText(null);  
          if (empty) {
            setGraphic(null);
            return;       
          }
          BooleanProperty enabled = (BooleanProperty) column.getCellObservableValue(getIndex());
          button.setEnabled(enabled.get());
          enabledChanged(button, enabled.get());
          setGraphic(button.node());                
        }        
      });
    }
    
    protected void enabledChanged(FxButton button, boolean enabled) {      
    }
  }

  /** コンボボックス列 */
  public static class ComboBoxColumn<E, T> extends Column<E, T> {

    public ComboBoxColumn(String title, PropertyGetter<E, T>propertyGetter, FxSelections<T>selections) {
      this(title, propertyGetter, selections, null);
    }

    /** 列タイトル、フィールド名を指定する */
    public ComboBoxColumn(String title, PropertyGetter<E, T>propertyGetter, FxSelections<T>selections, ComboBoxColumnEvent<T> e) {
      super(title, propertyGetter);
      col.setCellFactory(column-> new TableCell<E, T>() {
        @Override
        public void updateItem(T item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setGraphic(null);
            setText(null);
            return;
          }
          SimpleObjectProperty<T> wrapper = (SimpleObjectProperty<T>)column.getCellObservableValue(getIndex());
          //ystem.out.println("" + wrapper.getClass());
          FxComboBox<T> combo = new FxComboBox<T>(selections);
          combo.setSelection(wrapper.getValue());
          combo.setSelectionCallback(value-> {
            wrapper.set(value);
            if (e != null) e.selected(getIndex(), value);
          });
          setGraphic(combo.node());
          setText(null);            
        }          
      });
    }
  }
  
  public interface ComboBoxColumnEvent<T> {
    public void selected(int index, T value);
  }
  
  /** テキストフィールド列　*/
  public static class TextFieldColumn<E> extends Column<E, String> {
    public TextFieldColumn(String title, PropertyGetter<E, String> propertyGetter) {
      super(title, propertyGetter);
      col.setCellFactory(p-> new TextFieldTableCell<E, String>(new DefaultStringConverter()));
    }    
  }

  public interface PropertyGetter<E, T> {
    public ObservableValue<T> get(E e);
  }
  
  public static class FixedValue<T> implements ObservableValue<T>{
    
    public static <T>FixedValue<T> w(T value) {
      return new FixedValue<T>(value);
    }
    
    T value;
    public FixedValue(T value) {
      this.value = value;
    }
    public T getValue() {
      return value;
    }
    public void addListener(InvalidationListener listener) {}
    public void removeListener(InvalidationListener listener) {}
    public void addListener(ChangeListener<? super T> listener) {}
    public void removeListener(ChangeListener<? super T> listener) {}
  }

  
  /**
   * 列幅参考 col1.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
   * col2.prefWidthProperty().bind(table.widthProperty().multiply(0.7));
   */
}
