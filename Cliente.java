package PJBL;

public class Cliente extends Pessoa{
    private int mesNascimento;
    private int anoNascimento;
    private double saldoBancario;

    public Cliente(int id,String nome, String cpf, String telefone, String email, int mesNascimento, int anoNascimento, double saldoBancario) {
        super(id,nome, cpf, telefone, email);
        this.mesNascimento = mesNascimento;
        this.anoNascimento = anoNascimento;
        this.saldoBancario = saldoBancario;
    }

    @Override
    public void exibirResumo() {
        System.out.println("Cliente: " + toString());
    }
}
