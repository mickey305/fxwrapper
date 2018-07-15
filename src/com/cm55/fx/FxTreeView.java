package com.cm55.fx;

import java.util.stream.*;

import com.cm55.eventBus.*;

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

  protected Adapter<T>adapter;
  protected TreeView<T> treeView;
  protected MultipleSelectionModel<TreeItem<T>> selectionModel;
  protected EventBus eventBus = new EventBus();
  
  public FxTreeView(Adapter<T>adapter) {
    this.adapter = adapter;
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
    treeView.setCellFactory(t->new MyTreeCell<T>(adapter));
  }
  
  public static class MyTreeCell<T> extends TreeCell<T> {
    private Adapter<T> adapter;
    public MyTreeCell(Adapter<T>adapter) {
      this.adapter = adapter;
    }
    @Override
    public void updateItem(T data, boolean empty){          
        super.updateItem(data, empty);
        System.out.println("" + data);
        if (data == null) return;
        if(empty){
            //空の場合は、ラベルもアイコンも表示させない
            setText(null);
            setGraphic(null);
        }else if(isEditing()){
          /*
            //編集時はLabeledTextにラベルを表示させない
            setText(null);
            setGraphic(this.graph);
            */
        } else {
            //通常の表示
            setText(adapter.getLabel(data));
            setGraphic(null);
        }
    }
  }
  
  public FxTreeView(Adapter<T>adapter, T root) {
    this(adapter);
    setRoot(root);
  }
  
  public FxTreeView<T> setRoot(T root) {
    TreeItem<T>treeItem = new Object() {
      TreeItem<T>getTreeItem(T node) {
        TreeItem<T>item = new TreeItem<T>(node);
        adapter.children(node).forEach(n-> {
          item.getChildren().add(new TreeItem<T>(n));
        });
        return item;
      }      
    }.getTreeItem(root);
    
    treeView.setRoot(treeItem);
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
  
  public interface Adapter<T> {
    public String getLabel(T node);
    public Stream<T>children(T node);
  }
}
