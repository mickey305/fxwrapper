package com.cm55.fx;


import java.util.*;

import com.cm55.fx.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class FxAlerts {
  
  // INFORM
  
  public static boolean inform(FxNode node, String message) {
    return inform(node, message, null);
  }
  
  public static boolean inform(FxNode node, String message, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.INFORMATION, message, ButtonType.OK));
    if (run != null) run.run(); 
    return true;
  }
  
  // CONFIRM ==========================================================================================================
  public static void confirmYes(FxNode node, String message, Runnable run) {
    confirm(node, message, ButtonType.YES, run);
  }
  
  public static boolean confirmYes(FxNode node, String message) {
    return confirm(node, message, ButtonType.YES, null);
  }
  
  public static boolean confirmNo(FxNode node, String message) {
    return confirm(node, message, ButtonType.NO, null);
  }
    
  public static boolean confirm(FxNode node, String message, ButtonType expect, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO));
    option.filter(response -> response == expect).ifPresent(response -> { if (run != null) run.run(); }); 
    return option.get() == expect;    
  }
  
  // OK/CANCEL =======================================================================================================
  
  public static void okCancelOk(FxNode node, String message, Runnable run) {
    okCancel(node, message, ButtonType.OK, run);
  }
  
  public static boolean okCancelOk(FxNode node, String message) {
    return okCancel(node, message, ButtonType.OK, null);
  }
  
  public static boolean okCancelCancel(FxNode node, String message) {
    return okCancel(node, message, ButtonType.CANCEL, null);
  }
    
  public static boolean okCancel(FxNode node, String message, ButtonType expect, Runnable run) {  
    Optional<ButtonType>option = showAndWait(node, 
        new Alert(AlertType.WARNING, message, ButtonType.OK, ButtonType.CANCEL));
    option.filter(response -> response == expect).ifPresent(response -> { if (run != null) run.run(); }); 
    return option.get() == expect;    
  }
  
  // ERROR/INFO/WARNING ==============================================================================================
  
  public static void error(FxNode node, String message) {
    showAndWait(node, new Alert(AlertType.ERROR, message, ButtonType.OK));
  }
  
  public static void info(FxNode node, String message) {
    showAndWait(node, new Alert(AlertType.INFORMATION, message, ButtonType.OK));
  }
  
  public static void warning(FxNode node, String message) {
    showAndWait(node, new Alert(AlertType.WARNING, message, ButtonType.OK));
  }

  // SHOW =============================================================================================================
  
  private static Optional<ButtonType>showAndWait(FxNode node, Alert alert) {
    if (node != null) {
      alert.initOwner(node.node().getScene().getWindow());
      new RelocateAroundNode.ForDialog(node, new FxDialog<ButtonType>(alert));
    }
    return alert.showAndWait();
  }
  
}
