package com.cm55.fxlib.splitPane;

import javafx.scene.input.*;

public interface ViewMouseHandler {
  public void pressed(MouseEvent me);
  public void dragged(MouseEvent me);
  public void released(MouseEvent me);
}
