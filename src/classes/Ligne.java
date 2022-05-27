import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// ex: each senses

public class Ligne {

    private ArrayList<String> rawDataLine;

    public ArrayList<Arret> dataLine = new ArrayList<>();
    public ArrayList<String> listBusStopName = new ArrayList<>();


    /*
    =======================================================================
     Initialisation
    =======================================================================
    */

    // initialisation
    public Ligne(ArrayList<String> rawDataLine){
        this.rawDataLine = rawDataLine;
        convertRawData();
        setListBusStopName();
        // Special BusStop
        setIsTerminus();
        setIsNotAlready();
        setIsSeparation();
    }


    // convert raw data
    public void convertRawData(){
        for (String busStop: this.rawDataLine){
//            System.out.println(busStop);
            if (!busStop.equals("")) {
                this.dataLine.add(new Arret(busStop));
            }
        }
    }

    public void setListBusStopName(){
        for (Arret bs: this.dataLine){
            this.listBusStopName.add(bs.getName());
        }
    }

    public void setIsTerminus(){
        for (Arret bs: this.dataLine){
            if (bs.name.equals(bs.name.toUpperCase())){
                bs.isTerminus = true;
            }
        }
    }

    public void setIsNotAlready(){
        for (Arret bs: this.dataLine){
            if (bs.listHourOfPassage.stream().anyMatch(s->s.equals("-"))) bs.isNotAlready = true;
        }
    }

    public void setIsSeparation(){
        for (Arret bs: this.dataLine){
            if (bs.isNotAlready){
                for (Arret bs_check: this.dataLine) {
                    if (bs.isReverse(bs_check)){
                        bs.isSeparation = true;
                        break;
                    }
                }
            }
        }
    }

//    public void setIsJunctionSeparation(){


    /*
    =======================================================================
     Methods
    =======================================================================
    */

    public String getLast(){
        return this.listBusStopName.get(this.listBusStopName.size() - 1);
    }

    public int indexBusStopName(String busStopName){
        // -1 if not in list else index
        if (!this.listBusStopName.contains(busStopName)) return -1;
        else return this.listBusStopName.indexOf(busStopName);
    }

    public ArrayList<String> listHourOfPassage(String busStopName){
        for (Arret bs: this.dataLine){
            if (bs.getName().equals(busStopName)){
                return bs.listHourOfPassage;
            }
        }
        return null;
    }

    public boolean isAfter(String firstBusStopName, String secondBusStopName){
        // true if the second bus stop name is after
        return indexBusStopName(firstBusStopName) < indexBusStopName(secondBusStopName);
    }

    public boolean isBefore(String firstBusStopName, String secondBusStopName){
        // true if the second bus stop name is before
        return indexBusStopName(firstBusStopName) > indexBusStopName(secondBusStopName);
    }

    public ArrayList<Arret> getNextBusStop(Arret busStop){
        ArrayList<Arret> listBusStop = new ArrayList<>();
        Arret previousBusStop = null;
//        System.out.println(this.listBusStopName);
        for (Arret bs: this.dataLine) {
            if (busStop == previousBusStop){
                listBusStop.add(bs);
            }
            // if two possibilities next
            if (listBusStop.size() > 0){
//                System.out.println(listBusStop.get(listBusStop.size() - 1).name + " ~~~~~~ " + bs.name);
//                System.out.println("SOP: " + listBusStop.get(listBusStop.size() - 1).isReverse(bs));
                if (listBusStop.get(listBusStop.size() - 1).isReverse(bs)){
                    System.out.println("test");
                    listBusStop.add(bs);
                }
            }
            previousBusStop = bs;
        }
        return listBusStop;
    }

    public ArrayList<Arret> getPreviousBusStop(Arret busStop){
        ArrayList<Arret> listBusStop = new ArrayList<>();
        Arret previousBusStop = null;
//        System.out.println(this.listBusStopName);
        for (Arret bs: this.dataLine) {
            if (busStop == bs){
                listBusStop.add(previousBusStop);
            }
            previousBusStop = bs;
        }
        return listBusStop;
    }



//    public BusStop getPreviousBusStop(BusStop busStop){
//        AtomicReference<BusStop> previousBusStop = null;
//        for (BusStop bs: this.dataLine) {
//            if (busStop == previousBusStop.get()){
//                return previousBusStop.get();
//            } // prendre en compte les fourchettes
//            previousBusStop.set(bs);
//        }
//        return null;
//    }

