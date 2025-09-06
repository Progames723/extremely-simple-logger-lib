package dev.progames723.esll;

public interface ILogger {
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#DEBUG} level
	 * @see #log(Level, String, Object...)
	 */
	void debug(String message, Object... args);
	
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#TRACE} level
	 * @see #log(Level, String, Object...)
	 */
	void trace(String message, Object... args);
	
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#INFO} level
	 * @see #log(Level, String, Object...)
	 */
	void info(String message, Object... args);
	
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#WARN} level
	 * @see #log(Level, String, Object...)
	 */
	void warn(String message, Object... args);
	
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#ERROR} level
	 * @see #log(Level, String, Object...)
	 */
	void error(String message, Object... args);
	
	/**
	 * uses {@link dev.progames723.esll.ILogger.Level#FATAL} level
	 * @see #log(Level, String, Object...)
	 */
	void fatal(String message, Object... args);
	
	/**
	 * it logs
	 * @param level logging level to use
	 * @param message message to be logged
	 * @param args arguments to the message
	 */
	void log(Level level, String message, Object... args);
	
	/**
	 * funny enum
	 */
	enum Level {
		DEBUG,
		TRACE,
		INFO,
		WARN,
		ERROR,
		FATAL
	}
}
