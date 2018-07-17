package com.cm55.fx;

import java.io.*;

import javafx.stage.*;

/**
 * ディレクトリを選択するダイアログ
 * @author ysugimura
 */
public class FxDirectoryChooser {
  private DirectoryChooser dc = new DirectoryChooser();

  /** 初期パスを指定する。なくても可 */
  public FxDirectoryChooser setInitDir(String path) {
    if (path == null) return this;
    setInitDir(new File(path));
    return this;    
  }

  /** 初期パスを指定する。なくても可 */
  public FxDirectoryChooser setInitDir(File file) {
    if (file == null) return this;
    if (!file.exists()) return this;
    if (!file.isDirectory()) return this;
    dc.setInitialDirectory(file);
    return this;
  }

  /** ダイアログのタイトルを指定する */
  public FxDirectoryChooser setTitle(String title) {
    dc.setTitle(title);
    return this;
  }

  /** 
   * 親とするノードを指定して表示する。
   * 指定されれた場合は{@link File}が返され、キャンセルされたときはnullが買える。
   * @param node
   * @return
   */
  public File showDialog(FxNode node) {
    return showDialog(node.node().getScene().getWindow());
  }
  
  public File showDialog(Window window) {
    return dc.showDialog(window);
  }
}
