package services.impl1;

import configuration.Config;
import contracts.ILogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingService implements ILogger {
    private final Logger logger;
    private FileHandler fh = null;

    public LoggingService() {
        this.logger = Logger.getLogger("myLogger");
    }
    @Override
    public void setLogFile(String filename, int rotationSize, int maxFiles) throws IOException {
        this.fh = new FileHandler(Config.loggingDirectory + filename , rotationSize, maxFiles);
        logger.addHandler(fh);
    }

    @Override
    public void message(String level, String message) {
        switch (level) {
            case "INFO":
                logger.info(message);
                break;
            case "WARNING":
                logger.warning(message);
                break;
            case "SEVERE":
                logger.severe(message);
                break;
            case "DEBUG":
            default: {
                logger.setLevel(Level.ALL);
                logger.info("This is a DEBUG message");
            }
        }
    }

    @Override
    public void close() {
        this.fh.close();
    }
}
