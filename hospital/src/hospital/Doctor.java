package hospital;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String crm;
    private String specialty;
    private double consultationCost;
    private List<String> agenda = new ArrayList<>();
    private int consultationsConducted = 0;

    public Doctor(String name, String crm, String specialty, double cost){ super(name); this.crm = crm; this.specialty = specialty; this.consultationCost = cost; }
    public String getCrm(){ return crm; }
    public String getSpecialty(){ return specialty; }
    public double getConsultationCost(){ return consultationCost; }
    public List<String> getAgenda(){ return agenda; }
    public void addAgendaSlot(String iso){ agenda.add(iso); }
    public int getConsultationsConducted(){ return consultationsConducted; }
    public void incrementConsultationsConducted(){ consultationsConducted++; }

    public String toCsv(){ StringBuilder sb = new StringBuilder(); sb.append(crm).append("|").append(getName()).append("|").append(specialty).append("|").append(consultationCost).append("|"); for (int i=0;i<agenda.size();i++){ if (i>0) sb.append(","); sb.append(agenda.get(i)); } sb.append("|").append(consultationsConducted); return sb.toString(); }

    public static Doctor fromCsv(String csv){
        String[] parts = csv.split("\\|", -1);
        String crm = parts[0]; String name = parts.length>1?parts[1]:""; String specialty = parts.length>2?parts[2]:"";
        double cost = parts.length>3 && !parts[3].isEmpty()?Double.parseDouble(parts[3]):0.0;
        Doctor d = new Doctor(name, crm, specialty, cost);
        if (parts.length>4 && !parts[4].isEmpty()){ String[] slots = parts[4].split(","); for (String s: slots) if (!s.isEmpty()) d.addAgendaSlot(s); }
        if (parts.length>5 && !parts[5].isEmpty()) d.consultationsConducted = Integer.parseInt(parts[5]);
        return d;
    }

    @Override public String toString(){ return String.format("Dr(a). %s | CRM: %s | Especialidade: %s | Valor consulta: R$ %.2f", getName(), crm, specialty, consultationCost); }
}
