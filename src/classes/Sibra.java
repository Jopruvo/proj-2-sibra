import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// all buses

public class Sibra {

    ArrayList<String> listPathFile = new ArrayList<>();

    ArrayList<Bus> listBus = new ArrayList<Bus>();

    /*
    =======================================================================
     Class initialization
    =======================================================================
    */

    // by default
    public Sibra(){
        this.listPathFile.add(System.getProperty("user.dir") + "/data/1_Poisy-ParcDesGlaisins.txt");
        this.listPathFile.add(System.getProperty("user.dir") + "/data/2_Piscine-Patinoire_Campus.txt");
    }

    // with non optionnal parameters
    public Sibra(ArrayList<String> listPathFile){
        this.listPathFile = listPathFile;
    }


    /*
    =======================================================================
     Load data
    =======================================================================
    */


    public void loadData(){
        // initialisation
        ArrayList<List<String>> data = new ArrayList<>();

        // data in list
        for (String pathBus: this.listPathFile) {
            Bus bus = new Bus(pathBus);
            listBus.add(bus);
//            bus.print();
        }

    }

    public ArrayList<String> listDataToInfo(List<String> listData){
        return null;
    }


    /*
    =======================================================================
     Setters
    =======================================================================
    */

    public void setUsedBus(String typeDay){
        for (Bus b: this.listBus){
            if (typeDay.equals("saturday or summer")){
                b.setIsUsedLineWeek(false);
                b.setIsUsedLineSaturday(true);
            } else if (typeDay.equals("week")){
                b.setIsUsedLineWeek(true);
                b.setIsUsedLineSaturday(false);
            }
        }
    }



    /*
    =======================================================================
     Use bus methods
    =======================================================================
    */

    public String getDirection(String busName, String lineName){
        for (Bus b: this.listBus){
            if (b.getName() == busName){
                return b.getDirection(lineName);
            }
        }
        return null;
    }

    public void displayBusDepartureAndArrival(String departure, String arrival){
        System.out.println("Possibility of departure bus line:");
        for (Bus b: listBusStop(departure)) {
            //
            System.out.println(b.name);
        }
        System.out.println("Possibility of arrival bus line:");
        for (Bus b: listBusStop(arrival)) {
            System.out.println(b.name);
        }

    }

    public ArrayList<Bus> listBusStop(String nameBusStop){
        ArrayList<Bus> listBusStop = new ArrayList<>();
        for (Bus b: this.listBus){
            if (b.hasBusStop(nameBusStop)){
                listBusStop.add(b);
//                b.print();
            }
        }

        return listBusStop;
    }

    public ArrayList<Bus> getListBus() {
        return listBus;
    }

    public void makeAllBusUsedToFalse() {
        for (Bus b : this.listBus) {
            b.makeAllLineToFalse();
        }
    }


//    public ArrayList<String> firstHourDepartance(String nameBusStop, String hourDepartance){
//        ArrayList<String> listHourDepartance = new ArrayList<>();
//        for (Bus b: this.listBus){
//            if (b.hasBusStop(nameBusStop)){
//                listBusStop.add(b);
//                b.print();
//            }
//        }
//
//        return listBusStop;
//    }

//    public boolean isBusStop1

    public ArrayList<String> busAndLineWithThisSuccession(String typeDay,
                                                          String busStopName1, ArrayList<String> listHourOfPassage1,
                                                          String busStopName2, ArrayList<String> listHourOfPassage2){
        ArrayList<String> busAndLine = new ArrayList<>();
        String nameLineWithBusStops;
        for (Bus b: this.listBus){
            nameLineWithBusStops = b.nameLineWithBusStops(busStopName1, listHourOfPassage1, busStopName2, listHourOfPassage2, typeDay);
//            System.out.println("(" + busStopName1 + " - " + b.name + ") listHourOfPassage1: " + listHourOfPassage1);
//            System.out.println("(" + busStopName2 + " - " + b.name + ") listHourOfPassage2: " + listHourOfPassage2);
//            System.out.println("nameLineWithBusStops: " + nameLineWithBusStops);
            if (nameLineWithBusStops != null) {
                busAndLine.add(b.name);
                busAndLine.add(nameLineWithBusStops);
//                System.out.println("#");
                break;
            }
        }
        return busAndLine;
    }

