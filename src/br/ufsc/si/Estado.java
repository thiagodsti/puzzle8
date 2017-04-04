package br.ufsc.si;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Estado {

	public static final int TOTAL_COLUNAS = 3;
	public static final int TOTAL_LINHAS = 3;
	public static final int TOTAL_NUMEROS = TOTAL_COLUNAS * TOTAL_LINHAS;
	int[][] estado;
	int custo;
	int heuristica;
	int valor;
	short linhaInicial;
	short colunaInicial;
	Estado pai = null;

	public Estado(int[][] valores) {
		this.estado = Arrays.copyOf(valores, valores.length);
		this.custo = 0;
		this.gerarHeuristica();
		this.calcularValor();
	}

	public Estado(int[][] valores, int custo, short linhaInicial, short colunaInicial, Estado pai) {
		this.estado = new int[TOTAL_LINHAS][TOTAL_COLUNAS];
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				this.estado[i][j] = valores[i][j];
			}
		}
		this.custo = custo;
		this.linhaInicial = linhaInicial;
		this.colunaInicial = colunaInicial;
		this.pai = pai;
	}

	public void mostrarTabuleiro() {
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				System.out.print(" |  " + this.estado[i][j] + "  |  ");
			}
			System.out.print("\n");
		}
	}

	public List<Estado> criarFilhos() {
		List<Estado> filhos = new ArrayList<>();

		this.setarPosicaoInicial();

		if (this.linhaInicial != 0) {
			filhos.add(this.criarFilho(this.linhaInicial - 1, this.colunaInicial));
		}

		if (this.linhaInicial < (TOTAL_LINHAS - 1)) {
			filhos.add(this.criarFilho(this.linhaInicial + 1, this.colunaInicial));
		}

		if (this.colunaInicial != 0) {
			filhos.add(this.criarFilho(this.linhaInicial, this.colunaInicial - 1));
		}

		if (this.colunaInicial < (TOTAL_COLUNAS - 1)) {
			filhos.add(this.criarFilho(this.linhaInicial, this.colunaInicial + 1));
		}

		return filhos;
	}

	private void setarPosicaoInicial() {
		for (short i = 0; i < TOTAL_LINHAS; i++) {
			for (short j = 0; j < TOTAL_COLUNAS; j++) {
				if (this.estado[i][j] == 0) {
					this.linhaInicial = i;
					this.colunaInicial = j;
				}
			}
		}
	}

	private Estado criarFilho(int linha, int coluna) {
		Estado tabuleiroCopia = criarCopiaComUmCustoMaior();
		tabuleiroCopia.mover(linha, coluna);
		tabuleiroCopia.gerarHeuristica();
		tabuleiroCopia.calcularValor();
		return tabuleiroCopia;
	}

	private void calcularValor() {
		this.valor = this.heuristica + this.custo;
	}

	private void gerarHeuristica() {
		int[][] estadoFinal = Principal.estadoFinal;
		int esperado = 0;
		int count = 0;
		for (int linha = 0; linha < TOTAL_LINHAS; linha++) {
			for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
				int valor = estado[linha][coluna];
				esperado++;
				if (valor != 0 && valor != esperado) {
					count += Math.abs(linha - obterLinhaFinal(estadoFinal, valor))
							+ Math.abs(coluna - obterColunaFinal(estadoFinal, valor));
				}
			}
		}

		this.heuristica = count;
	}

	public int obterColunaFinal(int[][] estadoFinal, int valor) {
		for (int linha = 0; linha < TOTAL_LINHAS; linha++) {
			for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
				if (estadoFinal[linha][coluna] == valor) {
					return coluna;
				}
			}
		}
		return -1;
	}

	public int obterLinhaFinal(int[][] estadoFinal, int valor) {
		for (int linha = 0; linha < TOTAL_LINHAS; linha++) {
			for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
				if (estadoFinal[linha][coluna] == valor) {
					return linha;
				}
			}
		}
		return -1;
	}

	public Estado mover(int linhaFinal, int colunaFinal) {
		int valorASerMovido = this.estado[linhaFinal][colunaFinal];
		this.estado[this.linhaInicial][this.colunaInicial] = valorASerMovido;
		this.estado[linhaFinal][colunaFinal] = 0;
		return this;
	}

	public Estado criarCopiaComUmCustoMaior() {
		return new Estado(this.estado, this.custo + 1, this.linhaInicial, this.colunaInicial, this);
	}

	// This function returns true if given 8 puzzle is solvable.
	public boolean solucionavel() {
		// Count inversions in given 8 puzzle
		int invCount = this.getInvCount(this.estado);

		// return true if inversion count is even.
		return (invCount % 2 == 0);
	}

	private int getInvCount(int array[][]) {
		int inversaoCont = 0;
		int arr[] = new int[TOTAL_NUMEROS];
		int cont = 0;
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				arr[cont] = array[i][j];
				cont++;
			}
		}
		for (int i = 0; i < TOTAL_NUMEROS - 1; i++)
			for (int j = i + 1; j < TOTAL_NUMEROS; j++)
				// Value 0 is used for empty space
				if (arr[i] > 0 && arr[j] > 0 && arr[i] > arr[j])
					inversaoCont++;
		return inversaoCont;
	}

	public boolean igualAoEstado(int[][] estadoFinal) {
		return Arrays.deepEquals(estadoFinal, this.estado);
	}

	@Override
	public String toString() {
		String tab = "";
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				tab = tab + " |  " + this.estado[i][j] + "  |  ";
			}
			tab = tab + "\n";
		}
		return tab;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(estado);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		if (!Arrays.deepEquals(estado, other.estado))
			return false;
		return true;
	}

}
