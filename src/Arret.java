import java.util.ArrayList;

public class Arret {
    
    private String ligne;
    private ArrayList<Horaire> horaire = new ArrayList<>();

    public Arret(String ligne){
        this.ligne = ligne;
    }

    public void addHoraire(Horaire h){
        horaire.add(h);
    }

}
