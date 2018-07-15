package com.cm55.fx;

import com.cm55.eventBus.*;

import javafx.beans.*;
import javafx.beans.value.*;
import javafx.scene.control.*;

/**
 * ツリービュー
 * <p>
 * デフォルトの選択モードはSINGLEになっている。
 * </p>
 * @author ysugimura
 *
 * @param <T>
 */
public class FxTreeView<T> implements FxParent {

  protected TreeView<T> treeView;
  protected MultipleSelectionModel<TreeItem<T>> selectionModel;
  protected EventBus eventBus = new EventBus();
  
  public FxTreeView() {
    treeView = new TreeView<>();
    selectionModel = treeView.getSelectionModel();
    selectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<T>>() {
      @Override
      public void changed(ObservableValue<? extends TreeItem<T>> observable, TreeItem<T> oldValue,
          TreeItem<T> newValue) {
        TreeItem<T> selectedItem = newValue;
        System.out.println("" + selectedItem);
      }
    });
    //treeView.setCellFactory(t->new TreeCell());

  }
  
  public FxTreeView(TreeItem<T>root) {
    this();
    setRoot(root);
  }
  
  public FxTreeView<T> setRoot(TreeItem<T>root) {
    treeView.setRoot(root);
    return this;
  }
  

  public TreeView<T> node() {
    return treeView;
  }
  
  public static class ItemSelectionEvent<T> {
    public final TreeItem<T>item;
    public ItemSelectionEvent(TreeItem<T>item) {
      this.item = item;
    }
  }
}
