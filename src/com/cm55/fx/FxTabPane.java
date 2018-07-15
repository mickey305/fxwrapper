package com.cm55.fx;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import com.cm55.eventBus.*;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.control.*;

/**
 * タブペイン
 * @author ysugimura
 */
public class FxTabPane implements FxParent {

  private TabPane tabPane;
  
  public static class ChangeEvent {
    FxNode oldNode;
    FxNode newNode;
    public ChangeEvent(FxNode oldNode, FxNode newNode) {
      this.oldNode = oldNode;
      this.newNode = newNode;
    }
    @SuppressWarnings("unchecked")
    public <T extends FxNode>T getOldNode() { return (T)oldNode; }
    @SuppressWarnings("unchecked")
    public <T extends FxNode>T getNewNode() { return (T)newNode; }
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ChangeEvent)) return false;
      ChangeEvent that = (ChangeEvent)o;
      return 
          this.oldNode == that.oldNode && 
          this.newNode == that.newNode;
    }
  }

  private EventBus eventBus = new EventBus();

  
  private HashMap<Node, FxNode>nodeMap = new HashMap<>();
  private Integer fixedIndex = null;
  /** なぜかイベントが二回発生してしまうため、古い方を覚えておく */
  private ChangeEvent oldChange;

  public FxTabPane() {
    tabPane = new TabPane();
  }
  
  @SuppressWarnings("unchecked")
  public <T extends FxNode>Stream<T>allNodes() {
    return tabPane.getTabs().stream().map(tab->(T)nodeMap.get(tab.getContent()));
  }
  
  /**
   * disable時はタブの選択ができないようにする。
   * {@link TabPane#setDisable(boolean)}や{@link Tab#setDisable(boolean)}ではだめ。
   * タブの中身全体がdisable表示になってしまう。
   * @param value
   * @return
   */
  public FxTabPane setEnabled(boolean value) {
    if (value == (fixedIndex == null)) return this;  
    if (value) 
      fixedIndex = null;
    else 
      fixedIndex = tabPane.getSelectionModel().getSelectedIndex();
    return this;
  }

  /** 選択中のノードを取得する */
  @SuppressWarnings("unchecked")
  public <T extends FxNode>T getSelectedNode() {
    Node node = tabPane.getSelectionModel().getSelectedItem().getContent();
    return (T)nodeMap.get(node);
  }
  
  public FxTabPane add(String title, FxNode node) {

    nodeMap.put(node.node(), node);
    Tab tab = new Tab();
    tab.setText(title);
    tab.setContent(node.node());
    tab.setClosable(false);
    tabPane.getTabs().add(tab);

    tabPane.getSelectionModel().selectedItemProperty().addListener(
      new ChangeListener<Tab>() {
        @Override
        public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
          if (fixedIndex != null) {
            Platform.runLater(()->tabPane.getSelectionModel().select(fixedIndex));
            return;
          }
          ChangeEvent newChange = new ChangeEvent(
            nodeMap.get(oldTab.getContent()),
            nodeMap.get(newTab.getContent())
          );
              
          // なぜかイベントが二回発生してしまうため、以前と同じなら無視
          if (newChange.equals(oldChange)) return;
          eventBus.dispatchEvent(oldChange = newChange);
        }
      }
    );
    return this;
  }
  
  /** {@link ChangeEvent}をリッスンする */
  public void listenChange(Consumer<ChangeEvent>o) {
    eventBus.listen(ChangeEvent.class,  o);
  }

  @Override
  public Parent node() {
    return tabPane;
  }
}
