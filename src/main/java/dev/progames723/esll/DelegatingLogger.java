package dev.progames723.esll;

@SuppressWarnings("unused")
public class DelegatingLogger implements ILogger {
	private final ILogger delegate;
	
	/**
	 * constructs a {@link DelegatingLogger} instance
	 * @param delegate delegate logger to use
	 */
	public DelegatingLogger(ILogger delegate) {this.delegate = delegate;}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void debug(String message, Object... args) {delegate.debug(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void trace(String message, Object... args) {delegate.trace(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void info(String message, Object... args) {delegate.info(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void warn(String message, Object... args) {delegate.warn(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void error(String message, Object... args) {delegate.error(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @see #log(Level, String, Object...)
	 */
	@Override
	public void fatal(String message, Object... args) {delegate.fatal(message, args);}
	
	/**
	 * {@inheritDoc}
	 * @param level {@inheritDoc}
	 * @param message {@inheritDoc}
	 * @param args {@inheritDoc}
	 * @apiNote delegates to the {@link #delegate} logger
	 */
	@Override
	public void log(Level level, String message, Object... args) {delegate.log(level, message, args);}
}
