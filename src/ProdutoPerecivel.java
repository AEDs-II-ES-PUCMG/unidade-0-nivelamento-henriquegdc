import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        if(validade.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de validade inválida: " + validade);
        } else {
            this.dataDeValidade = validade;
        }
    }

    @Override
    public double valorDeVenda() {
        double valorNormal = super.valorDeVenda();
        long diasValidade = ChronoUnit.DAYS.between(LocalDate.now(), dataDeValidade);
        
        if(diasValidade <= PRAZO_DESCONTO){
            return valorNormal * (1.0 - DESCONTO);
        }
        return valorNormal;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() + " - Validade: " + dataDeValidade.format(formatter);
    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(Locale.US, "2;%s;%.2f;%.2f;%s", descricao, precoCusto, margemLucro, dataDeValidade.format(formatter));
    }
}