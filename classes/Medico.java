package classes;
public class Medico extends Pessoa{
    private boolean ehMedico;

    public Medico(String nome, String cpf, int idade, boolean ehMedico){
        super(nome, cpf, idade);
        this.ehMedico = ehMedico;
    }
    
}