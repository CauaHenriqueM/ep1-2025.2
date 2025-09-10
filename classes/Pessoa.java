package classes;

public class Pessoa {
    private String nome;
    private String cpf;
    private int idade;

    public Pessoa(String nome, String cpf, int idade){
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
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setCpf(String cpf){
     this.cpf = cpf;
    }
    public void setIdade(int idade){
        this.idade = idade;
    }

    public void mostrarPessoa(){
        System.out.println("Nome: " + this.nome);
        System.out.println("CPF: " + this.cpf);
        System.out.println("Idade: " + this.idade);
    }
    
}

