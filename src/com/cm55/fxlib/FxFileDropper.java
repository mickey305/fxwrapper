package com.cm55.fxlib;

import java.io.*;
import java.util.*;

import javafx.event.*;
import javafx.scene.input.*;

/**
 * 
 * @author admin
 */
public abstract class FxFileDropper {

  private FxNode node;
  
  public FxFileDropper(FxNode node) {
    this.node = node;
    node.node().setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
          event.acceptTransferModes(TransferMode.COPY);
        } else {
          event.consume();
        }
      }
    });

    // Dropping over surface
    node.node().setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        try {
          if (!db.hasFiles()) return;
          List<File>fileList = new ArrayList<File>();
          for (File file : db.getFiles()) {           
            fileList.add(file);
          }          
          success = true;
          dropped(fileList);
        } finally {
          event.setDropCompleted(success);
          event.consume();
        }
      }
    });
  }

  protected void dropped(List<File>fileList) {
    if (fileList.size() > 0) dropped(fileList.get(0));
    
    // 外からドロップするとフォーカスが失われてしまう。
    node.node().getScene().getWindow().requestFocus();
  }
  
  protected void dropped(File file) {
    
  }
}
