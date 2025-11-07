package PJBL;

public class Corretor extends Pessoa{
    protected String creci;
    protected double comissao;

    public  Corretor(String nome, String cpf, String telefone, String email,String creci, double comissao){
        super(nome,cpf,telefone,email);
        this.creci=creci;
        this.comissao=comissao;
    }
}
