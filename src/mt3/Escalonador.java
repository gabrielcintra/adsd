package mt3;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador extends Thread {	
	// Detalhes - Fila 1
	private Queue<Integer> fila1;
	private List<Integer> numsFila1;
	private int idFila1;
	private int[] varsFila1;
	
	// Detalhes - Fila 2
	private Queue<Integer> fila2;
	private List<Integer> numsFila2;
	private int idFila2;
	private int[] varsFila2;
	
	// Detalhes - Saída esperada
	private List<Integer> numsSaida;
	private int idSaida;
	private int[] varsSaida;
	
	// Parâmetros funcionais
	private final int SAIDA = -1;
	private int tempoExecucao;
	private int saida;
	private boolean servico;
	private int filaAtendida;
	private GeradorNumerosAleatorios geradorNums;
	
	/**
	   * Construtor do Escalonador. Recebe o tempo limite de execução como parâmetro.
	   * @param tempo
	   */
	
	public Escalonador(int tempo) {
		this.tempoExecucao = tempo;
		this.servico = false;
		this.saida = -1;
		this.varsFila1 = new int[] {7, 5, 12}; // mod (param 3) define a expo
		this.varsFila2 = new int[] {1, 1, 4}; // mod (param 3) define a expo
		this.varsSaida = new int[] {17, 5, 6}; // mod (param 3) define a expo
		this.idFila1 = 0;
		this.idFila2 = 0;
		this.idSaida = 0;
		
		Calendar data = Calendar.getInstance();
		int seed = data.get(Calendar.MINUTE);
		
		geradorNums = new GeradorNumerosAleatorios(seed);
		// TODO: Não deixar o código tão explícito (loop pode ser aplicado, por exemplo)
		numsFila1 = geradorNums.gerarNumeros(varsFila1[0], varsFila1[1], varsFila1[2]);
		numsFila2 = geradorNums.gerarNumeros(varsFila2[0], varsFila2[1], varsFila2[2]);
		numsSaida = geradorNums.gerarNumeros(varsSaida[0], varsSaida[1], varsSaida[2]);

		fila1 = new LinkedList<Integer>();
		fila2 = new LinkedList<Integer>();
		
		this.escalonarEntrada(0, 1); // Escalona para fila 1
		this.escalonarEntrada(0, 2); // Escalona para fila 2
		
		System.out.println("[INICIO SIMULAÇÃO] Tempo de execução: " + tempo);
	}
	
	/**
	   * Verifica e registra cada um dos eventos criados para escalonamento de entradas e saídas
	   * Trata as 3 situações descritas no mini-teste, referente a ocupação do serviço e escalonamentos
	   * @param segundos
	   */
	
	private void checarEventoNoTempo(int segundos) {
		boolean match = false;
		int filaEntrada = -1,
			segFila1 = ((LinkedList<Integer>) this.fila1).get(this.fila1.size() - 1),
			segFila2 = ((LinkedList<Integer>) this.fila2).get(this.fila2.size() - 1);
		
		if (segundos == segFila1) {
			match = true;
			
			if (!this.servico) criarSaida(1, segundos);
	
			this.escalonarEntrada(segundos, 1);
			filaEntrada = 1;
		}
		
		if (segundos == segFila2) {
			match = true;
			
			if (!this.servico) criarSaida(2, segundos);
			
			this.escalonarEntrada(segundos, 2);
			filaEntrada = 2;
		}
		
		if (filaEntrada != -1) // Houve entrada na fila
			System.out.println("[ENTRADA] Tempo: " + segundos + " / Fila " + filaEntrada);

		if (this.saida == segundos) {
			this.servico = false;
			
			if (this.fila1.size() > 0 && this.fila1.peek() < segundos) 
				criarSaida(1, segundos);
			else if (this.fila2.size() > 0) 
				criarSaida(2, segundos);
			
			System.out.println("[SAIDA] Tempo: " + segundos);
		}
		
		if (match) {
			System.out.println("[FILA 1] Tamanho: " + this.fila1.size());
			System.out.println("[FILA 2] Tamanho: " + this.fila2.size());
			System.out.println("[SERVICO] Fila atendida: " + this.filaAtendida);
		}
	}

	/**
	   * Cria uma nova saida numa fila e momento específico
	   * @param fila
	   * @param segundos
	   */
	
	private void criarSaida(int fila, int segundos) {
		this.servico = true;
		this.escalonarSaida(segundos);
		this.filaAtendida = fila;
		
		if (this.filaAtendida == 1) this.fila1.poll();
		else this.fila2.poll();
	}
	
	/**
	   * Gera números aleatórios, ids e aumento do parâmetro congruente
	   * @param fila identificador da fila
	   */

	private void gerarNumeroAleatorio(int fila) {
		if(fila == 1 && this.idFila1 >= (this.numsFila1.size() - 1)) {
			this.varsFila1[0]++;
			this.numsFila1 = this.geradorNums.gerarNumeros(varsFila1[0], varsFila1[1], varsFila1[2]);
			this.idFila1 = 0;
		} else if (fila == 2 && this.idFila2 >= (this.numsFila2.size() - 1)) {
			this.varsFila2[0]++;
			this.numsFila2 = this.geradorNums.gerarNumeros(varsFila2[0], varsFila2[1], varsFila2[2]);
			this.idFila2 = 0;
		} else if (fila == SAIDA && this.idSaida >= (this.numsSaida.size() - 1)) {
			this.numsSaida = this.geradorNums.gerarNumeros(varsSaida[0], varsSaida[1], varsSaida[2]);
			this.idSaida = 0;
		}
	}
	
	/**
	   * Realiza atividade de escalonar entrada na fila definida e no tempo definido
	   * Finaliza execução caso ultrapasse o tempo limite
	   * @param inicio
	   * @param fila
	   */

	private void escalonarEntrada(int inicio, int fila) {
		this.gerarNumeroAleatorio(fila);
		int chegada = inicio;
		
		// Adiciona na fila 1
		if (fila == 1) { 
			chegada += this.numsFila1.get(this.idFila1);
			this.fila1.add(chegada);
			this.idFila1++;
		// Adiciona na fila 2
		} else if (fila == 2) {
			chegada += this.numsFila2.get(this.idFila2);
			this.fila2.add(chegada);
			this.idFila2++;
		}
	}
	
	/**
	   * Realiza atividade de escalonar saida na fila de saídas
	   * Finaliza execução caso ultrapasse o tempo limite
	   * @param entrada
	   */

	private void escalonarSaida(int entrada) {
		if (this.saida < 0) this.saida = 0;
		
		this.gerarNumeroAleatorio(SAIDA);
		
		this.saida += entrada;
		this.saida += this.numsSaida.get(this.idSaida);
		this.idSaida++;
	}
	
	/**
	   * Execução do escalonador.
	   * O critério de parada é que o tempo de execução alcance o tempo total estimado na execução.
	   */

	public void run() {
		int tempoAtual = 0;
		
		while(true) {
			if (tempoAtual == this.tempoExecucao) break;
			checarEventoNoTempo(tempoAtual);
			tempoAtual++;
		}

		System.out.println("[FIM SIMULAÇÃO]");
	}
}
