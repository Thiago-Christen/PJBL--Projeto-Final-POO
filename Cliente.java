package PJBL;

public class Cliente extends Pessoa{
    protected int mesNascimento;
    protected int anoNascimento;
    protected double saldoBancario;

    public Cliente(String nome, String cpf, String telefone, String email, int mesNascimento, int anoNascimento,  double saldoBancario) {
        super(nome, cpf, telefone, email);
        this.mesNascimento = mesNascimento;
        this.anoNascimento = anoNascimento;
        this.saldoBancario = saldoBancario;
    }
}