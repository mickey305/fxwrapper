package com.cm55.fx;

import java.util.function.*;
import java.util.stream.*;

import com.cm55.eventBus.*;

import javafx.event.*;
import javafx.scene.control.*;

public class FxMenu<T>  {

  public static class SelectionEvent<T> {
    public final T node;
    public SelectionEvent(T node) {
      this.node = node;
    }
  }
  
  public interface Adapter<T> {
    public String getLabel(T object);
    public boolean hasChildren(T object);
    public Stream<T>children(T object);
  }

  protected Adapter<T>adapter;
  protected Menu menu;
  protected EventBus eventBus = new EventBus();
  
  public Menu node() {
    return menu;
  }
  
  public FxMenu(Adapter<T>adapter) {
    this.adapter = adapter;
  }
  
  public FxMenu(Adapter<T>adapter, T root) {
    this(adapter);
    set(root);
  }
  
  public void set(T root) {
    menu = new Object() {
      
      // Menuを作成する。親ノードでなければいけない
      Menu createMenu(T node) {
        Menu menu = new Menu(adapter.getLabel(node));
        adapter.children(node).forEach(child-> {
          menu.getItems().add(createItem(child));
        });
        return menu;
      }
      MenuItem createItem(T node) {
        if (adapter.hasChildren(node)) return createMenu(node);
        MenuItem item = new MenuItem(adapter.getLabel(node));              
        item.addEventHandler(ActionEvent.ACTION , e ->  eventBus.dispatchEvent(new SelectionEvent<T>(node)));
        return item;
      }
    }.createMenu(root);    
  }
  
  @SuppressWarnings("rawtypes")
  public Unlistener<SelectionEvent> listen(Consumer<SelectionEvent>l) {
    return eventBus.listen(SelectionEvent.class,  l);
  }
  
  private void test() {
    /*
  
    // メニュー
    menuBar     = new MenuBar();
//    Image       icon        = new Image( new File( "img/chara_one.png" ).toURI().toString() );
//    ImageView   iconView1   = new ImageView( icon );
//    iconView1.setFitWidth( 15 );
//    iconView1.setFitHeight( 15 );
     
    // メニューFileを、一般メニューとして作成
    // 各メニューにはイメージを付与することが可能
    Menu        menu1_1 = new Menu( "File" ); //, iconView1 );
    MenuItem    menu1_2 = new MenuItem( "New" );
    MenuItem    menu1_3 = new MenuItem( "Save" );
    MenuItem    menu1_4 = new SeparatorMenuItem();
    MenuItem    menu1_5 = new MenuItem( "Close" );
    menu1_3.setDisable( true );
    menu1_1.getItems().addAll( menu1_2 , menu1_3 , menu1_4 , menu1_5 );
     
    // メニューEditを、チェックメニューで作成
    Menu            menu2_1     = new Menu( "Edit" );
    CheckMenuItem   menu2_2     = new CheckMenuItem( "check1" );
    CheckMenuItem   menu2_3     = new CheckMenuItem( "check2" );
    menu2_1.getItems().addAll( menu2_2 , menu2_3 );

    // メニューViewModeを、ラジオメニューで作成
    Menu            menu3_1     = new Menu( "ViewMode" );
    RadioMenuItem   menu3_2     = new RadioMenuItem( "radio1" );
    RadioMenuItem   menu3_3     = new RadioMenuItem( "radio2" );
    ToggleGroup     menu3Group  = new ToggleGroup();
    menu3_2.setUserData( "radio1が選択されました" );
    menu3_3.setUserData( "radio2が選択されました" );
    menu3_2.setToggleGroup( menu3Group );
    menu3_3.setToggleGroup( menu3Group );
    menu3_1.getItems().addAll( menu3_2 , menu3_3 );
     
    // メニューにイベントハンドラを登録
    menu1_2.addEventHandler( ActionEvent.ACTION , e -> System.out.println( menu1_2.getText() ) );
    menu1_3.addEventHandler( ActionEvent.ACTION , e -> System.out.println( menu1_3.getText() ) );
    menu1_5.addEventHandler( ActionEvent.ACTION , e -> System.out.println( menu1_5.getText() ) );
    menu2_2.selectedProperty().addListener( ( ov , old , current ) -> System.out.println( "check1:" + current ) );
    menu2_3.selectedProperty().addListener( ( ov , old , current ) -> System.out.println( "check2:" + current ) );
    menu3Group.selectedToggleProperty().addListener( ( ov , old , current ) -> System.out.println( "radio:" + ( ( menu3Group.getSelectedToggle() == null ) ? "" : menu3Group.getSelectedToggle().getUserData() ) ) );
     
    // メニューを登録
    menuBar.getMenus().addAll( menu1_1 , menu2_1 , menu3_1 );
    */
  }

}
