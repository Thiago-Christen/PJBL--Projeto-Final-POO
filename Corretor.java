package PJBL;

public class Corretor extends Pessoa{
    protected String creci;
    protected double comissao;

    public Corretor(int id,String nome, String cpf, String telefone, String email,String creci, double comissao){
        super(id,nome,cpf,telefone,email);
        this.creci = creci;
        this.comissao = comissao;
    }

    @Override
    public void exibirResumo() {
        System.out.println("Corretor: " + toString() + " | Registro: " + creci);
    }
}
