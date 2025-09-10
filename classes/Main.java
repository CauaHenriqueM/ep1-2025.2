package classes;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int escolha;
        do {
            escolha = scanner.nextInt();
            switch(escolha){
                case 1:
                    Cadastro();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
            }
        }while(escolha!=0);

    }

    public static void Cadastro(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do paciente:");
        String nome = scanner.nextLine();
        System.out.println("Digite o CPF do paciente, sem pontos ou traços:");
        String cpf = scanner.nextLine();
        VerificarCpf(cpf);
        System.out.println("");
        System.out.println("Digite a idade do paciente:");
        int idade = scanner.nextInt();

        Pessoa paciente = new Pessoa(nome, cpf, idade);
        paciente.mostrarPessoa();
    }

    public static String VerificarCpf(String cpf) {
    cpf = cpf.trim();
    if (cpf.length() != 11) {
        System.out.println("CPF inválido: deve conter 11 dígitos.");
        return null;
    }
    char primeiroDigito = cpf.charAt(0);
    boolean todosIguais = cpf.chars().allMatch(c -> c == primeiroDigito);
    if (todosIguais) {
        System.out.println("CPF inválido: todos os dígitos são iguais.");
        return null;
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
        digito2 == Character.getNumericValue(cpf.charAt(10))) {
        return cpf;
    } else {
        System.out.println("CPF inválido: dígitos verificadores não conferem.");
        return null;
    }
    }
}


