
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileCreator {

	// Lista dei 10 nomi e cognomi più comuni in Italia
	private String[] nomi = {"Francesco","Sofia","Leonardo","Giulia","Alessandro","Aurora","Lorenzo","Mattia","Andrea","Gabriele"};
	private String[] cognomi = {"Rossi","Russo","Ferrari","Esposito","Bianchi","Romano","Colombo","Bruno","Ricci","Marino"};
	private String[] causali = {"Bonifico","Accredito","Bollettino","F24","PagoBancomat"};
	
	// Genera un nuovo file conticorrenti.json con n conti.
	public void generateFile(int n) throws IOException{
		
		/*
		 * Creazione FileChannel di scrittura.
		 * Il file viene creato se non esiste. Se esiste, viene ripulito (TRUNCATE_EXISTING).
		 */
		FileChannel fc = FileChannel.open(Paths.get("conticorrenti.json"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		
		// Creazione dell'array che conterrà i conti
		JSONArray jarray = new JSONArray();
		
		while(n>0) {
			// Creazione oggetto JSON del singolo conto e inserimento dei dati con generazione di 1<k<100 movimenti.
			JSONObject obj = new JSONObject();
			obj.put("nome", nomi[(int) (Math.random()*10)]);
			obj.put("cognome",new String(cognomi[(int) (Math.random()*10)]));
			
			// Array JSON per contenere gli oggetti relativi ai singoli movimenti
			JSONArray listamovimenti = new JSONArray();
			
			int k = ThreadLocalRandom.current().nextInt(1,101);
			while(k>0) {
				JSONObject mov = new JSONObject();
				// Genera data e la inserisce nell'oggetto relativo al movimento
				String dd = String.valueOf(ThreadLocalRandom.current().nextInt(1,32));
				String mm = String.valueOf(ThreadLocalRandom.current().nextInt(1,13));
				String yy = String.valueOf(ThreadLocalRandom.current().nextInt(2018,2020));
				mov.put("data", new String(dd+"/"+mm+"/"+yy));
				// Sceglie una causale random e la inserisce nell'oggetto
				mov.put("causale", new String(causali[ThreadLocalRandom.current().nextInt(0,5)]));
				
				listamovimenti.add(mov);
				
				k--;
			}
			// Inserimento della lista movimenti nel conto
			obj.put("movimenti", listamovimenti);
		
			// Aggiunta del conto alla lista dei conti
			jarray.add(obj);
			
			n--;
		}
		//Inserimento in buffer della rappresentazione String dell'array JSON. Segue la scrittura su file.
		byte jsonstring[] = (jarray.toJSONString()).getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(jsonstring);
		fc.write(buffer);
		
		fc.close();
		
	}
	
}
