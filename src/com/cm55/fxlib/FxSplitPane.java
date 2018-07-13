package com.cm55.fxlib;

import java.util.*;
import java.util.stream.*;

import com.cm55.fxlib.splitPane.*;
import com.cm55.fxlib.splitPane.Divider.*;
import com.cm55.fxlib.splitPane.OrientationAdapter.*;
//Java9コンパイラはエラーを出すが使用可能
import com.sun.javafx.collections.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class FxSplitPane implements FxNode {
  
  public static class Hor extends FxSplitPane {
    public Hor(Node... nodes) {
      super(true, nodes);
    }
  }
  
  public static class Ver extends FxSplitPane {
    public Ver(Node... nodes) {
      super(false, nodes);
    }
  }
  
  public static final String SPLITTED_PART = "splitted-part";
  
  private SplitPaneView pane;
  private boolean horizontal;
  private ObservableList<Node> paneChildren;
  
  private Part[]allParts;
  
  /** 表示中のパートリスト 。visibleParts.size() == dividers.size() + 1という関係がある。 */
  private List<Part> visibleParts;
  
  /** 使用中のディバイダリスト 。visibleParts.size() == dividers.size() + 1という関係がある。*/
  @SuppressWarnings({ "unchecked", "restriction", "rawtypes" })
  private ObservableList<Divider> dividers = new ObservableListWrapper(new ArrayList<>());
  
  private WholeLayouter wholeLayouter;
  
  private SimpleIntegerProperty dividerThickness = new SimpleIntegerProperty();
  private SimpleIntegerProperty spacing = new SimpleIntegerProperty();  
  private OrientationAdapter adapter;
  private boolean tracking;
  
  public FxSplitPane(boolean horizontal, Node... nodes) {
    dividerThickness.set(7);
    this.horizontal = horizontal;

    visibleParts = new ArrayList<Part>();
    IntStream.range(0, nodes.length).forEach(i->visibleParts.add(ensurePart(i, nodes[i])));    
    allParts = visibleParts.toArray(new Part[0]);
    IntStream.range(0, allParts.length - 1).forEach(i->dividers.add(createDivider())); 
    
    pane = new SplitPaneView(horizontal, visibleParts, dividers, dividerThickness);
    paneChildren = pane.getChildren();
    
    visibleParts.forEach(part-> paneChildren.add(part.node));
    dividers.forEach(divider->paneChildren.add(divider));

    if (horizontal) adapter = new HorizontalAdapter(pane, dividers, dividerThickness, spacing);
    else            adapter = new VerticalAdapter(pane, dividers, dividerThickness, spacing);
    pane.setWholeLayouter(wholeLayouter = new WholeLayouter(adapter, visibleParts));
  }

  public FxSplitPane setTracking(boolean value) {
    tracking = value;
    wholeLayouter.setTracking(true);
    return this;
  }
  
  /**
   * {@link Part}があることを確認し、それを返す。
   * @param node
   * @return
   */
  private Part ensurePart(int index, Node node) {
    Part part = (Part) node.getProperties().get(SPLITTED_PART);
    if (part == null) {
      part = new Part(index, node);
      node.getProperties().put(SPLITTED_PART, part);
    } else {
      part.order = index;
    }
    return part;
  }
  
  public class Element {
    private Part part;
    private boolean visible = true;
    private Element(Part part) {
      this.part = part;
    }
    public void setVisible(boolean value) {
      if (visible == value) return;
      visible = value;
      if (visible) showElement(this);
      else hideElement(this);
    }
    public void bind(BooleanProperty property) {
      setVisible(property.get());
      property.addListener((ChangeListener<Boolean>)(ob, o, n)->setVisible(n));      
    }
  }
  
  private Element[]elements;
  
  public Element[]getElements() {
    if (elements == null) {
      elements = Arrays.stream(allParts).map(part->new Element(part))
        .collect(Collectors.toList()).toArray(new Element[0]);
    }
    return elements;
  }
  
  /** Paneを取得する */
  public Pane getPane() {
    return pane;
  }

  public Pane node() {
    return pane;
  }
  
  /** スペーシングを取得する */
  public int getSpacing() {
    return spacing.get();
  }

  /** スペーシングを設定する */
  public void setSpacing(int value) {
    spacing.set(value);
    pane.requestLayout();
  }
  
  /** ディバイダのサイズを取得する */
  public int getDividerThickness() {
    return dividerThickness.get();
  }
  
  /** ディバイダのサイズを設定する */
  public void setDividerThickness(int value) {
    dividerThickness.set(value);
    this.pane.requestLayout();
  }
    
  /** 現在のパーツの各ピクセルサイズを配列で返す。レイアウトされているとは限らないため、いずれかがnullであればnullを返す */
  public double[]getPartSizes() {
    try {
      return Arrays.stream(allParts).mapToDouble(p->p.layoutSize).toArray();
    } catch (NullPointerException ex) {
      return null;
    }
  }
  
  /** 現在のパーツ用のピクセルサイズを設定する */
  public void setPartSizes(double[]sizes) {   
    if (tracking) System.out.println("setPartSizes targetAvail " + adapter.targetAvail);
    Arrays.stream(allParts).forEach(p-> {      
      p.layoutSize = null;
      p.layoutRatio = null;
    });
    int count = Math.min(sizes.length, allParts.length);
    for (int i = 0; i < count; i++) {
      Part part = allParts[i];
      part.layoutSize = sizes[i];
      if (adapter.targetAvail != null) {
        // この時点では表示されておらず利用可能サイズが決定されていないことがある。
        part.layoutRatio = sizes[i] / adapter.targetAvail;
      }
    }
  }
  
  /** 指定位置にノードを追加する */
  private void showElement(Element e) {   
    assert e.visible;
    Part part = e.part;
    int index = 0;
    for (; index < visibleParts.size(); index++) {
      if (visibleParts.get(index).order > part.order) {
        break;
      }
    }
    visibleParts.add(index, part);
    paneChildren.add(part.node);
    if (paneChildren.size() == 1) return;
    Divider divider = createDivider();
    dividers.add(divider);
    paneChildren.add(divider);
  }
  
  
  /** 指定インデックスのノードを削除する。ノードに設定したPartはそのまま保持させておく */
  private void hideElement(Element e) {
    Part part = e.part;
    visibleParts.remove(part);
    paneChildren.remove(part.node);
    if (paneChildren.size() == 0) return;
    paneChildren.remove(dividers.remove(0));
  }

  public FxSplitPane setResizeFixed(int...indexes) {
    for (int index: indexes) {
      visibleParts.get(index).resizeFixed = true;
    }
    return this;
  }
  
  /**
   * {@link Divider}を作成する
   * @return
   */
  private Divider createDivider() {
    Divider divider = new Divider(horizontal);
    divider.setCallback(new DividerCallback() {
      public void startOperation(ViewMouseHandler mouseHandler) {
        int index = dividers.indexOf(divider);
        if (index < 0) throw new IllegalStateException();
        pane.setMouseHandler(mouseHandler);
        pane.setDragLayouter(new DragLayouter(
          adapter,
          visibleParts.get(index), 
          divider, 
          visibleParts.get(index + 1)
        ));
      }

      public void dragging(double move) {
        pane.setDragMove(move);
      }

      @Override
      public void endOperation() {
        pane.setMouseHandler(null);
        pane.setDragLayouter(null);
      }
    });
    return divider;
  }
}
