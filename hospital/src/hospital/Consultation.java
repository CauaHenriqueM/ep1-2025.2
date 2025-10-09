package hospital;

import java.time.LocalDateTime;

public class Consultation {
    private String id;
    private String patientCpf;
    private String doctorCrm;
    private LocalDateTime dateTime;
    private String location;
    private ConsultationStatus status = ConsultationStatus.SCHEDULED;
    private String diagnosis = "";
    private String prescription = "";
    private double price = 0.0;

    public Consultation(String id, String patientCpf, String doctorCrm, LocalDateTime dateTime, String location){ this.id = id; this.patientCpf = patientCpf; this.doctorCrm = doctorCrm; this.dateTime = dateTime; this.location = location; }
    public String getId(){ return id; } public String getPatientCpf(){ return patientCpf; } public String getDoctorCrm(){ return doctorCrm; } public LocalDateTime getDateTime(){ return dateTime; } public String getLocation(){ return location; } public ConsultationStatus getStatus(){ return status; } public void setStatus(ConsultationStatus s){ status = s; } public void setDiagnosis(String d){ diagnosis = d; } public void setPrescription(String p){ prescription = p; } public void setPrice(double p){ price = p; } public double getPrice(){ return price; }

    public String toCsv(){ StringBuilder sb = new StringBuilder(); sb.append(id).append("|").append(patientCpf).append("|").append(doctorCrm).append("|").append(dateTime.toString()).append("|").append(location).append("|").append(status).append("|").append(escape(diagnosis)).append("|").append(escape(prescription)).append("|").append(price); return sb.toString(); }

    public static Consultation fromCsv(String csv){ String[] parts = csv.split("\\|", -1); String id = parts[0]; String patientCpf = parts[1]; String doctorCrm = parts[2]; java.time.LocalDateTime dt = java.time.LocalDateTime.parse(parts[3]); String loc = parts[4]; Consultation c = new Consultation(id, patientCpf, doctorCrm, dt, loc); if (parts.length>5 && !parts[5].isEmpty()) c.status = ConsultationStatus.valueOf(parts[5]); if (parts.length>6) c.diagnosis = unescape(parts[6]); if (parts.length>7) c.prescription = unescape(parts[7]); if (parts.length>8 && !parts[8].isEmpty()) c.price = Double.parseDouble(parts[8]); return c; }

    private static String escape(String s){ return s.replace("\n","\\n").replace("|","/"); }
    private static String unescape(String s){ return s.replace("\\n","\n"); }

    @Override public String toString(){ return String.format("Consulta[%s] Paciente:%s Médico:%s %s Local:%s Status:%s R$ %.2f", id, patientCpf, doctorCrm, dateTime.toString(), location, status, price); }
}
