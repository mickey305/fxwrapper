

import javafx.application.*;
import javafx.stage.*;


public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    
    // フォントの色がおかしくなる問題に対処
    System.setProperty( "prism.lcdtext" , "false" );

    
    // コマンドラインパラメータを取得
    Parameters params = getParameters();
    
    // メニューパネルの実行
 //   injector.getInstance(MenuPanel.class).execute(params, getHostServices(), stage);
  }
  
  public static void main(String[] args) {  
    
    // JavaFXアプリのラウンチ、コマンドラインパラメータを引き渡す
    Application.launch(Main.class, args);
  }
}
