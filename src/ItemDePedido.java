public class ItemDePedido {

    private Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public double calcularSubtotal() {
        return this.quantidade * this.precoVenda;
    }

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemDePedido outro = (ItemDePedido) obj;
        return this.produto.equals(outro.getProduto());
    }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
}