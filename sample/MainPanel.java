import com.cm55.fx.*;

public class MainPanel {

  public MainPanel(FxStage stage) {
    
    FxTabPane tabs = new FxTabPane();
    tabs.add("ツリーテスト", new TreePanel());    
    stage.setScene(new FxScene(tabs)).show();
  }
}
