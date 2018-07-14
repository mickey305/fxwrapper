

import com.cm55.fxlib.*;

import javafx.application.*;
import javafx.stage.*;


public class AppMain extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    
    // フォントの色がおかしくなる問題に対処
    System.setProperty( "prism.lcdtext" , "false" );

    
    // コマンドラインパラメータを取得
    Parameters params = getParameters();
    
    // メインパネルの実行
    new MainPanel(new FxStage(stage));
  }
  
  public static void main(String[] args) {  
    
    // JavaFXアプリのラウンチ、コマンドラインパラメータを引き渡す
    Application.launch(AppMain.class, args);
  }
}
