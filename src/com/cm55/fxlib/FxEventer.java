package com.cm55.fxlib;

import java.util.*;
import java.util.function.*;

/**
 * イベントシステム。
 * <p>
 * これは主にリスナー登録と削除が容易かどうかに着目して設計してある。つまり、
 * </p>
 * <ul>
 * <li>リスナーインターフェースをできるだけ作りたくない。
 * <li>イベントクラスをできるだけ作りたくない。
 * <li>Eclipse上でリスナー登録コードを記述する場合に、短くすむようにしたい。
 * <li>もちろんその他もろもろのコードをできるだけ少なくしたい。
 * <li>Javaの型システムによるチェックを活かしたい。これにより大規模修正の際にも間違いが少なくなる。
 * </ul>
 * <h2>Guava EventBusの問題点</h2>
 * <p>
 * EventBusは簡単に扱える代わりに以下の大きな問題点がある。
 * </p>
 * <ul>
 * <li>リッスン側が、全く異なるイベントクラスを使用していても字面上では気がつかない（コンパイラエラーにならない）。
 * <li>同じく@Subscribeを書き忘れていても字面上では気がつかない（これもコンパイラエラーにならない）。
 * <li>どんなに小さい仕様のイベントでも必ずイベントオブジェクトクラスを記述する必要がある。
 * </ul>
 * <h2>JavaFXイベントシステムの問題点</h2>
 * <ul>
 * <li>引数型だけ異なる、同じ名称のaddChangeListenerが複数存在するが、ラムダ式で呼び出すにはキャストを行わなければならない。
 * メソッド名を異なるものにし、何のリスナーを登録しているのか明確にすべき。
 * <li>複数のイベントを一度に登録する方法が無い。つまり、SwingのMouseListenerのような複数のイベントリスナーを一度に登録する機能がない。
 * この機能が無いと、複数のリスナーを一度に登録したり、除去したりするのが面倒になる。
 * </ul>
 * <p>
 * 上記を鑑み、
 * </p>
 * <ul>
 * <li>一つのリスナーを登録する場合は、ラムダ式にて簡単に登録することができる。
 * <li>リスナーインターフェースは新たに登録する必要が無いようにする。つまり、メソッド引数は一つに固定する。
 * 複数パラメータを渡したい場合は、専用のクラスを定義すればよい。
 * <li>複数のリスナー（リスナーグループ）を定義し、それを一度に登録あるいは除去することができる。
 * </ul>
 * <p>
 * という設計にした。もちろんこれはあくまで、「リスナーを登録して利用する側」からのもの。
 * </p>
 * @author admin
 */
public class FxEventer {
  
  /** このイベンターの登録 */
  protected Set<Registration<?>>registrations = new HashSet<Registration<?>>();

  /** イベントタイプ、コールバックを指定して登録する */
  public <T>void add(FxEventType<T>type, Consumer<T>callback) {
    registrations.add(new Registration<T>(type, callback));
  }

  /** イベントタイプ、コールバックを指定して登録除去する */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <T>boolean remove(FxEventType<T>type, Consumer<T>callback) {
    return registrations.remove(new Registration(type, callback));
  }

  /** リスナーグループを登録する */
  public <T>void add(ListenerGroup group) {
    registrations.addAll(group.set);
  }

  /** リスナーグループを登録解除する */
  public <T>void remove(ListenerGroup group) {
    registrations.removeAll(group.set);
  }

  /** イベントタイプを指定してイベント発行する */
  @SuppressWarnings("unchecked")
  public <T>void fire(FxEventType<T>type, T event) {
    registrations.stream().filter(s->s.type == type).forEach(s-> {
      ((Consumer<T>)s.callback).accept(event);
    });
  }

  /** 登録オブジェクト */
  protected static class Registration<T> {
    /** イベントタイプ */
    public FxEventType<T>type;
    
    /** コールバック */
    public Consumer<T>callback;
    
    public Registration(FxEventType<T>type, Consumer<T>callback) {
      this.type = type;
      this.callback = callback;
    }

    @Override
    public int hashCode() {
      return type.hashCode() + callback.hashCode() * 17;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Registration)) return false;
      Registration<T> that = (Registration<T>)o;
      return this.type == that.type && this.callback == that.callback;
    }
  }

  /** リスナーグループオブジェクト */
  public static class ListenerGroup {
    private Set<Registration<?>>set = new HashSet<>();    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T>ListenerGroup add(FxEventType<T>type, Consumer<T>callback) {
      set.add(new Registration(type, callback));
      return this;
    }
  }
  
}
