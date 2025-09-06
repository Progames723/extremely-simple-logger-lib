package dev.progames723.esll;

public interface ILogger {
	void debug(String message, Object... args);
	
	void trace(String message, Object... args);
	
	void info(String message, Object... args);
	
	void warn(String message, Object... args);
	
	void error(String message, Object... args);
	
	void fatal(String message, Object... args);
	
	void log(Level level, String message, Object... args);
	
	enum Level {
		DEBUG,
		TRACE,
		INFO,
		WARN,
		ERROR,
		FATAL
	}
}
