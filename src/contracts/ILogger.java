package contracts;

import java.io.IOException;

public interface ILogger {
    void setLogFile(String path, int rotationSize, int maxFiles) throws IOException;
    void message(String level, String message);
    void close();
}
