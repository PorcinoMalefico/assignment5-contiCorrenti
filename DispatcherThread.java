import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DispatcherThread implements Runnable {
	ExecutorService pool;
	String filename;
	
	public DispatcherThread(ExecutorService pool, String filename) {
		this.pool = pool;
		this.filename = filename;
	}
	
	@Override
	public void run() {
		try(FileChannel fc = FileChannel.open(Paths.get(filename), StandardOpenOption.READ)) {
			// Allocazione del buffer e lettura da file.
			ByteBuffer buffer = ByteBuffer.allocate((int) fc.size());
			fc.read(buffer);

			// Conversione in String del buffer usato per la lettura
			String content = new String(buffer.array(), StandardCharsets.UTF_8);
			
			JSONParser parser = new JSONParser();
			try {
				
				// Ottenimento del JSONArray contenente i conti 
				JSONArray jarray = (JSONArray) parser.parse(content);
				
				// Per ogni conto nel JSONArray evoca un WorkerThread
				for(Object obj : jarray) {
					pool.execute(new WorkerThread((JSONObject) obj));
				}
				
				pool.shutdown();
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
