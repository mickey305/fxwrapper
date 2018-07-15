import com.cm55.fx.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TreePanel implements FxParent {

  FxTitledBorder titledBorder;
  FxTreeView<Mine> treeView;
  
  public TreePanel() {
    treeView = new FxTreeView<>();
    TreeItem<Mine> rootItem = new TreeItem<> (new Mine());
    rootItem.setExpanded(true);
    for (int i = 1; i < 3; i++) {
      TreeItem<Mine> item = new TreeItem<> (new Mine());            
      rootItem.getChildren().add(item);
      
      {
        TreeItem<Mine>sub = new TreeItem<>(new Mine());
        item.getChildren().add(sub);
      }
    }  
    treeView.setRoot(rootItem);
    titledBorder = new FxTitledBorder("test", treeView);
  }
  
  public Pane node() {
    return titledBorder.node();
  }

  public static class Mine {
    
  }
}
