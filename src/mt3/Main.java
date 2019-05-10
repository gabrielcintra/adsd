package mt3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main 
{
	
  public static void main(String[] args) {
	// Coleta data atual formatada
	DateFormat formatoData = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    String dataAtual = formatoData.format(cal.getTime());
    
    // Gera arquivo de saída
    try {
		PrintStream out = new PrintStream(new File("%s.txt", dataAtual));
		System.setOut(out);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    
  }
}
