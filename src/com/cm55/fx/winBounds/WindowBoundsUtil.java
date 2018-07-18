package com.cm55.fx.winBounds;

import static java.lang.Math.*;

import javafx.geometry.*;
import javafx.stage.*;

public class WindowBoundsUtil {

  public static final double screenWidth;
  public static final double screenHeight;
  
  private static final int MIN_WIDTH = 100;
  private static final int MIN_HEIGHT = 100;
  
  static {
    Rectangle2D vb = Screen.getPrimary().getVisualBounds();
    screenWidth = vb.getWidth();
    screenHeight = vb.getHeight();
  }
  
  public static Rectangle2D fixWindowBounds(Rectangle2D rect) {
    double x = rect.getMinX();
    double y = rect.getMinY();
    double width = rect.getWidth();
    double height = rect.getHeight();
    
    x = max(x, 0);
    y = max(y, 0);
    width = max(width, MIN_WIDTH);
    width = min(width, screenWidth);
    height = max(height, MIN_HEIGHT);
    height = min(height, screenHeight);

    if (screenWidth < x + width) {
      x = screenWidth - width;
    }
    if (screenHeight < y + height) {
      y = screenHeight - height;
    }    
    return new Rectangle2D(x, y, width, height);
  }
}
