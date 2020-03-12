package net.afterday.compas.engine.core.log;

public interface LogImpl {
    void error(String message);
    void debug(String message);
    void info(String message);
    void warning(String message);
}
