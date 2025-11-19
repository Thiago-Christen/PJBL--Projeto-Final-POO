package PJBL;

public abstract class Pessoa {
    protected int id;
    protected String nome;
    protected String cpf;
    protected String telefone;
    protected String email;

    public Pessoa(int id,String nome, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public abstract void exibirResumo();

    @Override
    public String toString() {
        return id + " - " + nome + " | " + telefone + " | " + email;
    }
}
