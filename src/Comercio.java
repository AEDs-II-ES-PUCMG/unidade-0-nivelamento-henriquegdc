import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int). */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantidade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = null;
        quantosProdutos = 0;

        try {
            Scanner leitor = new Scanner(new File(nomeArquivoDados));
            if (leitor.hasNextLine()) {
                int n = Integer.parseInt(leitor.nextLine().trim());
                vetorProdutos = new Produto[n + MAX_NOVOS_PRODUTOS];

                while (leitor.hasNextLine() && quantosProdutos < n) {
                    String linha = leitor.nextLine();
                    if (!linha.trim().isEmpty()) {
                        vetorProdutos[quantosProdutos] = Produto.criarDoTexto(linha);
                        quantosProdutos++;
                    }
                }
            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado. Inicializando com estrutura vazia.");
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        } catch (Exception e) {
            System.out.println("Erro durante a leitura do ficheiro: " + e.getMessage());
            if (vetorProdutos == null) vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        }
        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. */
    static void localizarProdutos(){
        cabecalho();
        System.out.print("Digite o nome (descrição) do produto que procura: ");
        String termoBusca = teclado.nextLine();
        
        Produto produtoFicticio = null;
        try {
            produtoFicticio = Produto.criarDoTexto("1;" + termoBusca + ";0;0");
        } catch (Exception e) {
            System.out.println("Erro ao inicializar pesquisa.");
            return;
        }

        boolean localizado = false;
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i] != null && produtosCadastrados[i].equals(produtoFicticio)) {
                System.out.println("\nArtigo Encontrado:");
                System.out.println(produtosCadastrados[i].toString());
                localizado = true;
                break;
            }
        }
        
        if (!localizado) {
            System.out.println("Produto não localizado no sistema.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor.
     */
    static void cadastrarProduto(){
        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("A capacidade máxima de registo foi atingida.");
            return;
        }
        
        cabecalho();
        System.out.println("SELECIONE O TIPO DO PRODUTO");
        System.out.println("1 - Não Perecível");
        System.out.println("2 - Perecível");
        System.out.print("Opção: ");
        String tipo = teclado.nextLine().trim();

        if (!tipo.equals("1") && !tipo.equals("2")) {
            System.out.println("Tipo inválido. A abortar...");
            return;
        }

        System.out.print("Descrição do artigo: ");
        String descricao = teclado.nextLine();

        System.out.print("Preço de Custo (ex: 12.50): ");
        String precoCusto = teclado.nextLine().replace(",", ".");

        System.out.print("Margem de Lucro (ex: 0.25 para 25%): ");
        String margem = teclado.nextLine().replace(",", ".");

        String construtorLinha = tipo + ";" + descricao + ";" + precoCusto + ";" + margem;

        if (tipo.equals("2")) {
            System.out.print("Data de Validade (dd/mm/aaaa): ");
            String validade = teclado.nextLine();
            construtorLinha += ";" + validade;
        }

        try {
            Produto novo = Produto.criarDoTexto(construtorLinha);
            produtosCadastrados[quantosProdutos] = novo;
            quantosProdutos++;
            System.out.println("Artigo registado com sucesso!");
        } catch (Exception e) {
            System.out.println("Falha no registo: " + e.getMessage());
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){
        try {
            FileWriter escritor = new FileWriter(nomeArquivo);
            escritor.write(quantosProdutos + "\n");
            
            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i] != null) {
                    escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
                }
            }
            escritor.close();
            System.out.println("\nDados guardados no ficheiro com sucesso.");
        } catch (IOException e) {
            System.out.println("Não foi possível gravar no ficheiro: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}