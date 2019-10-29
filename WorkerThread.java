import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WorkerThread implements Runnable {
	
	JSONObject obj;
	
	public WorkerThread(JSONObject obj) {
		this.obj = obj;
	}

	@Override
	public void run() {
		// Ottiene una String contenente i movimenti del conto passato come argomento alla creazione del thread
		String lista = obj.get("movimenti").toString();
		JSONParser parser = new JSONParser();
		
		try {
			// Ottiene un JSONArray dalla stringa ottenuta sopra
			JSONArray movimenti = (JSONArray) parser.parse(lista);
			
			// Per ogni elemento (JSONOBject) del JSONArray evoca il metodo readAndAdd
			for(Object mov : movimenti) {
				readAndAdd((JSONObject) mov);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// Estrae la causale dal movimento e incrementa il contatore relativo
	private void readAndAdd(JSONObject mov) {
		String causale = (String) mov.get("causale");
		
		switch(causale) {
			case "Bonifico":
				MainClass.cBonifico++;
				break;
			case "Accredito":
				MainClass.cAccredito++;
				break;
			case "Bollettino":
				MainClass.cBollettino++;
				break;
			case "F24":
				MainClass.cF24++;
				break;
			case "PagoBancomat":
				MainClass.cPagoBancomat++;
				break;
		}
		
	}
	

}
