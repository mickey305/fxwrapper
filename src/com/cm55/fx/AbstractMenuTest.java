package com.cm55.fx;

import java.util.*;
import java.util.stream.*;

import org.junit.*;
import org.testfx.framework.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.cm55.eventBus.*;
import com.cm55.fx.AbstractMenu.*;

import javafx.scene.control.*;

public class AbstractMenuTest extends ApplicationTest {

  
  @Test
  public void test1() {
    MenuCreator<TreeNode> c = new MenuCreator<>(
      new Adapter() {
        public MenuItemKind getKind(TreeNode object) {
          return object.children.length == 0? MenuItemKind.LEAF:MenuItemKind.BRANCH;
        }        
      },
      new EventBus()
    );
    ContextMenu contextMenu = c.createContextMenu(new TreeNode("", 
      new TreeNode("com", 
        new TreeNode("cm55")
      )    
    ));
    assertThat(contextMenu.getItems().size(), equalTo(1));
    Menu comMenu = (Menu)contextMenu.getItems().get(0);
    assertThat(comMenu.getText(), equalTo("com"));
    assertThat(comMenu.getItems().size(), equalTo(1));
    MenuItem cm55Item = comMenu.getItems().get(0);
    assertThat(cm55Item.getText(), equalTo("cm55"));
  }
  
  @Test
  public void test2() {
    MenuCreator<TreeNode> c = new MenuCreator<>(
      new Adapter() {
        public MenuItemKind getKind(TreeNode object) {
          return object.children.length == 0? MenuItemKind.LEAF:MenuItemKind.SELECTABLE_BRANCH;
        }        
      },
      new EventBus()
    );
    ContextMenu contextMenu = c.createContextMenu(new TreeNode("", 
      new TreeNode("com", 
        new TreeNode("cm55")
      )    
    ));
    assertThat(contextMenu.getItems().size(), equalTo(1));
    Menu comMenu = (Menu)contextMenu.getItems().get(0);
    assertThat(comMenu.getText(), equalTo("com"));
    assertThat(comMenu.getItems().size(), equalTo(3));
    MenuItem comItem = comMenu.getItems().get(0); 
    assertThat(comItem.getText(), equalTo("com"));
    SeparatorMenuItem sepItem = (SeparatorMenuItem)comMenu.getItems().get(1);
    MenuItem cm55Item = comMenu.getItems().get(2);
    assertThat(cm55Item.getText(), equalTo("cm55"));
  }

  public abstract static class Adapter implements AbstractMenu.Adapter<TreeNode> {
    public String getLabel(TreeNode object) {
      return object.name;
    }
    public Stream<TreeNode> children(TreeNode object) {
      return Arrays.stream(object.children);
    }
    
  }
  
  public static class TreeNode {
    final String name;
    final TreeNode[]children;
    TreeNode(String name, TreeNode...children) {
      this.name = name;
      this.children = children;
    }
  }
}
