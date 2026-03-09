import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
	/**
     * Inicializador privado.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.0)
     * @param margemLucro Margem de lucro (mínimo 0.0)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		// Os valores são validados com >= 0.0 para permitir a criação do produto fictício de pesquisa no Comercio.java
		if ((desc.length() >= 3) && (precoCusto >= 0.0) && (margemLucro >= 0.0)) {
			this.descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
	
    @Override
	public String toString() {
    	NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		return String.format("%s: %s", descricao, moeda.format(valorDeVenda()));
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Produto)) return false;
        Produto produto = (Produto) obj;
        return this.descricao.equalsIgnoreCase(produto.descricao);
    }

    /** Assinatura do método para forçar a implementação em cada tipo específico */
    public abstract String gerarDadosTexto();

    /**
     * Padrão Factory para converter uma linha de texto do CSV de volta para a Instância correta
     */
    public static Produto criarDoTexto(String linha) {
        String[] dados = linha.split(";");
        int tipo = Integer.parseInt(dados[0]);
        String desc = dados[1];
        double custo = Double.parseDouble(dados[2]);
        double margem = Double.parseDouble(dados[3]);

        if (tipo == 1) {
            return new ProdutoNaoPerecivel(desc, custo, margem);
        } else if (tipo == 2) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate validade = LocalDate.parse(dados[4], formatter);
            return new ProdutoPerecivel(desc, custo, margem, validade);
        } else {
            throw new IllegalArgumentException("Tipo de produto desconhecido.");
        }
    }
}