    public String getFirstAndLast(){
        // get first bus stop and last bus stop
        return this.listBusStopName.get(0) + "_to_" + this.listBusStopName.get(this.listBusStopName.size() - 1);
    }

    public void makeAllBusStopToFalse(){
        for (Arret bs: this.dataLine){
            bs.setIsUsed(false);
        }
    }

    public boolean isBusStopHere(String name, ArrayList<String> listHourOfPassage){
        for (Arret bs: this.dataLine){
            if (bs.name == name && bs.listHourOfPassage == listHourOfPassage){
                return true;
            }
        }
        return false;
    }



    /*
    =======================================================================
     Functions for graph
    =======================================================================
    */

    public ArrayList<NodeArret> getArrayNearestBusStop(String busStopName, String busName, String lineName){
        // get the next bus stop in the list if exists
        ArrayList<NodeArret> listNodeBusStop = new ArrayList<>();
        for (Arret bs: this.dataLine){
            if (bs.name.equals(busStopName)){
                for (Arret nextBusStop: getNextBusStop(bs)) {
                    if (nextBusStop != null) {
                        listNodeBusStop.add(new NodeArret(busName, lineName, nextBusStop));
                    }
                }
            }
        }
//        System.out.println("listNodeBusStop");
//        System.out.println(listNodeBusStop);
//        System.out.println("listNodeBusStop");
        return listNodeBusStop;
    }

    public void doBusStopUsed(String busStopName){
        for (Arret bs: this.dataLine){
            if (bs.name.equals(busStopName)){
                bs.setIsUsed(true);
            }
        }
    }

    public NodeArret getNodeBusStop(String busStopName, String busName, String lineName){
        for (Arret bs: this.dataLine){
            if (bs.name.equals(busStopName)) {
                return new NodeArret(busName, lineName, bs);
            }
        }
        return null;
    }

    public ArrayList<NodeArret> getAllNodeBusStops(String busName, String lineName) {
        ArrayList<NodeArret> allNodeBusStops = new ArrayList<>();
        for (Arret bs: this.dataLine){
            allNodeBusStops.add(new NodeArret(busName, lineName, bs));
        }
        return allNodeBusStops;
    }


    public int getTimeBetweenTwoBusStop(String nameBusStop1, String nameBusStop2){
        ArrayList<String> arrayBusStop1 = listHourOfPassage(nameBusStop1);
        ArrayList<String> arrayBusStop2 = listHourOfPassage(nameBusStop2);
        for (AtomicInteger index = new AtomicInteger(); index.get() < arrayBusStop1.size(); index.getAndIncrement()){
            if (!arrayBusStop1.get(index.get()).contains("-") && !arrayBusStop2.get(index.get()).contains("-")){
//                System.out.println(arrayBusStop2.get(index.get()) + " - " + arrayBusStop1.get(index.get()));
                return calculateTimeBetweenTwoHours(arrayBusStop2.get(index.get()), arrayBusStop1.get(index.get()));
            }
        }
        return -1;
    }

    private int calculateTimeBetweenTwoHours(String hour_1, String hour_2){

        int hour1Value;
        int minute1Value;
        int hour2Value;
        int minute2Value;

        if (hour_1.length() == 4) {
            hour1Value = Integer.parseInt(hour_1.substring(0, 1));
            minute1Value = Integer.parseInt(hour_1.substring(2,4));
        } else if (hour_1.length() == 5) {
            hour1Value = Integer.parseInt(hour_1.substring(0, 2));
            minute1Value = Integer.parseInt(hour_1.substring(3,5));
        } else {
            return -1;
        }

        if (hour_2.length() == 4) {
            hour2Value = Integer.parseInt(hour_2.substring(0, 1));
            minute2Value = Integer.parseInt(hour_2.substring(2,4));
        } else if (hour_2.length() == 5) {
            hour2Value = Integer.parseInt(hour_2.substring(0, 2));
            minute2Value = Integer.parseInt(hour_2.substring(3,5));
        } else {
            return -1;
        }

//        System.out.println("_________");
//        System.out.println((hour2Value - hour1Value) * 60 + (minute2Value - minute1Value));
//        System.out.println("_________");
        return (hour2Value - hour1Value) * 60 + (minute2Value - minute1Value);
    }


    /*
    =======================================================================
     Print and debugs
    =======================================================================
    */

    public void print(){
        System.out.println("========================");
//        for (BusStop busStop: this.dataLine) busStop.print();
        System.out.println(this.listBusStopName);
        System.out.println("========================");
    }


}
