package com.cm55.fx;

import org.junit.*;
import org.testfx.framework.junit.*;

import javafx.scene.control.*;

public class TestfxTest extends ApplicationTest {

  @Test
  public void test() {
    ContextMenu menu = new ContextMenu();
    menu.getItems().add(new MenuItem("a"));
  }
}
