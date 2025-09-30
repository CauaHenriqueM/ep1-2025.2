package classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int escolha;
        System.out.println("Bem-vindo ao sistema de gerenciamento de pacientes!");
        System.out.println("Digite 1 para cadastrar um paciente. Digite 2 para realizar uma consulta. Digite 0 para sair.");
        do {
            escolha = scanner.nextInt();
            switch(escolha){
                case 1:
                    Cadastro();
                    break;
                case 2:
                    Consulta();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
            }
        }while(escolha!=0);

    }

    public static void Cadastro(){
        boolean especial = false;
        boolean valido = false;
        String nomePlano = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do paciente:");
        String nome = scanner.nextLine();
        System.out.println("Digite o CPF do paciente, sem pontos ou traços:");
        String cpf = "";
        while(!valido){
            cpf = scanner.nextLine();
            valido = VerificarCpf(cpf);
        }
        System.out.println("Digite a idade do paciente:");
        int idade = scanner.nextInt();
        System.out.println("Paciente tem plano de saúde? Digite 1 para sim e 0 para não:");
        int plano = scanner.nextInt();
        
        switch(plano){
            case 1:
                especial = true;
                System.out.println("Qual o plano de saude? ");
                nomePlano = scanner.next();
                break;
            case 0:
                System.out.println("Paciente cadastrado sem plano de saúde.");
                break;
        }
        if(especial){
            PacienteEspecial pacienteEspecial = new PacienteEspecial(nome, cpf, idade, especial, nomePlano);
            pacienteEspecial.mostrarPaciente();
            System.out.println("Plano de saúde: " + pacienteEspecial.getPlano());
            return;
        }
        Paciente paciente = new Paciente(nome, cpf, idade);
        paciente.mostrarPaciente();
    }

    public static void Consulta(){
        System.out.println("Vai tomar no cu");
    }

    public static boolean VerificarCpf(String cpf) {
    
    cpf = cpf.trim();
    if (cpf.length() != 11) {
        System.out.println("CPF inválido: deve conter 11 dígitos.");
        return false;
    }
    char primeiroDigito = cpf.charAt(0);
    boolean todosIguais = cpf.chars().allMatch(c -> c == primeiroDigito);
    if (todosIguais) {
        System.out.println("CPF inválido: todos os dígitos são iguais.");
        return false;
    }
    int soma1 = 0;
    for (int i = 0; i < 9; i++) {
        soma1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
    }
    int resto1 = soma1 % 11;
    int digito1 = (resto1 < 2) ? 0 : 11 - resto1;
    int soma2 = 0;
    for (int i = 0; i < 10; i++) {
        soma2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
    }
    int resto2 = soma2 % 11;
    int digito2 = (resto2 < 2) ? 0 : 11 - resto2;

    if (digito1 == Character.getNumericValue(cpf.charAt(9)) &&
        digito2 == Character.getNumericValue(cpf.charAt(10))){
        return true;
    } else {
        System.out.println("CPF inválido: dígitos verificadores não conferem.");
        return false;
    }
    }

    public static void escrevendoArquivo(){///////TESTEEEEE
        String nomeArquivo = "planos.txt";
        String pasta = "planos";
        new File(pasta).mkdirs();
        try (BufferedWriter save = new BufferedWriter(new FileWriter(pasta+"/"+nomeArquivo))){
            save.write("bola, pinto, minhoca, suco");
        }catch(IOException e){
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public static void lendoArquivo(){////////TESTEEEEE
        String nomeArquivo = "planos.txt";
        String pasta = "planos";
        try (BufferedReader read = new BufferedReader(new FileReader(pasta+"/"+nomeArquivo))){
            String linha;
            while((linha = read.readLine()) != null){
                String [] planos = linha.split(",");
                for(String plano : planos){
                    System.out.println(plano.trim());
                }
            }
        }catch(IOException e){
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}


