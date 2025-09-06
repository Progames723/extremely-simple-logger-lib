package dev.progames723.esll;

@SuppressWarnings("unused")
public class DelegatingLogger implements ILogger {
	private final ILogger delegate;
	
	public DelegatingLogger(ILogger delegate) {this.delegate = delegate;}
	
	@Override
	public void debug(String message, Object... args) {delegate.debug(message, args);}
	
	@Override
	public void trace(String message, Object... args) {delegate.trace(message, args);}
	
	@Override
	public void info(String message, Object... args) {delegate.info(message, args);}
	
	@Override
	public void warn(String message, Object... args) {delegate.warn(message, args);}
	
	@Override
	public void error(String message, Object... args) {delegate.error(message, args);}
	
	@Override
	public void fatal(String message, Object... args) {delegate.fatal(message, args);}
	
	@Override
	public void log(Level level, String message, Object... args) {delegate.log(level, message, args);}
}
