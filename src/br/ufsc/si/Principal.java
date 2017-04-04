package br.ufsc.si;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Principal {

	public static List<Tabuleiro> fronteira = new ArrayList<>();
	public static List<Tabuleiro> visitados = new ArrayList<>();
	public static int[][] valoresObjetivo = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
	public static Tabuleiro tabuleiroObjetivo = new Tabuleiro(valoresObjetivo);

	public static void main(String[] args) {

		// int[][] tabuleiro = criarTabuleiro();
		int[][] tabuleiroValores = { { 5, 4, 0 }, { 6, 1, 8 }, { 7, 3, 2 } };
		// int[][] tabuleiroValores = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };

		Tabuleiro tabuleiro = new Tabuleiro(tabuleiroValores, 0);
		tabuleiro.mostrarTabuleiro();

		System.out.println("\n");

		if (tabuleiro.solucionavel() == false) {
			System.out.println("Não solucionável");
			System.exit(1);
		}

		visitados.add(tabuleiro);
		while (tabuleiro.igualA(tabuleiroObjetivo) == false) {
			fronteira.addAll(tabuleiro.criarFilhos());
			ordenarFronteira();
			for (int i = 0; i < fronteira.size(); i++) {
				if (visitados.contains(fronteira.get(i))) {
					continue;
				}
				tabuleiro = fronteira.get(i);
				break;
			}
			visitados.add(tabuleiro);
		}

		System.out.println(fronteira.size());
		System.out.println(visitados.size());

		int passos = 0;
		while (tabuleiro != null) {
			tabuleiro.mostrarTabuleiro();
			tabuleiro = tabuleiro.pai;
			System.out.println("");
			passos++;
		}

		System.out.println("Passos: " + passos);

	}

	private static void ordenarFronteira() {
		fronteira = fronteira.stream().sorted((tab, other) -> Integer.compare(tab.valor, other.valor))
				.collect(Collectors.toList());
	}

}
