package contracts;

import java.io.IOException;

/**
 * All logging solutions MUST implement this interface
 *
 */
public interface ILogger {
    /**
     * set a file to send the logger message
     *
     * @param path path to the log file
     * @param rotationSize – the maximum number of bytes to write to any one file count
     * @param maxFiles the number of files to use
     *
     */
    void setLogFile(String path, int rotationSize, int maxFiles) throws IOException;
    /**
     * log a message in the given level
     *
     * @param level "INFO", "DEBUG", "WARNING" or "SEVERE"
     * @param message – the message
     *
     */
    void message(String level, String message);
    /**
     * close the logger
     * Call this method once you have finished logging
     */
    void close();
}
