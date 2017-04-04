package br.ufsc.si;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Principal {

	public static List<Estado> fronteira = new ArrayList<>();
	public final static List<Estado> visitados = new ArrayList<>();
	public final static int[][] estadoFinal = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };

	public static void main(String[] args) {

		// int[][] estadoInicial = criarEstadoInicialRandomico();
		//int[][] estadoInicial = { { 5, 4, 0 }, { 6, 1, 8 }, { 7, 3, 2 } };
		//int[][] estadoInicial = {{8,6,7}, {2,5,4}, {3,0,1}};
		int[][] estadoInicial = {{6,4,7}, {8,5,0}, {3,2,1}};
		// int[][] estadoInicial = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
		// int[][] estadoInicial = { {3,2,1}, {0,6,7}, {5,4,8}};

		Estado estado = new Estado(estadoInicial);
		estado.mostrarTabuleiro();

		System.out.println("\n");

		if (estado.solucionavel() == false) {
			System.out.println("Não solucionável");
			System.exit(1);
		}

		visitados.add(estado);
		while (estado.igualAoEstado(estadoFinal) == false) {
			adicionarEstadosNaFronteiraOrdenado(estado);
			//ordenarFronteira();
			estado = obterPrimeiroDaFronteiraNaoVisitadoRemovendo();
			//estado = obterPrimeiroDaFronteiraNaoVisitado();
			visitados.add(estado);
		}

		
		//Mostrar resultado
		
		System.out.println("Resultado: ");
		List<Estado> resultados = new ArrayList<>();
		while (estado != null) {
			resultados.add(estado);
			estado = estado.pai;
		}
		Collections.reverse(resultados);
		for (Estado resultado : resultados) {
			resultado.mostrarTabuleiro();
			System.out.println("");
		}
		System.out.println("Passos: " + resultados.size());
		System.out.println("Tamanho da fronteira: " + fronteira.size());
		System.out.println("Tamanho dos visitados: " + visitados.size());

	}

	private static Estado obterPrimeiroDaFronteiraNaoVisitado() {
		for (int i = 0 ;i<fronteira.size() ; i++) {
			if (visitados.contains(fronteira.get(i))) {
				continue;
			}
			return fronteira.get(i);
		}
		return null;
	}
	
	private static Estado obterPrimeiroDaFronteiraNaoVisitadoRemovendo() {
		Iterator<Estado> iterator = fronteira.iterator();
		while (iterator.hasNext()) {
			Estado next = iterator.next();
			if (visitados.contains(next)){
				iterator.remove();
				continue;
			}
			iterator.remove();
			return next;
		}
		return null;
	}

	private static void adicionarEstadosNaFronteiraOrdenado(Estado estado) {
		for (Estado filho : estado.criarFilhos()) {
			boolean inserido = false;
			for (int i=0;i<fronteira.size();i++) {
				if (filho.valor <= fronteira.get(i).valor) {
					fronteira.add(i, filho);
					inserido = true;
					break;
				}
			}
			if (inserido == false) {
				fronteira.add(filho);
			}
		}
	}
	
	private static void adicionarEstadosNaFronteira(Estado estado) {
		fronteira.addAll(estado.criarFilhos());
	}

	private static void ordenarFronteira() {
		Collections.sort(fronteira, (tab, other) -> Integer.compare(tab.valor, other.valor));
	}
	
	static public int[][] criarEstadoInicialRandomico() {
		List<Integer> valoresLista = new ArrayList<>();
		for (int i = 0; i < Estado.TOTAL_NUMEROS; i++) {
			valoresLista.add(i);
		}
		int[][] tabuleiro = new int[Estado.TOTAL_LINHAS][Estado.TOTAL_COLUNAS];
		for (int i = 0; i < Estado.TOTAL_LINHAS; i++) {
			for (int j = 0; j < Estado.TOTAL_COLUNAS; j++) {
				int indexArray = new Random().nextInt(valoresLista.size());
				tabuleiro[i][j] = valoresLista.get(indexArray);

				valoresLista.remove(indexArray);
			}
		}
		return tabuleiro;
	}

}
