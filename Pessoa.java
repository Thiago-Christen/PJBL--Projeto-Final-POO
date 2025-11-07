package PJBL;

public abstract class Pessoa {
    protected String nome;
    protected String cpf;
    protected String telefone;
    protected String email;

    public Pessoa(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }
}
