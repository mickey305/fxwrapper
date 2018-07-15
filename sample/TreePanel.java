import java.util.*;
import java.util.stream.*;

import com.cm55.fx.*;
import com.cm55.fx.FxTreeView.*;

import javafx.scene.layout.*;

public class TreePanel implements FxParent {

  FxTitledBorder titledBorder;
  FxTreeView<Mine> treeView;
  
  public TreePanel() {
    FxTreeView.Adapter<Mine>adapter = new FxTreeView.Adapter<Mine>() {

      @Override
      public String getLabel(Mine node) {
        return node.name;
      }

      @Override
      public Stream<Mine> children(Mine node) {
        return node.children.stream();        
      }      
    };
    
    treeView = new FxTreeView<Mine>(adapter);
    treeView.listen(ItemSelectionEvent.class, e-> {
      Mine mine = (Mine)e.item;
      System.out.println("" + mine.name);
      
    });
    
    Mine root = new Mine("a");
    for (int i = 1; i < 3; i++) {
      root.children.add(new Mine("b" + i));

    }  
    treeView.setRoot(root);
    titledBorder = new FxTitledBorder("test", treeView);
  }
  
  public Pane node() {
    return titledBorder.node();
  }

  public static class Mine {
    List<Mine>children = new ArrayList<>();
    public String name;
    public Mine(String name) {
      this.name = name;
    }
  }
}
