package com.cm55.fxlib;

import java.io.*;

import javafx.scene.*;
import javafx.stage.*;

public class FxDirectoryChooser {
  private DirectoryChooser dc = new DirectoryChooser();
  
  public FxDirectoryChooser setInitDir(String path) {
    if (path == null) return this;
    setInitDir(new File(path));
    return this;    
  }
  
  public FxDirectoryChooser setInitDir(File file) {
    if (file == null) return this;
    if (!file.exists()) return this;
    if (!file.isDirectory()) return this;
    dc.setInitialDirectory(file);
    return this;
  }
  
  public FxDirectoryChooser setTitle(String title) {
    dc.setTitle(title);
    return this;
  }
  
  public File showDialog(FxNode node) {
    return showDialog(node.node().getScene().getWindow());
  }
  
  public File showDialog(Window window) {
    return dc.showDialog(window);
  }
}
