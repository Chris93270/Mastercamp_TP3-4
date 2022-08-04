import java.time.LocalDate;
import java.util.Date;

public class Runner implements Comparable<Runner> {
    private static int lastArrivedPosition = 1;
    private final int numDossard;
    private final String nom;
    private final String prenom;
    private Integer classement;
    private Date temps;
    private RunnerStatus status;

    public Runner(int numDossard, int classement, String nom, String prenom, Date temps, RunnerStatus status) {
        if(numDossard < 1 || numDossard > Launcher.nbRunners){
            throw new IllegalArgumentException("Num dossard incorrect");
        }
        this.numDossard = numDossard;
        this.classement = classement;
        this.nom = nom;
        this.prenom = prenom;
        this.temps = temps;
        this.status = status;
    }

    public Runner(String nom, String prenom, int numDossard){
        if(numDossard < 1 || numDossard > Launcher.nbRunners)
            throw new IllegalArgumentException("Num dossard incorrect");
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        this.numDossard = numDossard;
        this.classement = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.temps = date;
        this.status = RunnerStatus.RUNNING;
    }

    public Runner() {
        numDossard = 0;
        nom = "";
        prenom = "";
    }

    public int getNumDossard() {
        return numDossard;
    }

    public int getClassement() {
        return classement;
    }

    public void setClassement(int classement) {
        this.classement = classement;
    }

    public void arrive(){
        status = RunnerStatus.FINISHED;
        classement = lastArrivedPosition;
        lastArrivedPosition++;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Date getTemps() {
        return temps;
    }

    public void setTemps(Date temps) {
        this.temps = temps;
    }

    public RunnerStatus getStatus() {
        return status;
    }

    public void setStatus(RunnerStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (n°"+ numDossard + ") : " + status.name();
    }

    @Override
    public int compareTo(Runner o) {
        return this.classement.compareTo(o.classement);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(!(obj instanceof Runner)) return false;
        Runner runner = (Runner) obj;
        return numDossard == runner.numDossard;
    }
}