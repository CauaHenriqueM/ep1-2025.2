package hospital;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Hospital {
    private Map<String, Patient> patients = new LinkedHashMap<>();
    private Map<String, Doctor> doctors = new LinkedHashMap<>();
    private Map<String, HealthPlan> plans = new LinkedHashMap<>();
    private List<Consultation> consultations = new ArrayList<>();
    private List<Internment> internments = new ArrayList<>();
    private Map<String, Room> rooms = new LinkedHashMap<>();

    private final FileStorage storage = new FileStorage();

    public void loadAll() {
        try {
            storage.ensureFilesExist();
            loadPlans();
            loadPatients();
            loadDoctors();
            loadRooms();
            loadConsultations();
            loadInternments();
        } catch (Exception e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
        }
    }

    public void saveAll() {
        try {
            savePlans();
            savePatients();
            saveDoctors();
            saveRooms();
            saveConsultations();
            saveInternments();
        } catch (Exception e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    // Basic add/get/list methods
    public boolean addPatient(Patient p){ if (patients.containsKey(p.getCpf())) return false; patients.put(p.getCpf(), p); savePatients(); return true; }
    public Patient getPatient(String cpf){ return patients.get(cpf); }
    public Collection<Patient> listPatients(){ return patients.values(); }

    public boolean addDoctor(Doctor d){ if (doctors.containsKey(d.getCrm())) return false; doctors.put(d.getCrm(), d); saveDoctors(); return true; }
    public Doctor getDoctor(String crm){ return doctors.get(crm); }
    public Collection<Doctor> listDoctors(){ return doctors.values(); }

    public boolean addPlan(HealthPlan p){ if (plans.containsKey(p.getId())) return false; plans.put(p.getId(), p); savePlans(); return true; }
    public HealthPlan getPlan(String id){ return plans.get(id); }
    public Collection<HealthPlan> listPlans(){ return plans.values(); }

    public boolean addRoom(Room r){ if (rooms.containsKey(r.getId())) return false; rooms.put(r.getId(), r); saveRooms(); return true; }
    public Room getRoom(String id){ return rooms.get(id); }
    public Collection<Room> listRooms(){ return rooms.values(); }

    // Consultations scheduling
    public synchronized boolean scheduleConsultation(Consultation c){
        if (!patients.containsKey(c.getPatientCpf())) return false;
        if (!doctors.containsKey(c.getDoctorCrm())) return false;
        for (Consultation o : consultations){
            if (o.getStatus() == ConsultationStatus.CANCELLED) continue;
            if (o.getDateTime().equals(c.getDateTime())){
                if (o.getDoctorCrm().equals(c.getDoctorCrm())) return false;
                if (o.getLocation().equalsIgnoreCase(c.getLocation())) return false;
            }
        }
        Patient p = patients.get(c.getPatientCpf());
        Doctor d = doctors.get(c.getDoctorCrm());
        double cost = d.getConsultationCost();
        if (p instanceof SpecialPatient){
            SpecialPatient sp = (SpecialPatient)p;
            HealthPlan plan = sp.getPlan();
            if (plan != null) cost = plan.applyDiscountForSpecialty(d.getSpecialty(), cost, p.getAge());
        } else if (p.getAge() >= 60){
            cost *= 0.9;
        }
        c.setPrice(cost);
        consultations.add(c);
        p.addConsultation(c);
        saveConsultations();
        savePatients();
        return true;
    }

    public synchronized boolean concludeConsultation(String id, String diagnosis, String prescription){
        Consultation c = findConsultationById(id);
        if (c == null) return false;
        c.setStatus(ConsultationStatus.COMPLETED);
        c.setDiagnosis(diagnosis);
        c.setPrescription(prescription);
        Doctor d = doctors.get(c.getDoctorCrm());
        if (d != null) d.incrementConsultationsConducted();
        saveConsultations();
        saveDoctors();
        return true;
    }

    private Consultation findConsultationById(String id){
        for (Consultation c: consultations) if (c.getId().equals(id)) return c;
        return null;
    }

    // Internments
    public synchronized boolean admitPatient(Internment it){
        Room r = rooms.get(it.getRoomId());
        if (r == null) return false;
        for (Internment i : internments){
            if (i.getRoomId().equals(it.getRoomId()) && i.getStatus() == InternmentStatus.ACTIVE) return false;
        }
        internments.add(it);
        Patient p = patients.get(it.getPatientCpf());
        if (p != null) p.addInternment(it);
        saveInternments();
        savePatients();
        return true;
    }

    public synchronized boolean dischargePatient(String id, java.time.LocalDate exitDate){
        Internment it = findInternmentById(id);
        if (it == null) return false;
        it.setExitDate(exitDate);
        it.setStatus(InternmentStatus.DISCHARGED);
        saveInternments();
        return true;
    }

    public synchronized boolean cancelInternment(String id){
        Internment it = findInternmentById(id);
        if (it == null) return false;
        it.setStatus(InternmentStatus.CANCELLED);
        Patient p = patients.get(it.getPatientCpf());
        if (p != null) p.removeInternmentById(id);
        saveInternments();
        savePatients();
        return true;
    }

    private Internment findInternmentById(String id){
        for (Internment it : internments) if (it.getId().equals(id)) return it;
        return null;
    }

    // Reports (basic)
    public String reportPatients(){
        StringBuilder sb = new StringBuilder();
        for (Patient p : patients.values()){
            sb.append(p).append(System.lineSeparator());
            sb.append("Consultas:\n");
            for (Consultation c: p.getConsultations()) sb.append("  ").append(c).append("\n");
            sb.append("Internações:\n");
            for (Internment it: p.getInternments()) sb.append("  ").append(it).append("\n");
            sb.append("-----\n");
        }
        return sb.toString();
    }

    public String reportDoctors(){
        StringBuilder sb = new StringBuilder();
        for (Doctor d : doctors.values()) sb.append(d).append("\nConsultas realizadas: ").append(d.getConsultationsConducted()).append("\n-----\n");
        return sb.toString();
    }

    public String reportConsultations(){
        StringBuilder sb = new StringBuilder();
        for (Consultation c : consultations) sb.append(c).append("\n");
        return sb.toString();
    }

    public String reportCurrentlyInterned(){
        StringBuilder sb = new StringBuilder();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (Internment it : internments){
            if (it.getStatus() == InternmentStatus.ACTIVE){
                long days = java.time.Duration.between(it.getEntryDate().atStartOfDay(), today.atStartOfDay()).toDays();
                sb.append(it).append(" | dias: ").append(days).append("\n");
            }
        }
        return sb.toString();
    }

    // Plan economy
    public String reportPlanEconomy(){
        Map<String,Integer> counts = new HashMap<>();
        Map<String,Double> saved = new HashMap<>();
        for (Patient p : patients.values()){
            if (p instanceof SpecialPatient){
                SpecialPatient sp = (SpecialPatient)p;
                HealthPlan plan = sp.getPlan();
                if (plan != null){
                    counts.put(plan.getId(), counts.getOrDefault(plan.getId(),0)+1);
                    for (Consultation c: sp.getConsultations()){
                        Doctor d = doctors.get(c.getDoctorCrm());
                        if (d!=null){
                            double diff = d.getConsultationCost() - c.getPrice();
                            saved.put(plan.getId(), saved.getOrDefault(plan.getId(),0.0)+diff);
                        }
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String pid: counts.keySet()){
            sb.append("Plano ").append(pid).append(" -> usuários: ").append(counts.get(pid)).append(" | economia: R$ ").append(String.format("%.2f", saved.getOrDefault(pid,0.0))).append("\n");
        }
        return sb.toString();
    }

    // Persistence delegations
    private void loadPatients() throws IOException{
        patients.clear();
        List<String> lines = storage.readAllLines("patients.txt");
        for (String l: lines){ if (l.isBlank()) continue; Patient p = Patient.fromCsv(l, this); patients.put(p.getCpf(), p); }
    }
    private void savePatients(){ List<String> lines = new ArrayList<>(); for (Patient p: patients.values()) lines.add(p.toCsv()); storage.writeAllLines("patients.txt", lines); }

    private void loadDoctors() throws IOException{
        doctors.clear();
        List<String> lines = storage.readAllLines("doctors.txt");
        for (String l: lines){ if (l.isBlank()) continue; Doctor d = Doctor.fromCsv(l); doctors.put(d.getCrm(), d); }
    }
    private void saveDoctors(){ List<String> lines = new ArrayList<>(); for (Doctor d: doctors.values()) lines.add(d.toCsv()); storage.writeAllLines("doctors.txt", lines); }

    private void loadPlans() throws IOException{
        plans.clear();
        List<String> lines = storage.readAllLines("plans.txt");
        for (String l: lines){ if (l.isBlank()) continue; HealthPlan p = HealthPlan.fromCsv(l); plans.put(p.getId(), p); }
    }
    private void savePlans(){ List<String> lines = new ArrayList<>(); for (HealthPlan p: plans.values()) lines.add(p.toCsv()); storage.writeAllLines("plans.txt", lines); }

    private void loadRooms() throws IOException{
        rooms.clear();
        List<String> lines = storage.readAllLines("rooms.txt");
        for (String l: lines){ if (l.isBlank()) continue; Room r = Room.fromCsv(l); rooms.put(r.getId(), r); }
    }
    private void saveRooms(){ List<String> lines = new ArrayList<>(); for (Room r: rooms.values()) lines.add(r.toCsv()); storage.writeAllLines("rooms.txt", lines); }

    private void loadConsultations() throws IOException{
        consultations.clear();
        List<String> lines = storage.readAllLines("consultations.txt");
        for (String l: lines){ if (l.isBlank()) continue; Consultation c = Consultation.fromCsv(l); consultations.add(c); Patient p = patients.get(c.getPatientCpf()); if (p!=null) p.addConsultation(c); }
    }
    private void saveConsultations(){ List<String> lines = new ArrayList<>(); for (Consultation c: consultations) lines.add(c.toCsv()); storage.writeAllLines("consultations.txt", lines); }

    private void loadInternments() throws IOException{
        internments.clear();
        List<String> lines = storage.readAllLines("internments.txt");
        for (String l: lines){ if (l.isBlank()) continue; Internment it = Internment.fromCsv(l); internments.add(it); Patient p = patients.get(it.getPatientCpf()); if (p!=null) p.addInternment(it); }
    }
    private void saveInternments(){ List<String> lines = new ArrayList<>(); for (Internment it: internments) lines.add(it.toCsv()); storage.writeAllLines("internments.txt", lines); }
}
