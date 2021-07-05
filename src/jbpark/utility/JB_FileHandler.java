package jbpark.utility;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

public class JB_FileHandler extends FileHandler {
	public JB_FileHandler(String pattern, int limit, int count) 
			throws IOException, SecurityException {
		super(pattern, limit, count);
	} fff
	
	/** Write each log line(=record) on to disk right away.
	 * @author Park, JongBum
	 */
	@Override
	public synchronized void publish(LogRecord record) {
		super.publish(record);
		flush();
	}
}
