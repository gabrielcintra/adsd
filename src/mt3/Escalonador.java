package mt3;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador extends Thread {
	private int tempo;
	private int saida;
	private boolean servico;
	private GeradorNumerosAleatorios geradorNums;
	private int filaAtendida;
	private boolean fim = false;
	
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
	private final int SAIDA = 3;
	
	public Escalonador(int tempo) {
		this.tempo = tempo;
		this.servico = false;
		this.saida = 0;
		this.varsFila1 = new int[] {7, 5, 12};
		this.varsFila2 = new int[] {1, 1, 4};
		this.varsSaida = new int[] {21, 5, 6};
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
		
		System.out.println("[INICIO SIMULAÇÃO] Tempo total: " + tempo);
	}
	
	/**
	   * Verifica e registra cada um dos eventos criados para escalonamento de entradas e saídas
	   * Trata as 3 situações descritas no mini-teste, referente a ocupação do serviço e escalonamentos
	   */

	private void checarEventosCriados() {
		if(!this.servico) {
			int inicio;
			
			if(!this.fila1.isEmpty() && this.fila1.peek() <= this.saida) {
				inicio = this.fila1.peek();
				
				this.servico = true;
				this.escalonarSaida(inicio);
				this.escalonarEntrada(inicio, 1);
				this.filaAtendida = 1;
			} else {
				inicio = this.fila2.peek();
				
				this.servico = true;
				this.escalonarSaida(inicio);
				this.escalonarEntrada(inicio, 2);
				this.filaAtendida = 2;
			}
			
			System.out.println("[ENTRADA] Tempo: " + inicio);
		} else {
			this.servico = false;
			
			if (this.filaAtendida == 1) 
				this.fila1.poll();
			else 
				this.fila2.poll();
		
			System.out.println("[SAIDA] Tempo: " + this.saida + " | Fila: " + this.filaAtendida);
		}
		
		System.out.println("[SERVICO] Fila atendida: " + this.filaAtendida);
		System.out.println("[FILA 1] Tamanho: " + this.fila1.size());
		System.out.println("[FILA 2] Tamanho: " + this.fila2.size());
		System.out.println("-----------------------------");
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
		int novaChegada = inicio;
		if (fila == 1) {
			novaChegada += this.numsFila1.get(this.idFila1);
			this.fila1.add(novaChegada);
			this.idFila1++;
		} else if (fila == 2) {
			novaChegada += this.numsFila2.get(this.idFila2);
			this.fila2.add(novaChegada);
			this.idFila2++;
		}
		
		if(novaChegada > this.tempo) {
			this.fim = true;
		}
	}
	
	/**
	   * Realiza atividade de escalonar saida na fila de saídas
	   * Finaliza execução caso ultrapasse o tempo limite
	   * @param entrada
	   */

	private void escalonarSaida(int entrada) {
		this.gerarNumeroAleatorio(SAIDA);
		this.saida += entrada + this.numsSaida.get(this.idSaida);
		this.idSaida++;
		if (this.saida > this.tempo) {
			this.fim = true;
		}
	}
	
	/**
	   * Execução do escalonador.
	   * O critério de parada é a variável global fim, manipulada pelos métodos de escalonamento.
	   */

	public void run() {
		while(!this.fim) {
			checarEventosCriados();
		}
		
		System.out.println("[FIM SIMULAÇÃO]");
	}
}
