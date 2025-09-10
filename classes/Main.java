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
        System.out.println("Digite o CPF do paciente:");
        String cpf = scanner.nextLine();
        System.out.println("Digite a idade do paciente:");
        int idade = scanner.nextInt();

        Paciente paciente = new Paciente(nome, cpf, idade);
        paciente.mostrarPaciente();
    }
}
