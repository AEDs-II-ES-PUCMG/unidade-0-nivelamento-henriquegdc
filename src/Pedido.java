import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {

	/** Quantidade máxima de produtos de um pedido */
	private static final int MAX_PRODUTOS = 10;

	/** Porcentagem de desconto para pagamentos à vista */
	private static final double DESCONTO_PG_A_VISTA = 0.15;

	/** Vetor refatorado para armazenar os itens do pedido */
	private ItemDePedido[] itens;

	/** Data de criação do pedido */
	private LocalDate dataPedido;

	/** Indica a quantidade total de itens (posições ocupadas) no pedido até o momento */
	private int quantProdutos = 0;

	/** Indica a forma de pagamento do pedido sendo: 1, pagamento à vista; 2, pagamento parcelado */
	private int formaDePagamento;

	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		this.itens = new ItemDePedido[MAX_PRODUTOS];
		this.quantProdutos = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}

	public boolean incluirProduto(Produto novo) {
		if (quantProdutos < MAX_PRODUTOS) {
			itens[quantProdutos++] = new ItemDePedido(novo, 1, novo.valorDeVenda());
			return true;
		}
		return false;
	}

	public double valorFinal() {
		double valorPedido = 0;

		for (int i = 0; i < quantProdutos; i++) {
			valorPedido += itens[i].calcularSubtotal();
		}

		if (formaDePagamento == 1) {
			valorPedido = valorPedido * (1.0 - DESCONTO_PG_A_VISTA);
		}
		return valorPedido;
	}

	@Override
	public String toString() {
		StringBuilder stringPedido = new StringBuilder();
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		stringPedido.append("Data do pedido: " + formatoData.format(dataPedido) + "\n");
		stringPedido.append("Pedido com " + quantProdutos + " produtos.\n");
		stringPedido.append("Produtos no pedido:\n");
		for (int i = 0; i < quantProdutos; i++ ) {
			stringPedido.append(itens[i].getProduto().toString() + " | Qtd: " + itens[i].getQuantidade() + "\n");
		}

		stringPedido.append("Pedido pago ");
		if (formaDePagamento == 1) {
			stringPedido.append("à vista. Percentual de desconto: " + String.format("%.2f", DESCONTO_PG_A_VISTA * 100) + "%\n");
		} else {
			stringPedido.append("parcelado.\n");
		}

		stringPedido.append("Valor total do pedido: R$ " + String.format("%.2f", valorFinal()));

		return stringPedido.toString();
	}

	@Override
	public boolean equals(Object obj) {
		Pedido outro = (Pedido)obj;
		return this.dataPedido.equals(outro.dataPedido);
	}

	public void mesclarPedido(Pedido outroPedido) {
		int novosItens = 0;
		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			boolean existe = false;
			for (int j = 0; j < this.quantProdutos; j++) {
				if (this.itens[j].equals(outroPedido.itens[i])) {
					existe = true;
					break;
				}
			}
			if (!existe) novosItens++;
		}

		if (this.quantProdutos + novosItens > MAX_PRODUTOS) {
			throw new IllegalStateException("A mesclagem excederá a capacidade máxima do vetor.");
		}

		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			ItemDePedido itemSecundario = outroPedido.itens[i];
			boolean existe = false;

			for (int j = 0; j < this.quantProdutos; j++) {
				if (this.itens[j].equals(itemSecundario)) {
					existe = true;
					this.itens[j].setQuantidade(this.itens[j].getQuantidade() + itemSecundario.getQuantidade());

					if (itemSecundario.getPrecoVenda() < this.itens[j].getPrecoVenda()) {
						this.itens[j].setPrecoVenda(itemSecundario.getPrecoVenda());
					}
					break;
				}
			}

			if (!existe) {
				this.itens[this.quantProdutos++] = itemSecundario;
			}
		}

		for (int i = 0; i < outroPedido.quantProdutos; i++) {
			outroPedido.itens[i] = null;
		}
		outroPedido.quantProdutos = 0;
	}

	public void imprimirRecibo() {
		System.out.println("\n===== CUPOM FISCAL =====");
		double totalGeral = 0;

		for (int i = 0; i < quantProdutos; i++) {
			if (itens[i] != null) {
				Produto p = itens[i].getProduto();
				int qtd = itens[i].getQuantidade();
				double precoUn = itens[i].getPrecoVenda();
				double subtotal = qtd * precoUn;

				if (qtd > 10) {
					subtotal *= 0.95;
				}

				System.out.printf("- %s | Qtd: %d | Preço: R$ %.2f | Subtotal: R$ %.2f\n",
						p.descricao, qtd, precoUn, subtotal);

				totalGeral += subtotal;
			}
		}

		System.out.println("------------------------");
		if (formaDePagamento == 1) {
			double valorDesconto = totalGeral * DESCONTO_PG_A_VISTA;
			System.out.printf("Desconto pg. à vista: R$ %.2f\n", valorDesconto);
			totalGeral -= valorDesconto;
		}

		System.out.printf("TOTAL GERAL: R$ %.2f\n", totalGeral);
		System.out.println("========================\n");
	}
}