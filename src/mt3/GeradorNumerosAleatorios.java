package mt3;

import java.util.ArrayList;
import java.util.List;


public class GeradorNumerosAleatorios {

	private List<Integer> valores;
	private int semente;

	/**
	   * Construtor de classe
	   * @param semente
	   */
	
	public GeradorNumerosAleatorios(int semente) {
		this.semente = semente;
		this.valores = new ArrayList<Integer>();
	}
	
	/**
	   * Gerador de número aleatório utilizando seed com base no Congruential Generator Method
	   * https://en.wikipedia.org/wiki/Linear_congruential_generator
	   * @param a
	   * @param c
	   * @param mod
	   * @return List< Integer>
	   */

	private List<Integer> CongruentialGenerator(int a, int c, int mod) {
		valores = new ArrayList<Integer>();
		valores.add(semente);
		
		if (a < mod && c < mod) {
			for (int i = 0; i < mod-1; i++) {
				int aux = valores.get(i);
				int aux2 = ((a * aux) + c) % mod;
				valores.add(aux2);
			}
		}
		
		return valores;
	}
	
	/**
	   * Configura uma nova seed
	   * @param novaSemente
	   */

	public void setSemente(int novaSemente) {
		this.semente = novaSemente;
	}
	
	/**
	   * Método de chamada do Congruential Generator (Repasse)
	   * @param a
	   * @param c
	   * @param mod
	   */
	
	public List<Integer> gerarNumeros(int a, int c, int mod) {
		return CongruentialGenerator(a, c, mod);
	}

}
