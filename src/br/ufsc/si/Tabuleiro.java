package br.ufsc.si;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tabuleiro {

	private static final int TOTAL_COLUNAS = 3;
	private static final int TOTAL_LINHAS = 3;
	private static final int TOTAL_NUMEROS = TOTAL_COLUNAS * TOTAL_LINHAS;
	int[][] tabuleiro;
	int custo;
	int heuristica;
	int valor;
	int linhaInicial;
	int colunaInicial;
	Tabuleiro pai = null;

	public Tabuleiro() {

	}

	public Tabuleiro(int[][] valores) {
		this.tabuleiro = Arrays.copyOf(valores, valores.length);
	}

	public Tabuleiro(int[][] valores, int custo) {
		this.tabuleiro = Arrays.copyOf(valores, valores.length);
		this.custo = custo;
		this.gerarHeuristica();
		this.calcularValor();
	}

	public Tabuleiro(int[][] valores, int custo, int linhaInicial, int colunaInicial, Tabuleiro pai) {
		this.tabuleiro = new int[TOTAL_LINHAS][TOTAL_COLUNAS];
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				this.tabuleiro[i][j] = valores[i][j];
			}
		}
		this.custo = custo;
		this.linhaInicial = linhaInicial;
		this.colunaInicial = colunaInicial;
		this.pai = pai;
	}

	public int[][] criarTabuleiro() {
		List<Integer> valoresLista = new ArrayList<>();
		for (int i = 0; i < TOTAL_NUMEROS; i++) {
			valoresLista.add(i);
		}
		int[][] tabuleiro = new int[TOTAL_LINHAS][TOTAL_COLUNAS];
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				int indexArray = new Random().nextInt(valoresLista.size());
				tabuleiro[i][j] = valoresLista.get(indexArray);

				valoresLista.remove(indexArray);
			}
		}
		return tabuleiro;
	}

	public void mostrarTabuleiro() {
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				System.out.print(" |  " + this.tabuleiro[i][j] + "  |  ");
			}
			System.out.print("\n");
		}
	}

	public List<Tabuleiro> criarFilhos() {
		List<Tabuleiro> filhos = new ArrayList<>();

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
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				if (this.tabuleiro[i][j] == 0) {
					this.linhaInicial = i;
					this.colunaInicial = j;
				}
			}
		}
	}

	private Tabuleiro criarFilho(int linha, int coluna) {
		Tabuleiro tabuleiroCopia = criarCopiaComUmCustoMaior();
		tabuleiroCopia.mover(linha, coluna);
		tabuleiroCopia.gerarHeuristica();
		tabuleiroCopia.calcularValor();
		return tabuleiroCopia;
	}

	private void calcularValor() {
		this.valor = this.heuristica + this.custo;
	}

	private void gerarHeuristica() {
		int[][] objetivoFinal = Principal.tabuleiroObjetivo.tabuleiro;
		int expected = 0;
		int count = 0;
		for (int row = 0; row < TOTAL_LINHAS; row++) {
			for (int col = 0; col < TOTAL_COLUNAS; col++) {
				int value = tabuleiro[row][col];
				expected++;
				if (value != 0 && value != expected) {
					count += Math.abs(row - getRow(objetivoFinal, value))
							+ Math.abs(col - getCol(objetivoFinal, value));
				}
			}
		}

		this.heuristica = count;
	}

	// helper to get the column of a value.
	public int getCol(int[][] a, int value) {
		for (int row = 0; row < a.length; row++) {
			for (int col = 0; col < a[row].length; col++) {
				if (a[row][col] == value) {
					return col;
				}
			}
		}
		return -1;
	}

	// helper to get the row of a value.
	public int getRow(int[][] a, int value) {
		for (int row = 0; row < a.length; row++) {
			for (int col = 0; col < a[row].length; col++) {
				if (a[row][col] == value) {
					return row;
				}
			}
		}
		return -1;
	}

	public Tabuleiro mover(int linhaFinal, int colunaFinal) {
		int valorASerMovido = this.tabuleiro[linhaFinal][colunaFinal];
		this.tabuleiro[this.linhaInicial][this.colunaInicial] = valorASerMovido;
		this.tabuleiro[linhaFinal][colunaFinal] = 0;
		return this;
	}

	public Tabuleiro criarCopiaComUmCustoMaior() {
		return new Tabuleiro(this.tabuleiro, this.custo + 1, this.linhaInicial, this.colunaInicial, this);
	}

	// This function returns true if given 8 puzzle is solvable.
	public boolean solucionavel() {
		// Count inversions in given 8 puzzle
		int invCount = this.getInvCount(this.tabuleiro);

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

	public boolean igualA(Tabuleiro tabuleiro) {
		return Arrays.deepEquals(tabuleiro.tabuleiro, this.tabuleiro);
	}

	@Override
	public String toString() {
		String tab = "";
		for (int i = 0; i < TOTAL_LINHAS; i++) {
			for (int j = 0; j < TOTAL_COLUNAS; j++) {
				tab = tab + " |  " + this.tabuleiro[i][j] + "  |  ";
			}
			tab = tab + "\n";
		}
		return tab;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(tabuleiro);
		result = prime * result + valor;
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
		Tabuleiro other = (Tabuleiro) obj;
		if (!Arrays.deepEquals(tabuleiro, other.tabuleiro))
			return false;
		if (valor != other.valor)
			return false;
		return true;
	}

}
