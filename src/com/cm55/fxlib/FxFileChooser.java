package com.cm55.fxlib;

import java.io.*;

import javafx.scene.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;

public class FxFileChooser {
  private FileChooser fc = new FileChooser();
  
  public FxFileChooser() {
  }
  
  public FxFileChooser setTitle(String title) {
    fc.setTitle(title);
    return this;
  }
  
  public FxFileChooser setInitDir(String path) {
    if (path == null) return this;
    return setInitDir(new File(path));
  }
  
  public FxFileChooser setInitDir(File file) {
    if (file == null) return this;
    if (!file.exists()) return this;
    if (!file.isDirectory()) return this;
    fc.setInitialDirectory(file);
    return this;
  }

  public FxFileChooser setInitName(String name) {
    fc.setInitialFileName(name);
    return this;
  }
  
  public FxFileChooser setInitFile(File file) {
    if (file == null) return this;
    File dir = file.getParentFile();
    setInitDir(dir.getAbsolutePath());
    setInitName(file.getName());
    return this;
  }
  
  public FxFileChooser setInitFile(String path) {
    if (path == null) return this;
    setInitFile(new File(path));
    return this;
  }
  
  /** 対象拡張子を指定、ドット無し */
  public FxFileChooser addExtensions(String...exts) {
    for (String ext: exts) {
      addExtensionFilters(new ExtensionFilter(ext, "*." + ext));      
    }
    return this;
  }
  
  public FxFileChooser addExtensionFilters(ExtensionFilter...filters) {
    fc.getExtensionFilters().addAll(filters);
    return this;
  }
  
  public File showSaveDialog(FxNode node) {
    Window window = null;
    if (node != null) window = node.node().getScene().getWindow();
    return showSaveDialog(window);
  }
  
  public File showOpenDialog() {
    return showOpenDialog((Window)null);
  }
  
  public File showOpenDialog(Node node) {
    Window window = null;
    if (node != null) window = node.getScene().getWindow();
    return showOpenDialog(window);
  }
  
  public File showSaveDialog(Window window) {
    return fc.showSaveDialog(window);
  }
  
  public File showOpenDialog(Window window) {
    return fc.showOpenDialog(window);
  }
}
