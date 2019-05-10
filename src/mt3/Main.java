package mt3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main 
{
  private static final int TEMPO = 300;
	
  public static void main(String[] args) throws UnsupportedEncodingException {
	// Coleta data atual formatada
	DateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    Calendar cal = Calendar.getInstance();
    String dataAtual = formatoData.format(cal.getTime());
    
    // Gera arquivo de saída referente a data específica na pasta "results"
    try {
    	String filePath = "src/mt3/results/" + dataAtual + ".txt";
    	PrintStream printStream = new PrintStream(new File(filePath)); 
    	System.setOut(printStream);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    
    // Instancia o escalonador e inicia sua execução
    Escalonador escalonador = new Escalonador(TEMPO);
    escalonador.run();
  }
}
