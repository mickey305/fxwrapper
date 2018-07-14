package com.cm55.fx;

import java.util.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.util.*;

/**
 * TableViewに対して、指定したモデル上の行が表示されるように指示する。
 * <p>
 * 例えば、モデルが100行で、表示可能な行数が10行分で、現在表示されているのがモデル中のインデックス0..9の場合に、
 * 強制的にインデックス55を表示させたい場合に使用する。つまり、インデックス55が表示中の10行分のいずれかの行として表示される。
 * </p>
 * <p>
 * このクラスを作成した経緯は次の通り。Java8時代には、com.sun.javafx.scene.control.skin.VirtualContainerBaseを使用して、
 * TableView内部にアクセスし、指定行の強制表示ができたのだが、Java9になってから、連中が内部構造を変更したためこの方法が使えなくなってしまった。
 * もしかしたら、Java9でのJavaFXでの正式なやり方があるのかもしれないが、しかしそれを使ってしまうと、今度はJava8では動作しなくなる。
 * このため、Java8/9の両方で動作する苦肉の策としてこのコードを書いた。
 * </p>
 * <p>
 * これは以下のコードのほとんどまるコピーだが、自分にわかりやすいように修正している。
 * </p>
 * https://stackoverflow.com/questions/46474385/how-to-find-the-indices-of-the-visible-rows-in-a-tableview-in-javafx-9
 */
public class TableRowVisible<T> {

  private static final boolean DEBUG = true;

  /** ターゲットとする{@link TableView} */
  private final TableView<T> tableView;
  
  /** TableViewが使う表示行。モデルの行ではなく、表示上の行。例えば、モデルが100行でも、表示は10行分しかない場合には10しか作成されない */
  private LinkedHashSet<TableRow<T>> rows = new LinkedHashSet<>();

  /**
   * TableViewの行ファクトリをフックし、作成される表示行を保持しておく。
   * この行というのは、表示行数分しか作成されない。つまり、モデルに100行あっても、表示行数が10行分であれば、
   * 10つしか作成されない。
   */
  public TableRowVisible(TableView<T> tableView) {
    this.tableView = tableView;
    final Callback<TableView<T>, TableRow<T>> rowFactory = tableView.getRowFactory();
    tableView.setRowFactory(param -> {
      TableRow<T> row = rowFactory != null ? rowFactory.call(param) : new TableRow<T>();
      rows.add(row);
      return row;
    });
  }
  
  /**
   * 指定したモデル中行のインデックスを強制表示する。
   * @param index モデル中の行インデックス
   */
  public void makeVisible(int index) {
    
    // 可視のモデルインデックス範囲を求める。一行分さえ完全に表示されていない場合は何もしない
    VisibleRows visibleRows = this.getVisibleRows();
    if (visibleRows == null) return;
    
    // このモデルインデックスが既に表示中。何もしない
    if (visibleRows.first <= index && index <= visibleRows.last)
      return;

    // 下方向に一行だけスクロールし、斎場行として表示
    if (index == visibleRows.first - 1) {
      tableView.scrollTo(index);
      return;
    }

    // 上方向に一行スクロールして、最下行として表示
    if (visibleRows.last + 1 == index) {
      tableView.scrollTo(visibleRows.first + 1);
      return;
    }
    
    // その他の場合には、指定されたインデックスがなるべく表示行群の中心になるようにするが、しかし、三行分以上あること
    if (visibleRows.getCount() <= 2) {
      tableView.scrollTo(index);
      return;
    }
    
    tableView.scrollTo(Math.max(0, index - visibleRows.getCount() / 2));
  }

  /**
   * 表示中のモデル行範囲を求める。
   * 例えば、モデルが100行の場合、表示中の行は23から32かもしれない。
   * @return {@link VisibleRows}、あるいはフルの可視行が無い場合はnullを返す。
   */
  private VisibleRows getVisibleRows() {
    
    // 最初の可視インデックス
    Integer firstVisible = null;
    
    // 最後の可視インデックス
    Integer lastVisible = null;

    // TableView全体からヘッダ部を除いた高さを求める。この高さにTableRowが表示される。
    // ※TableViewに横方向スクロールバーが付加された場合は加味していない。どのようにやるべきか不明。
    double viewPortHeight = tableView.getHeight() - getTableHeaderRowHeight();

    // 表示行（モデル中の行ではない）を順に調べる
    for (TableRow<T> r : rows) {
      
      int modelIndex = r.getIndex();
      
      // 確保されたが使われていない表示行か、あるいはモデルインデックスの無い行は無視。
      // 例えば、いったんTableView表示領域が上下に拡大されて10行分のTableRowが確保されても、その後で縮小され4行分の表示領域しか
      // なくなった場合、6行分は非表示状態になっている。
      if (modelIndex < 0 || !r.visibleProperty().get()) continue;

      // 表示行の縦位置を取得
      double minY = r.getBoundsInParent().getMinY();
      double maxY = r.getBoundsInParent().getMaxY();
      
      // 完全に表示されているものに限定する
      if (!(0 <= minY && maxY <= viewPortHeight)) continue;
            
      // 最初と最後のモデルインデックスを取得する
      if (firstVisible == null || modelIndex < firstVisible) firstVisible = modelIndex;      
      if (lastVisible == null  || lastVisible < modelIndex)  lastVisible = modelIndex;      
    }

    if (firstVisible == null) return null;
    return new VisibleRows(firstVisible, lastVisible);
  }

  /**
   * TableViewのヘッダ部の高さを求める。
   * ヘッダ部ノードは、参考としたコードのような「tableView.lookup(".column-header-background")」では取得できなかったため、別の方法とした。
   * java8/9共に、ヘッダ部は"TableHeaderRow"という単純名を持つクラスであることに着目。もし見つからない場合には０を返す。
   * @return ヘッダ部の高さ
   */
  private double getTableHeaderRowHeight() {
    for (Node n: tableView.getChildrenUnmodifiable()) {
      if (n.getClass().getSimpleName().equals("TableHeaderRow")) return n.getBoundsInLocal().getHeight();     
    }
    return 0;
  }

  /** 可視行インデックス範囲。モデル中の最初の可視行インデックスと最後の可視行インデックス */
  static class VisibleRows {
    
    /** 最初の可視行モデルインデックス */
    final int first;
    
    /** 最後の可視行モデルインデックス */
    final int last;
    
    VisibleRows(int first, int last) {
      this.first = first;
      this.last = last;
    }
    
    /** 可視行数 */
    int getCount() {
      return last - first + 1;
    }
  }
}
