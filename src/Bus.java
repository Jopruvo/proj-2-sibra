import java.util.ArrayList;

public class Bus {
    
    String ligne;
    String direction;
    ArrayList<Arret> arret = new ArrayList<Arret>();

    public Bus(String ligne, String direction){
        this.ligne = ligne;
        this.direction = direction;
    }

    public void addArret(Arret a){
        arret.add(a);
    }

    public String getLigne(){
        return this.ligne;
    }

    public String getDirection(){
        return this.direction;
    }

    public String getArret(){
        for(int i = 0; i < arret.size(); i++){
            return (arret.get(i)).getNom();
        }  
    }
}
