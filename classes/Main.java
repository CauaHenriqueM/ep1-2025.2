package classes;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite um nome:");
        Paciente paciente1 = new Paciente(scanner.nextLine(), scanner.nextLine(), scanner.nextInt());

        System.out.println("Paciente 1: " + paciente1.getNome() + ", CPF: " + paciente1.getCpf() + ", Idade: " + paciente1.getIdade());
    }
}
