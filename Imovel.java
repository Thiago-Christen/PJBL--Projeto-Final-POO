package PJBL;

public abstract class Imovel {
    protected int id;
    protected String descricao;
    protected String endereco;
    protected String complemento;
    protected boolean disponivelVenda;
    protected boolean disponivelAluguel;

    public Imovel(int id, String descricao, String endereco, String complemento,
                  boolean disponivelVenda, boolean disponivelAluguel) {
        this.id = id;
        this.descricao = descricao;
        this.endereco = endereco;
        this.complemento = complemento;
        this.disponivelVenda = disponivelVenda;
        this.disponivelAluguel = disponivelAluguel;
    }
}

