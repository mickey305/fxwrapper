import com.cm55.fx.*;

public class MainPanel {

  public MainPanel(FxStage stage) {
    FxHorBorderPane pane = new FxHorBorderPane(new FxLabel("A"), null, null);
    
    stage.setScene(new FxScene(pane)).show();
  }
}
