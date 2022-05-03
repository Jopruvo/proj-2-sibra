import java.util.ArrayList;

public class Bus {
    
    private String ligne;
    private String direction;
    private ArrayList<Arret> arret = new ArrayList<>();

    public Bus(String ligne, String direction){
        this.ligne = ligne;
        this.direction = direction;
    }

    public void addArret(Arret a){
        arret.add(a);
    }
}
