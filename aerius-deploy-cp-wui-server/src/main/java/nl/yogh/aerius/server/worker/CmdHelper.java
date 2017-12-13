package nl.yogh.aerius.server.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdHelper {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  public static class ProcessExitException extends Exception {
    private static final long serialVersionUID = 1924394908516357042L;

    private final int code;

    public ProcessExitException(final int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
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
          // LOG.error("ERROR ====");
          LOG.error(cmd);
          // LOG.error("Exit code during process: " + exitCode);
          // LOG.error("ERROR ====");
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
