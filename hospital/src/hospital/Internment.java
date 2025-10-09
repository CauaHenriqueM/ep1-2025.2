package hospital;

import java.time.LocalDate;

public class Internment {
    private String id;
    private String patientCpf;
    private String responsibleDoctorCrm;
    private LocalDate entryDate;
    private LocalDate exitDate; // nullable
    private String roomId;
    private double cost;
    private InternmentStatus status = InternmentStatus.ACTIVE;

    public Internment(String id, String patientCpf, String responsibleDoctorCrm, LocalDate entryDate, String roomId, double cost){ this.id = id; this.patientCpf = patientCpf; this.responsibleDoctorCrm = responsibleDoctorCrm; this.entryDate = entryDate; this.roomId = roomId; this.cost = cost; }

    public String getId(){ return id; } public String getPatientCpf(){ return patientCpf; } public LocalDate getEntryDate(){ return entryDate; } public LocalDate getExitDate(){ return exitDate; } public String getRoomId(){ return roomId; } public InternmentStatus getStatus(){ return status; } public void setExitDate(LocalDate d){ exitDate = d; } public void setStatus(InternmentStatus s){ status = s; }

    public String toCsv(){ return id+"|"+patientCpf+"|"+responsibleDoctorCrm+"|"+entryDate.toString()+"|"+(exitDate!=null?exitDate.toString():"")+"|"+roomId+"|"+cost+"|"+status; }

    public static Internment fromCsv(String csv){ String[] parts = csv.split("\\|", -1); String id = parts[0]; String cpf = parts[1]; String doc = parts[2]; java.time.LocalDate entry = java.time.LocalDate.parse(parts[3]); java.time.LocalDate exit = parts.length>4 && !parts[4].isEmpty()?java.time.LocalDate.parse(parts[4]):null; String room = parts.length>5?parts[5]:""; double cost = parts.length>6 && !parts[6].isEmpty()?Double.parseDouble(parts[6]):0.0; Internment it = new Internment(id, cpf, doc, entry, room, cost); if (exit!=null) it.setExitDate(exit); if (parts.length>7 && !parts[7].isEmpty()) it.setStatus(InternmentStatus.valueOf(parts[7])); return it; }

    @Override public String toString(){ return String.format("Internação[%s] Paciente:%s Médico:%s Entrada:%s Saída:%s Quarto:%s Status:%s R$ %.2f", id, patientCpf, responsibleDoctorCrm, entryDate, exitDate!=null?exitDate.toString():"-", roomId, status, cost); }
}
