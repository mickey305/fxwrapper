package com.cm55.fxlib;

import java.util.*;

import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.control.*;

public class FxTabPane extends TabPane {

  public static class ChangeEvent {
    Node oldNode;
    Object oldUser;
    Node newNode;
    Object newUser;
    public ChangeEvent(Node oldNode, Object oldUser, Node newNode, Object newUser) {
      this.oldNode = oldNode;
      this.oldUser = oldUser;
      this.newNode = newNode;
      this.newUser = newUser;
    }
    @SuppressWarnings("unchecked")
    public <T extends Node>T getOldNode() { return (T)oldNode; }
    @SuppressWarnings("unchecked")
    public <T extends Node>T getNewNode() { return (T)newNode; }
    @SuppressWarnings("unchecked")
    public <T>T getOldUser() { return (T)oldUser; }
    @SuppressWarnings("unchecked")
    public <T>T getNewUser() { return (T)newUser; }
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ChangeEvent)) return false;
      ChangeEvent that = (ChangeEvent)o;
      return 
          this.oldNode == that.oldNode && 
          this.oldUser == that.oldUser &&
          this.newNode == that.newNode &&
          this.newUser == that.newUser;
    }
  }

  private FxEventer eventer = new FxEventer();
  private FxEventType<ChangeEvent>CHANGE_EVENT = new FxEventType<>();
  private HashMap<Node, Object>userMap = new HashMap<Node, Object>();
  
  /** なぜかイベントが二回発生してしまうため、古い方を覚えておく */
  private ChangeEvent oldChange;
  
  public FxTabPane add(String title, Node node) {
    return add(title, node, null);
  }
  
  public FxTabPane add(String title, Node node, Object user) {
    userMap.put(node,  user);
    Tab tab = new Tab();
    tab.setText(title);
    tab.setContent(node);
    tab.setClosable(false);
    getTabs().add(tab);

    getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
              Node oldNode = oldTab.getContent();
              Node newNode = newTab.getContent();
              ChangeEvent newChange = new ChangeEvent(
                oldNode, userMap.get(oldNode),
                newNode, userMap.get(newNode)
              );
              
              // なぜかイベントが二回発生してしまうため、以前と同じなら無視
              if (newChange.equals(oldChange)) return;
              eventer.fire(CHANGE_EVENT, oldChange = newChange);
            }
        }
    );
    return this;
  }
  
  public void listenChange(FxCallback<ChangeEvent>o) {
    eventer.add(CHANGE_EVENT,  o);
  }

}
