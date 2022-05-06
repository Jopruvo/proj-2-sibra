import java.util.ArrayList;

public class Arret {
    
    String ligne;
    String nom;
    ArrayList<Horaire> horaire = new ArrayList<Horaire>();

    public Arret(String ligne){
        this.ligne = ligne;
        this.nom = nom;
    }

    public String getNom(){
        return this.nom;
    }

    public void addHoraire(Horaire h){
        horaire.add(h);
    }

    public String getLigne(){
        return this.ligne;
    }

    public String getHoraire(){
        for(int i = 0; i < horaire.size(); i++){
            return (horaire.get(i)).getHeure();
        }  
    }
}
