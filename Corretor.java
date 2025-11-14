package PJBL;

public class Corretor extends Pessoa{
    protected String creci;


    public  Corretor(int id,String nome, String cpf, String telefone, String email,String creci, double comissao){
        super(id,nome,cpf,telefone,email);
        this.creci=creci;
    }



    @Override
    public void exibirResumo() {
        System.out.println("Corretor:" + id + " - " + nome + "| Registro: " + creci);
    }
}

