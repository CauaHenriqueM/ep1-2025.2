package hospital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner sc = new Scanner(System.in);
    private Hospital hospital;
    private DateTimeFormatter dtfmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsoleMenu(Hospital hospital){ this.hospital = hospital; }

    public void run(){
        while (true){
            System.out.println("\n=== Sistema Hospitalar ===");
            System.out.println("1) Cadastrar paciente");
            System.out.println("2) Cadastrar médico");
            System.out.println("3) Cadastrar plano de saúde");
            System.out.println("4) Agendar consulta");
            System.out.println("5) Concluir consulta");
            System.out.println("6) Internar paciente");
            System.out.println("7) Cancelar/Alta internação");
            System.out.println("8) Relatórios");
            System.out.println("9) Sair");
            System.out.print("Escolha: ");
            String op = sc.nextLine().trim();
            try{
                switch (op){
                    case "1": cadastroPaciente(); break;
                    case "2": cadastroMedico(); break;
                    case "3": cadastroPlano(); break;
                    case "4": agendarConsulta(); break;
                    case "5": concluirConsulta(); break;
                    case "6": internarPaciente(); break;
                    case "7": cancelarInternacao(); break;
                    case "8": relatoriosMenu(); break;
                    case "9": return;
                    default: System.out.println("Opção inválida");
                }
            } catch (Exception e){
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private void cadastroPaciente(){
        System.out.print("Nome: "); String name = sc.nextLine();
        System.out.print("CPF: "); String cpf = sc.nextLine();
        System.out.print("Idade: "); int age = Integer.parseInt(sc.nextLine());
        System.out.print("Tem plano de saúde? (s/n): "); String tem = sc.nextLine().trim().toLowerCase();
        if (tem.equals("s")){
            System.out.print("ID do plano: "); String pid = sc.nextLine();
            HealthPlan plan = hospital.getPlan(pid);
            if (plan==null){ System.out.println("Plano não encontrado. Cadastre o plano primeiro."); return; }
            SpecialPatient sp = new SpecialPatient(name, cpf, age, plan);
            if (hospital.addPatient(sp)) System.out.println("Paciente especial cadastrado com sucesso.");
            else System.out.println("CPF já cadastrado.");
        } else {
            Patient p = new Patient(name, cpf, age);
            if (hospital.addPatient(p)) System.out.println("Paciente cadastrado com sucesso.");
            else System.out.println("CPF já cadastrado.");
        }
    }

    private void cadastroMedico(){
        System.out.print("Nome: "); String name = sc.nextLine();
        System.out.print("CRM: "); String crm = sc.nextLine();
        System.out.print("Especialidade: "); String spec = sc.nextLine();
        System.out.print("Custo da consulta: "); double cost = Double.parseDouble(sc.nextLine());
        Doctor d = new Doctor(name, crm, spec, cost);
        System.out.print("Adicionar slots à agenda agora? (s/n): "); if (sc.nextLine().trim().equalsIgnoreCase("s")){
            while (true){
                System.out.print("Slot (yyyy-MM-dd HH:mm) ou vazio pra sair: "); String slot = sc.nextLine();
                if (slot.isBlank()) break;
                LocalDateTime dt = LocalDateTime.parse(slot, dtfmt);
                d.addAgendaSlot(dt.toString());
            }
        }
        if (hospital.addDoctor(d)) System.out.println("Médico cadastrado."); else System.out.println("CRM já cadastrado.");
    }

    private void cadastroPlano(){
        System.out.print("ID do plano: "); String id = sc.nextLine();
        System.out.print("Nome do plano: "); String name = sc.nextLine();
        HealthPlan p = new HealthPlan(id, name);
        System.out.print("Internação gratuita <7 dias? (s/n): "); if (sc.nextLine().trim().equalsIgnoreCase("s")) p.setFreeShortInternment(true);
        System.out.println("Adicione descontos por especialidade (ex: cardiologia 0.2) ou vazio pra sair:");
        while (true){
            System.out.print("Especialidade e desconto: "); String line = sc.nextLine();
            if (line.isBlank()) break;
            String[] parts = line.split("\\s+");
            p.setDiscount(parts[0], Double.parseDouble(parts[1]));
        }
        if (hospital.addPlan(p)) System.out.println("Plano cadastrado."); else System.out.println("ID já existe.");
    }

    private void agendarConsulta(){
        System.out.print("ID consulta: "); String id = sc.nextLine();
        System.out.print("CPF do paciente: "); String cpf = sc.nextLine();
        System.out.print("CRM do médico: "); String crm = sc.nextLine();
        System.out.print("Data e hora (yyyy-MM-dd HH:mm): "); String dt = sc.nextLine();
        System.out.print("Local (sala): "); String local = sc.nextLine();
        LocalDateTime ldt = LocalDateTime.parse(dt, dtfmt);
        Consultation c = new Consultation(id, cpf, crm, ldt, local);
        boolean ok = hospital.scheduleConsultation(c);
        System.out.println(ok?"Consulta agendada.":"Falha ao agendar (conflito ou dados inválidos).");
    }

    private void concluirConsulta(){
        System.out.print("ID da consulta: "); String id = sc.nextLine();
        System.out.print("Diagnóstico: "); String diag = sc.nextLine();
        System.out.print("Prescrição: "); String pres = sc.nextLine();
        boolean ok = hospital.concludeConsultation(id, diag, pres);
        System.out.println(ok?"Consulta concluída.":"ID não encontrado.");
    }

    private void internarPaciente(){
        System.out.print("ID internação: "); String id = sc.nextLine();
        System.out.print("CPF do paciente: "); String cpf = sc.nextLine();
        System.out.print("CRM do médico responsável: "); String crm = sc.nextLine();
        System.out.print("Data entrada (yyyy-MM-dd): "); LocalDate entry = LocalDate.parse(sc.nextLine());
        System.out.print("Quarto (id): "); String room = sc.nextLine();
        System.out.print("Custo estimado: "); double cost = Double.parseDouble(sc.nextLine());
        Internment it = new Internment(id, cpf, crm, entry, room, cost);
        boolean ok = hospital.admitPatient(it);
        System.out.println(ok?"Internação realizada.":"Falha — quarto ocupado ou dados inválidos.");
    }

    private void cancelarInternacao(){
        System.out.print("ID internação: "); String id = sc.nextLine();
        System.out.print("Deseja cancelar (c) ou dar alta (a)? "); String op = sc.nextLine().trim().toLowerCase();
        if (op.equals("c")){ boolean ok = hospital.cancelInternment(id); System.out.println(ok?"Internação cancelada.":"ID não encontrado."); }
        else if (op.equals("a")){ System.out.print("Data de saída (yyyy-MM-dd): "); LocalDate exit = LocalDate.parse(sc.nextLine()); boolean ok = hospital.dischargePatient(id, exit); System.out.println(ok?"Alta registrada.":"ID não encontrado."); }
        else System.out.println("Operação cancelada.");
    }

    private void relatoriosMenu(){
        System.out.println("\n--- Relatórios ---");
        System.out.println("1) Pacientes (com histórico)");
        System.out.println("2) Médicos");
        System.out.println("3) Consultas");
        System.out.println("4) Pacientes internados atualmente");
        System.out.println("5) Economia por plano");
        System.out.print("Escolha: "); String op = sc.nextLine().trim();
        switch (op){
            case "1": System.out.println(hospital.reportPatients()); break;
            case "2": System.out.println(hospital.reportDoctors()); break;
            case "3": System.out.println(hospital.reportConsultations()); break;
            case "4": System.out.println(hospital.reportCurrentlyInterned()); break;
            case "5": System.out.println(hospital.reportPlanEconomy()); break;
            default: System.out.println("Inválido"); break;
        }
    }
}
