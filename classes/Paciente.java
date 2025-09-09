package classes;

public class Paciente {
    private String nome;
    private String cpf;
    private int idade;

    public Paciente(String nome, String cpf, int idade){
    this.nome = nome;
    this.cpf = cpf;
    this.idade = idade;
    }

    public String getNome() {
        return this.nome;
    }
    public String getCpf() {
        return this.cpf;
    }
    public int getIdade() {
        return this.idade;
    }
}

