import java.time.LocalDate;

public class ProdutoPerecivel extends Produto{

    private static final double Desconto = 0.25;
    private final int PRAZO_DESCONTO =7;
    private LocalDate dataDeValidade;


    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
    }

    @Override
    public double valorDeVenda() {
        return super.valorDeVenda();
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
