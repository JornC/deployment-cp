package nl.yogh.aerius.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.server.worker.jobs.PullRequestUpdateJob;

public class CmdUtil {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  public static class ProcessExitException extends Exception {
    private static final long serialVersionUID = 1924394908516357042L;

    private final int code;

    public ProcessExitException(final int code) {
      super("ProcessException while executing command.");
      this.code = code;
    }

    public int getCode() {
      return code;
    }

    @Override
    public String toString() {
      return "ProcessExitException [code=" + code + "]";
    }
  }

  public static ArrayList<String> cmd(final String dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    LOG.trace("> {}", cmd);

    final ArrayList<String> output = new ArrayList<String>();
    final Process process = new ProcessBuilder(new String[] { "bash", "-c", cmd }).redirectErrorStream(true).directory(new File(dir)).start();

    try {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line = null;
        while ((line = br.readLine()) != null) {
          LOG.trace(line);
          output.add(line);
        }

        final int exitCode = process.waitFor();
        if (exitCode != 0) {
          LOG.error("ERROR ====");
          LOG.error(cmd);
          LOG.error("Exit code during process: " + exitCode);
          LOG.error("ERROR ====");
          throw new ProcessExitException(exitCode);
        }
      }
    } finally {
      // Kill all that lives
      if (process.isAlive()) {
        process.destroyForcibly();
      }
    }

    return output;
  }
}
