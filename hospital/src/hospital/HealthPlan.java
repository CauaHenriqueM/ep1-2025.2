package hospital;

import java.util.HashMap;
import java.util.Map;

public class HealthPlan {
    private String id;
    private String name;
    private Map<String, Double> discountsBySpecialty = new HashMap<>();
    private boolean freeShortInternment = false;

    public HealthPlan(String id, String name){ this.id = id; this.name = name; }
    public String getId(){ return id; } public String getName(){ return name; }
    public void setDiscount(String specialty, double percent){ discountsBySpecialty.put(specialty, percent); }
    public void setFreeShortInternment(boolean v){ freeShortInternment = v; }
    public boolean isFreeShortInternment(){ return freeShortInternment; }

    public double applyDiscountForSpecialty(String specialty, double originalPrice, int patientAge){
        double pct = discountsBySpecialty.getOrDefault(specialty, 0.0);
        if (patientAge >= 60) pct = Math.max(pct, 0.15);
        return originalPrice * (1.0 - pct);
    }

    public String toCsv(){ StringBuilder sb = new StringBuilder(); sb.append(id).append("|").append(name).append("|").append(freeShortInternment?"1":"0").append("|"); int i=0; for (Map.Entry<String,Double> e: discountsBySpecialty.entrySet()){ if (i>0) sb.append(","); sb.append(e.getKey()).append(":").append(e.getValue()); i++; } return sb.toString(); }

    public static HealthPlan fromCsv(String csv){ String[] parts = csv.split("\\|", -1); String id = parts[0]; String name = parts.length>1?parts[1]:""; HealthPlan p = new HealthPlan(id,name); if (parts.length>2) p.freeShortInternment = "1".equals(parts[2]); if (parts.length>3 && !parts[3].isEmpty()){ String[] items = parts[3].split(","); for (String it: items){ String[] kv = it.split(":"); if (kv.length==2) p.setDiscount(kv[0], Double.parseDouble(kv[1])); } } return p; }

    @Override public String toString(){ return String.format("Plano: %s (%s) | Internação curta gratuita: %s", id, name, freeShortInternment?"sim":"não"); }
}
