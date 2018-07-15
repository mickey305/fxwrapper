import com.cm55.fx.*;

public class MainPanel {

  public MainPanel(FxStage stage) {
    FxBorderPane.Hor pane = new FxBorderPane.Hor(new FxLabel("A"), null, null);
    
    stage.setScene(new FxScene(pane)).show();
  }
}
