package com.cm55.fx;

import java.io.*;

import javafx.application.*;

/**
 * 呼び出したスレッドとは別のスレッドでプロセスを実行し、その終了を待つ。 その間の標準出力、エラー出力、例外、終了コードをコールバック呼び出しで通知する。
 * これらの通知はJavaFXのスレッドで行われる。
 */
public class FxProcessTracker {

  /** コールバック */
  public interface Callback {

    /** 例外が発生した場合の通知 */
    public void exception(Exception ex);

    /** 標準出力行の通知 */
    public void stdout(String line);

    /** 標準エラー業の通知 */
    public void stderr(String line);

    /** プロセス終了通知 */
    public void exited(int result);
  }

  private Process process;
  private Thread thread;
  private Callback callback;
  private int retCode = -1;
  private Exception exception = null;

  public FxProcessTracker(ProcessBuilder builder) {
    thread = new Thread() {
      public void run() {
        try {
          process = builder.start();
        } catch (IOException ex) {
          doException(ex);
          return;
        }
        Thread stdout = new IsThread(process.getInputStream(), (line) -> {
          doStdout(line);
        });
        Thread stderr = new IsThread(process.getErrorStream(), (line) -> {
          doStderr(line);
        });
        try {
          int r = process.waitFor();
          doExited(r);
        } catch (InterruptedException ex) {
        } finally {
          try {
            stdout.join();
          } catch (InterruptedException ex) {
          }
          try {
            stderr.join();
          } catch (InterruptedException ex) {
          }
        }
      }
    };
  }

  /** コールバックを設定する */
  public FxProcessTracker setCallback(Callback callback) {
    this.callback = callback;
    return this;
  }

  /** 開始する */
  public FxProcessTracker start() {
    thread.start();
    return this;
  }

  /** 強制終了する */
  public void destroy() {
    process.destroy();
  }

  /** プロセスの返り値を得る */
  public int getRetCode() {
    return retCode;
  }

  /** 例外を得る */
  public Exception getException() {
    return exception;
  }

  private void doException(Exception ex) {
    Platform.runLater(() -> {
      exception = ex;
      callback.exception(ex);
    });
  }

  private void doExited(int result) {
    Platform.runLater(() -> {
      retCode = result;
      callback.exited(result);
    });
  }

  private void doStdout(String line) {
    Platform.runLater(() -> {
      callback.stdout(line);
    });
  }

  private void doStderr(String line) {
    Platform.runLater(() -> {
      callback.stderr(line);
    });
  }

  /**
   * 標準出力、エラー出力を監視するスレッド
   */
  public static class IsThread extends Thread {
    private BufferedReader reader;
    private LineGot lineGot;

    public IsThread(InputStream is, LineGot lineGot) {
      reader = new BufferedReader(new InputStreamReader(is));
      this.lineGot = lineGot;
      start();
    }

    @Override
    public void run() {
      try {
        for (;;) {
          String line = reader.readLine();
          if (line == null)
            break;
          lineGot.lineGot(line);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public interface LineGot {
    public void lineGot(String line);
  }

}