    public int getTimeBetweenTwoBusStop(ArrayList<String> infoLine, String nameBusStop1, String nameBusStop2){
        for (Bus b: this.listBus){
            // if same bus
            if (infoLine != null) {
                if (b.getName().equals(infoLine.get(0))) {
//                    System.out.println("=======");
                    return b.getTimeBetweenTwoBusStop(infoLine.get(1), nameBusStop1, nameBusStop2);
                }
            }
        }
        return -1;
    }


    /*
    =======================================================================
     Functions for graph
    =======================================================================
    */

    public ArrayList<NodeArret> getNearestBusStop(String nameBusStop, String nameLine, String nameBus, String typeDay){
        //list of buses only with the lines having the requested stop
        // (the list contains only the nearest bus stops)
        ArrayList<NodeArret> arrayNearestBusStop = new ArrayList<>();
        ArrayList<NodeArret> sameNodeBusStopOtherLine;
        for (Bus b: this.listBus){
            arrayNearestBusStop.addAll(b.getArrayNearestBusStop(nameBusStop, nameLine, typeDay));
            // si c'est un autre bus, ajout du m??me arret si existe
            if (!b.getName().equals(nameBus)){
                sameNodeBusStopOtherLine = b.getNodeBusStop(nameBusStop, typeDay);
                if (sameNodeBusStopOtherLine != null){
                    arrayNearestBusStop.addAll(sameNodeBusStopOtherLine);
                }
            }
            // si c'est le m??me bus, on prend l'arr??t dans l'autre sens
            if (b.getName().equals(nameBus)){
                sameNodeBusStopOtherLine = b.getNodeBusStop(nameBusStop, typeDay);
                for (NodeArret nbs: sameNodeBusStopOtherLine) {
                    if (nbs.getNameLine() != nameLine) arrayNearestBusStop.add(nbs);
                }
            }

        }
        arrayNearestBusStop.removeIf(Objects::isNull);
        return arrayNearestBusStop;
    }

    public void doBusStopUsed(String nameBusStop, String typeDay){
        for (Bus b: this.listBus){
            b.doBusStopUsed(nameBusStop, typeDay);
        }
    }

    public ArrayList<NodeArret> busStopToNodeBusStop(String busStopName, String typeDay){
        ArrayList<NodeArret> nodeBusStopArrayList = new ArrayList<>();
        for (Bus b: this.listBus){
            nodeBusStopArrayList.addAll(b.busStopToNodeBusStop(busStopName, typeDay));
        }
        return nodeBusStopArrayList;
    }

    public ArrayList<NodeArret> getAllNodeBusStops(String typeDay){
        ArrayList<NodeArret> allNodeBusStops = new ArrayList<>();
        for (Bus b: this.listBus){
            allNodeBusStops.addAll(b.getAllNodeBusStops(typeDay, b.name));
        }
        return allNodeBusStops;
    }

    public boolean isAfterWithInfo(String nameBusStop1, String nameBusStop2, String busName, String lineName){
        for (Bus b: this.listBus){
            if (b.getName() == busName){
                return b.isAfterWithInfo(nameBusStop1, nameBusStop2, lineName);
            }
        }
        return false;
    }


    /*
    =======================================================================
     Print, tests and debug
    =======================================================================
    */
    public void test(){

        Reading data_l1 = new Reading(this.listPathFile.get(0));
        data_l1.print();

        Reading data_l2 = new Reading(this.listPathFile.get(1));
        data_l2.print();
        System.out.println(data_l2.readRow());
    }

    public void print(){
        for (Bus bus: listBus){
            bus.print();
        }
    }


}
