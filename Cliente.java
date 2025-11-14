package PJBL;

public class Cliente extends Pessoa{
    public Cliente(int id,String nome, String cpf, String telefone, String email, int mesNascimento, int anoNascimento,  double saldoBancario) {
        super(id,nome, cpf, telefone, email);
    }

    @Override
    public void exibirResumo() {
        System.out.println("Cliente: " + id + " - " + nome + " | " + telefone + " | " + email);
    }
}