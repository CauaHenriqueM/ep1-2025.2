package classes;
public class Medico{
    private boolean ehMedico;
    private String licenca;
    private String nome;
    private String cpf;
    private int idade;

    public Medico(String nome, String cpf, int idade, boolean ehMedico, String licenca){
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.ehMedico = ehMedico;
        this.licenca = licenca;
    }

}