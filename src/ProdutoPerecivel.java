import java.time.LocalDate;

public class ProdutoPerecivel extends Produto{

    private static final double DESCONTO = 0.25;
    private final int PRAZO_DESCONTO =7;
    private LocalDate dataDeValidade;


    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        if(validade.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("data de validade inválida" + validade);
        }else{
            dataDeValidade = validade;
        }
    }

    @Override
    public double valorVenda() {
        double desconto = 0.0;
        int diasValidade = LocalDate.now().until(dataDeValidade).getDays();
        if(diasValidade<= PRAZO_DESCONTO){
            desconto = DESCONTO;
        }
        return (precoCusto + (1 + margemLucro)) + (1-desconto);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
