package classes;
public class PacienteEspecial extends Paciente{
    private boolean ehEspecial;
    private String plano;


    public PacienteEspecial(String nome, String cpf, int idade, boolean ehEspecial, String plano){
    super(nome, cpf, idade);
    this.ehEspecial = ehEspecial;
    this.plano = plano;

    System.out.println("Paciente especial cadastrado.");
    }
    //getters e setters
    public String getPlano() {
        return this.plano;
    }
    public void setPlano(String plano) {
        this.plano = plano;
    }
    public String getNome() {
        return super.getNome();
    }
    public String getCpf() {
        return super.getCpf();
    }
    public int getIdade() {
        return super.getIdade();
    }
    public boolean getEhEspecial() {
    return this.ehEspecial;
    }
    public void setEhEspecial(boolean ehEspecial) {
    this.ehEspecial = ehEspecial;
    }
    public void setNome(String nome){
        super.setNome(nome);
    }
    public void setCpf(String cpf){
     super.setCpf(cpf);
    }
    public void setIdade(int idade){
        super.setIdade(idade);
    }
}



