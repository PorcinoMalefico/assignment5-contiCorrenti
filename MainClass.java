import java.io.IOException;
import java.util.concurrent.*;

public class MainClass {

	public static int cBonifico = 0;
	public static int cAccredito = 0;
	public static int cBollettino = 0;
	public static int cF24 = 0;
	public static int cPagoBancomat = 0;
	
	/*
	 * Il file generato contiene 4 conti, numero arbitrario, ma può essere facilmente sostituito da argomento main.
	 * 
	 */
	public static void main(String[] args) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		FileCreator fc = new FileCreator();
		
		try {
			fc.generateFile(4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pool.execute(new DispatcherThread(pool, "conticorrenti.json"));
		
		// Aspetta la terminazione di tutti i thread lanciati da DispatcherThread
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Bonifici: "+cBonifico);
		System.out.println("Accrediti: "+cAccredito);
		System.out.println("Bollettino: "+cBollettino);
		System.out.println("F24: "+cF24);
		System.out.println("PagoBancomat: "+cPagoBancomat);
	}

}
