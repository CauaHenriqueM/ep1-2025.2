package hospital;

public class Main {
    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        hospital.loadAll();
        ConsoleMenu menu = new ConsoleMenu(hospital);
        menu.run();
        hospital.saveAll();
        System.out.println("Programa finalizado. Dados salvos.");
    }
}
