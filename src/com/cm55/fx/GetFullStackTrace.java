package com.cm55.fx;

import java.io.*;

public class GetFullStackTrace {

  public static String get(final Throwable th) {

    StringWriter sw = new StringWriter();
    PrintWriter logWriter = new PrintWriter(sw);

    // dump exception stack if specified
    if (null != th) {
      final StackTraceElement[] traces = th.getStackTrace();
      if (null != traces && traces.length > 0) {
        logWriter.println(th.getClass() + ": " + th.getMessage());

        for (final StackTraceElement trace : traces) {
          logWriter.println("    at " + trace.getClassName() + '.' + trace.getMethodName() + '(' + trace.getFileName()
              + ':' + trace.getLineNumber() + ')');
        }
      }

      Throwable cause = th.getCause();
      while (null != cause) {
        final StackTraceElement[] causeTraces = cause.getStackTrace();
        if (null != causeTraces && causeTraces.length > 0) {
          logWriter.println("Caused By:");
          logWriter.println(cause.getClass() + ": " + cause.getMessage());

          for (final StackTraceElement causeTrace : causeTraces) {
            logWriter.println("    at " + causeTrace.getClassName() + '.' + causeTrace.getMethodName() + '('
                + causeTrace.getFileName() + ':' + causeTrace.getLineNumber() + ')');
          }
        }

        // fetch next cause
        cause = cause.getCause();
      }
    }

    logWriter.flush();
    return sw.toString();
  }

}
