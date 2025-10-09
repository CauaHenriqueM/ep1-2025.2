package hospital;

public class SpecialPatient extends Patient {
    private HealthPlan plan;
    public SpecialPatient(String name, String cpf, int age, HealthPlan plan){
        super(name, cpf, age);
        this.plan = plan;
    }
    public HealthPlan getPlan(){ return plan; }
    public void setPlan(HealthPlan plan){ this.plan = plan; }
    @Override public String toCsv(){ String base = super.toCsv(); return base.replace("|0|","|1|" + (plan!=null?plan.getId():"")); }
}
