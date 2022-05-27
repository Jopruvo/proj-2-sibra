public class Chemin {

    NodeArret actualBusStop;
    NodeArret previousBusStop;
    int timeSinceDeparture;

    public Chemin(NodeArret actualBusStop, NodeArret previousBusStop, int timeSinceDeparture){
        this.actualBusStop = actualBusStop;
        this.previousBusStop = previousBusStop;
        this.timeSinceDeparture = timeSinceDeparture;
    }

    public String getNameLine(){
        return this.actualBusStop.getNameLine();
    }

    public String getNameBus(){
        return this.actualBusStop.getNameBus();
    }

}
