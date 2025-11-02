package dev.progames723.esll;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * the main logger
 */
@SuppressWarnings({"IOStreamConstructor", "unused", "ResultOfMethodCallIgnored", "DataFlowIssue", "removal"})
public class Logger implements ILogger {
	private final Level stdOutLoggingLevel;
	private final String loggerName;
	private final boolean logToFiles;
	
	private static final BufferedWriter logFile;
	private static final BufferedWriter debugLogFile;
	
	static {
		try {
			logFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(makeLogFile(false)), StandardCharsets.UTF_8));
			debugLogFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(makeLogFile(true)), StandardCharsets.UTF_8));
		} catch (Exception e) {
			Error error = new UnknownError("Failed to instantiate log files!");
			error.initCause(e);
			throw error;
		}
	}
	
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		.appendValue(ChronoField.HOUR_OF_DAY, 2)
		.appendLiteral(':')
		.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
		.optionalStart()
		.appendLiteral(':')
		.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
		.toFormatter();
	
	/**
	 * constructs a {@link Logger} has overloads
	 * @param stdOutLoggingLevel it does explain itself nicely
	 * @param loggerName same here
	 * @param logToFiles and here
	 */
	public Logger(Level stdOutLoggingLevel, String loggerName, boolean logToFiles) {
		this.stdOutLoggingLevel = stdOutLoggingLevel;
		this.loggerName = loggerName;
		this.logToFiles = logToFiles;
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	public Logger(Level stdOutLoggingLevel, String loggerName) {
		this(stdOutLoggingLevel, loggerName, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	public Logger(String loggerName, boolean logToFiles) {
		this(Level.INFO, loggerName, logToFiles);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	public Logger(String loggerName) {
		this(Level.INFO, loggerName, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void debug(String message, Object... args) {
		doLog(Level.DEBUG, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void trace(String message, Object... args) {
		doLog(Level.TRACE, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void info(String message, Object... args) {
		doLog(Level.INFO, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void warn(String message, Object... args) {
		doLog(Level.WARN, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void error(String message, Object... args) {
		doLog(Level.ERROR, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void fatal(String message, Object... args) {
		doLog(Level.FATAL, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * {@inheritDoc}
	 * @param level {@inheritDoc}
	 * @param message {@inheritDoc}
	 * @param args {@inheritDoc}
	 * @apiNote it inserts the arguments into the "{}" found in the message parameter <p>
	 * if it doesn't find enough arguments it will leave the curly brackets in <p>
	 * and if it doesn't find enough curly brackets for every remaining argument it discards them
	 */
	@Override
	public void log(Level level, String message, Object... args) {
		doLog(level == null ? Level.INFO : level, message == null ? "null" : message, args == null ? new Object[0] : args);
	}
	
	/**
	 * quite a handy method eh? <p>
	 * very hacky
	 */
	private static File makeLogFile(boolean debug) {
		File pathFile = new File((System.getProperty("user.dir") + "/logs").replace('\\', '/'));
		Path path = pathFile.toPath();
		if (!pathFile.exists()) pathFile.mkdirs();
		path = debug ? path.resolve("debug.log") : path.resolve("latest.log");
		pathFile = path.toFile();
		File tempFile = pathFile;
		//fuckery with old logs lol
		label: {
			if (pathFile.isDirectory()) pathFile.delete();
			if (!pathFile.isFile()) break label;
			try (FileReader reader = new FileReader(pathFile)) {
				if (reader.read() == -1) break label;
			} catch (IOException ignored) {}
			path = path.resolve("..");
			path = debug ? path.resolve("debug.log.old") : path.resolve("latest.log.old");
			pathFile = path.toFile();
			if (pathFile.exists()) pathFile.delete();
			tempFile.renameTo(pathFile);
			tempFile.delete();
			pathFile = tempFile;
		}
		try {
			pathFile.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pathFile;
	}
	
	/**
	 * my attempt at "async" file logging
	 */
	private CompletableFuture<Void> logToFile(String message, boolean debugFile) {
		return CompletableFuture.runAsync(() -> {
			if (debugFile) {
				try {
					debugLogFile.append(message).flush();
				} catch (IOException e) {
					System.err.println("Failed to write a (possibly debug) message!\nWriting to stdout instead...");
					System.err.print(message);
					System.err.println("this exception caused it");
					e.printStackTrace(System.err);
				}
			} else {
				try {
					logFile.append(message).flush();
				} catch (IOException e) {
					System.err.println("could not write (that ^) to log file!");
					System.err.println("this exception caused it");
					e.printStackTrace(System.err);
				}
			}
		});
	}
	
	/**
	 * the backbone method behind all this
	 */
	private void doLog(Level level, String message, Object[] args) {
		AtomicBoolean hasException = new AtomicBoolean(false);
		switch (level) {
			case ERROR:
			case FATAL:
				hasException.set(true);
		}
		String formattedMessage = format(level, message, args, hasException);
		CompletableFuture<Void> future1 = null;
		CompletableFuture<Void> future2 = null;
		if (level.ordinal() >= stdOutLoggingLevel.ordinal()) {
			if (!hasException.get()) System.out.print(formattedMessage);
			else System.err.print(formattedMessage);
			if (logToFiles) future1 = logToFile(formattedMessage, false);
		}
		if (logToFiles) future2 = logToFile(formattedMessage, true);
		try {
			future1.get();
		} catch (Exception ignored) {}
		try {
			future2.get();
		} catch (Exception ignored) {}
	}
	
	/**
	 * why am i writing documentation here
	 */
	private String format(Level level, String message, Object[] args, AtomicBoolean hasException) {
		return '[' + ZonedDateTime.now().format(formatter) + "] (" +
			loggerName + '/' + level + ") > " +
			customMessageFormat(message, args, hasException) +
			'\n';
	}
	
	/**
	 * why am i writing documentation here
	 */
	private String customMessageFormat(String message, Object[] args, AtomicBoolean hasException) {
		if (args == null) return message;
		String[] split = message.split("\\{}", args.length == 0 ? 1 : args.length);
		StringBuilder builder = new StringBuilder();
		if (args.length == 0) {
			builder.append(split[0]);
			if (split.length == 2) builder.append(split[1]);
		} else {
			Throwable throwable = null;
			if (args[args.length - 1] instanceof Throwable) throwable = (Throwable) args[args.length - 1];
			for (int i = 0; i < split.length; i++) {
				if (i < args.length) {
					if (i + 1 != args.length || !(args[i] instanceof Throwable))
						builder.append(split[i].replace("{}", Objects.toString(args[i])));
				} else builder.append(split[i]);
			}
			if (throwable != null) {
				hasException.set(true);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try (PrintStream printStream = new PrintStream(stream)) {
					throwable.printStackTrace(printStream);
				}
				builder.append("\n").append(stream);
			}
		}
		return builder.toString();
	}
	
	/**
	 * used to close the streams ig
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			IOException exception = null;
			try {
				logFile.flush();
				logFile.close();
			} catch (IOException e) {
				exception = e;
			}
			try {
				debugLogFile.flush();
				debugLogFile.close();
			} catch (IOException e) {
				if (exception != null) exception.addSuppressed(e);
				else exception = e;
			}
			if (exception != null) throw exception;
		} finally {
			super.finalize();
		}
	}
}
