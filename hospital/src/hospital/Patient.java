package hospital;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Person {
    private String cpf;
    private int age;
    private List<Consultation> consultations = new ArrayList<>();
    private List<Internment> internments = new ArrayList<>();

    public Patient(String name, String cpf, int age){
        super(name);
        this.cpf = cpf; this.age = age;
    }

    public String getCpf(){ return cpf; }
    public int getAge(){ return age; }
    public void setAge(int age){ this.age = age; }

    public void addConsultation(Consultation c){ consultations.add(c); }
    public void addInternment(Internment i){ internments.add(i); }
    public void removeInternmentById(String id){ internments.removeIf(it -> it.getId().equals(id)); }

    public List<Consultation> getConsultations(){ return consultations; }
    public List<Internment> getInternments(){ return internments; }

    public String toCsv(){
        StringBuilder sb = new StringBuilder();
        sb.append(cpf).append("|").append(getName()).append("|").append(age).append("|");
        for (int i=0;i<consultations.size();i++){ if (i>0) sb.append(","); sb.append(consultations.get(i).getId()); }
        sb.append("|");
        for (int i=0;i<internments.size();i++){ if (i>0) sb.append(","); sb.append(internments.get(i).getId()); }
        sb.append("|0|");
        return sb.toString();
    }

    public static Patient fromCsv(String csv, Hospital hospital){
        String[] parts = csv.split("\\|", -1);
        String cpf = parts[0];
        String name = parts.length>1?parts[1]:"";
        int age = parts.length>2 && !parts[2].isEmpty()?Integer.parseInt(parts[2]):0;
        boolean special = parts.length>5 && "1".equals(parts[5]);
        if (special){
            String planId = parts.length>6?parts[6]:"";
            HealthPlan plan = hospital.getPlan(planId);
            return new SpecialPatient(name, cpf, age, plan);
        }
        return new Patient(name, cpf, age);
    }

    @Override public String toString(){ return String.format("Paciente: %s | CPF: %s | Idade: %d", getName(), cpf, age); }
}
