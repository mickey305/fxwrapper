package com.cm55.fxlib;


import java.util.*;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class FxAlerts {
  
  // INFORM
  
  public static boolean inform(Node node, String message) {
    return inform(node, message, null);
  }
  
  public static boolean inform(Node node, String message, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.INFORMATION, message, ButtonType.OK));
    if (run != null) run.run(); 
    return true;
  }
  
  // CONFIRM ==========================================================================================================
  public static void confirmYes(Node node, String message, Runnable run) {
    confirm(node, message, ButtonType.YES, run);
  }
  
  public static boolean confirmYes(Node node, String message) {
    return confirm(node, message, ButtonType.YES, null);
  }
  
  public static boolean confirmNo(Node node, String message) {
    return confirm(node, message, ButtonType.NO, null);
  }
    
  public static boolean confirm(Node node, String message, ButtonType expect, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO));
    option.filter(response -> response == expect).ifPresent(response -> { if (run != null) run.run(); }); 
    return option.get() == expect;    
  }
  
  // OK/CANCEL =======================================================================================================
  
  public static void okCancelOk(Node node, String message, Runnable run) {
    okCancel(node, message, ButtonType.OK, run);
  }
  
  public static boolean okCancelOk(Node node, String message) {
    return okCancel(node, message, ButtonType.OK, null);
  }
  
  public static boolean okCancelCancel(Node node, String message) {
    return okCancel(node, message, ButtonType.CANCEL, null);
  }
    
  public static boolean okCancel(Node node, String message, ButtonType expect, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.WARNING, message, ButtonType.OK, ButtonType.CANCEL));
    option.filter(response -> response == expect).ifPresent(response -> { if (run != null) run.run(); }); 
    return option.get() == expect;    
  }
  
  // ERROR/INFO/WARNING ==============================================================================================
  
  public static void error(Node node, String message) {
    showAndWait(node, new Alert(AlertType.ERROR, message, ButtonType.OK));
  }
  
  public static void info(Node node, String message) {
    showAndWait(node, new Alert(AlertType.INFORMATION, message, ButtonType.OK));
  }
  
  public static void warning(Node node, String message) {
    showAndWait(node, new Alert(AlertType.WARNING, message, ButtonType.OK));
  }

  // SHOW =============================================================================================================
  
  private static Optional<ButtonType>showAndWait(Node node, Alert alert) {
    if (node != null) {
      alert.initOwner(node.getScene().getWindow());
      new RelocateAroundNode.ForDialog(node, alert);
    }
    return alert.showAndWait();
  }
  
}
