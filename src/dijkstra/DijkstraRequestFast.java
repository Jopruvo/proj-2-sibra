import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class DijkstraRequestFast extends Request {
    public DijkstraRequestFast(String day, String departure, String arrival, String hour) throws IOException, ParseException {
        super(day, departure, arrival, hour);
    }


    public void doRequest() {
//        System.out.println(sibra.getNearestBusStop("CAMPUS", this.typeDay));
//        for (NodeBusStop nbs : sibra.getNearestBusStop(this.arrival, this.typeDay)) {
//            System.out.println("=========");
//            System.out.println(nbs.getNameBus());
//            System.out.println(nbs.getNameLine());
//            nbs.getBusStop().print();
//        }
//        System.out.println("=========");
//
//        System.out.println(getTimeBetweenTwoBusStop("Mandallaz", "Chorus"));
        printDjikstraPath();


    }

    public void initialise(){

    }

    private ArrayList<Chemin> doDijkstra() {
        // Initialisation

        // liste des noeuds pas passés
        ArrayList<NodeArret> listOfRemainingBusStops = this.sibra.getAllNodeBusStops(this.typeDay);
        // liste des chemins
        ArrayList<Chemin> listOfPaths = new ArrayList<>();
        ArrayList<Chemin> newListOfPaths = new ArrayList<>();
        ArrayList<Chemin> lastListOfPaths = new ArrayList<>();
        ArrayList<Chemin> newNewListOfPaths = new ArrayList<>();
        ArrayList<NodeArret> nearestBusStops;
        int appearsCounter;
        boolean isAlready;
        // liste des chemins choisis
        ArrayList<Chemin> listOfSelectedPaths = new ArrayList<>();

        // Noeud actuel
        NodeArret actualNode = this.sibra.busStopToNodeBusStop(this.departure, this.typeDay).get(0);
        Chemin actualPath = new Chemin(actualNode, null, 0);
        listOfSelectedPaths.add(actualPath);
        listOfRemainingBusStops = removeNodeBusStopFromList(listOfRemainingBusStops, actualNode);


        int t;
        Chemin shortestPath;
        Chemin newPath;

        // Loop
        while (listOfRemainingBusStops.size() > 0){

            shortestPath = new Chemin(null, null, 1000);

            // ********************
            // update de la liste des chemins -> on ajoute tous les chemins possibles
            // ********************
            // pour toutes les destinations possibles depuis le chemin actuel
            nearestBusStops = sibra.getNearestBusStop(actualNode.getNameBusStop(), actualNode.getNameLine(), actualNode.getNameBus(), this.typeDay);
//            System.out.println("++++" + nearestBusStops.size() + "++++");
            for (NodeArret nbs: nearestBusStops){
                // si l'arret n'a pas encore été emprunté -> si il est encore dans la liste des destinations possibles
                if (isBusStopInList(nbs.getNameBusStop(), nbs.getNameLine(), nbs.getNameBus(), listOfRemainingBusStops)){
//                    System.out.println(nbs.getNameBusStop() + " - " + nbs.getNameBus() + " - " + nbs.getNameLine());
                    t = 1 + actualPath.timeSinceDeparture;
                    newPath = new Chemin(nbs, actualNode, t);
                    newListOfPaths.add(newPath);
                }
            }
            // si deux arrest sont identiques, on garde le plus court
            for (Chemin p1: newListOfPaths){
                appearsCounter = 0;
                for (Chemin p2: newListOfPaths){
                    if (p1.actualBusStop == p2.actualBusStop){
                        appearsCounter ++;
                    }
                }
                if (appearsCounter == 1){
                    // si une fois, on le garde
                    newNewListOfPaths.add(p1);
                } else {
                    // sinon on garde le plus court
                    newPath = p1;
                    for (Chemin p2: newListOfPaths){
                        if (p1.actualBusStop == p2.actualBusStop && p2.timeSinceDeparture < newPath.timeSinceDeparture){
                            newPath = p2;
                        }
                    }
                    // ajout que si il n'y st pas deja
                    isAlready = false;
                    for (Chemin p: newNewListOfPaths){
                        if (p == newPath){
                            isAlready = true;
                        }
                    }
                    if (!isAlready) newNewListOfPaths.add(newPath);
                }
            }

            lastListOfPaths = listOfPaths;
            listOfPaths = new ArrayList<>();
            listOfPaths.addAll(lastListOfPaths);
            listOfPaths.addAll(newListOfPaths);

//            listOfPaths = newNewListOfPaths;

//            listOfPaths = newListOfPaths;
            newListOfPaths = new ArrayList<>();
//            newNewListOfPaths = new ArrayList<>();

            // ********************
            // update de la liste des chemins les plus courts
            // ********************
            // recherche du chemin le plus court
//            System.out.println("-- " + listOfPaths);
            for (Chemin p: listOfPaths){
                if (p.timeSinceDeparture < shortestPath.timeSinceDeparture){
                    shortestPath = p;
                }
            }

            // nouveau chemin actuel
            actualNode = shortestPath.actualBusStop;
            actualPath = shortestPath;
            listOfSelectedPaths.add(actualPath);
//            for (Path p: listOfSelectedPaths) System.out.println(p.actualBusStop.getNameBusStop());
//            System.out.println("++++" + actualNode.getNameBusStop() + " - " + actualNode.getNameBus() + " - " + actualNode.getNameLine());

            // ********************
            // update de la liste de la liste des noeuds
            // ********************
            if (actualNode == null){break;}
//            for (NodeBusStop nbs: listOfRemainingBusStops){
//                System.out.println();
//            }
            listOfPaths = removePathFromList(listOfPaths, actualPath);
            listOfRemainingBusStops = removeNodeBusStopFromList(listOfRemainingBusStops, actualNode);

//            System.out.println(listOfRemainingBusStops.size());

        }

        return listOfSelectedPaths;

    }

    private ArrayList<NodeArret> removeNodeBusStopFromList(ArrayList<NodeArret> nodeBusStops, NodeArret nodeBusStop){
        ArrayList<NodeArret> outputNodeBusStops = new ArrayList<>();
        boolean isSameBus;
        boolean isSameLine;
        boolean isSameBusStop;
        String nodeBusStopNameBus = nodeBusStop.getNameBus();
        String nodeBusStopNameLine = nodeBusStop.getNameLine();
//        System.out.println("n: " + nodeBusStopNameLine);
        String nodeBusStopName = nodeBusStop.getNameBusStop();
        for (NodeArret nbs: nodeBusStops){
            isSameBus = nbs.getNameBus().equals(nodeBusStopNameBus);
            isSameLine = nbs.getNameLine().equals(nodeBusStopNameLine);
            isSameBusStop = nbs.getBusStop().getName().equals(nodeBusStopName);
//            System.out.println(isSameLine);
            if (isSameBus && isSameLine && isSameBusStop) {
//            if (isSameBus && isSameBusStop) {
//                System.out.println(nbs.getNameBusStop());
//                System.out.println(nbs.getNameBus());
            } else {
                outputNodeBusStops.add(nbs);
            }
        }
//        for (NodeBusStop nbs: outputNodeBusStops){
//            System.out.print(nbs.getNameBusStop() + "(" + nbs.getNameBus() + ") - ");
//        }
//        System.out.println(" ");
//        System.out.println("_______________");
        return outputNodeBusStops;
    }

    private ArrayList<Chemin> removePathFromList(ArrayList<Chemin> paths, Chemin path){
        ArrayList<Chemin> outputPaths = new ArrayList<>();
        for (Chemin p: paths){
            if (p != path) {
                outputPaths.add(p);
            }
        }
        return outputPaths;
    }

    /*
    =======================================================================
     Methods
    =======================================================================
    */


    public NodeArret getNearestBusStop(NodeArret departureBusStopNode, String hour){

        int busNumbre = getNextBusAtThisHour(departureBusStopNode, hour);

        // for this bus if it's possible
        for (NodeArret nbs: this.sibra.getNearestBusStop(departureBusStopNode.getNameBusStop(), null, null,this.typeDay)){
            if (departureBusStopNode.getNameBus().equals(nbs.getNameBus()) &&
                    departureBusStopNode.getNameLine().equals(nbs.getNameLine())) {
                // if same bus same line (= the next bus stop)
                return nbs;
            }
        }

        // else for another bus
        for (NodeArret nbs: this.sibra.getNearestBusStop(departureBusStopNode.getNameBusStop(), null, null,this.typeDay)){
            // prendre en compte le temps pour aller à l'arrêt pour pouvoir ensuite savoir à partir de quand attendre un autre bus
            //    -> fonction dans Sibra pour savoir le temps entre deux arrêts (rajouter une minute de marge apres)
            //    -> continuer sette boucle for en regardant l procain bus qui passe
        }

        return null;
    }


    /*
    =======================================================================
     Print and debugs
    =======================================================================
    */

    public void printDjikPath(){

        System.out.println("begin dijkstra path: ");

        for (Chemin p: doDijkstra()) {
            System.out.println("==========");
            if (p.actualBusStop != null) {
                System.out.println("actual bus stop: " + p.actualBusStop.getNameBusStop());
                System.out.println("actual bus: " + p.actualBusStop.getNameBus());
//                System.out.println("hour: " + p.actualBusStop.getHourAfter(hour));
                if (p.previousBusStop != null) {
                    System.out.println("previous bus stop: " + p.previousBusStop.getNameBusStop());
                }
                System.out.println("time since departure: " + p.timeSinceDeparture);
            }
        }
        System.out.println("==========");
    }

    public void printDjikstraPath() {
        ArrayList<Chemin> djikstraPath = doDijkstra();
        System.out.println(djikstraPath.size());
        ArrayList<Chemin> upsideDownPath = new ArrayList<>();
        ArrayList<Chemin> paths = new ArrayList<>();
        String hour = this.hour;
        String hourArrival = this.hour;

        Chemin previousPath;
        Chemin lastPath;
        boolean isSameBusStop;
        boolean isBefore;
        int counter = 0;
        int intMinutes;
        int t;
        boolean isFirst = true;

        Chemin path = new Chemin(null, null, 1000);
        // first path
        for (Chemin p: djikstraPath){
            isSameBusStop = path.timeSinceDeparture > p.timeSinceDeparture;
            if (p.actualBusStop != null) {
                isBefore = p.actualBusStop.getNameBusStop().equals(this.arrival);
            } else {
                isBefore = false;
            }
            if (isBefore && isSameBusStop){
                path = p;
            }
        }
        upsideDownPath.add(path);

        System.out.println(path.actualBusStop);
        while (!path.actualBusStop.getNameBusStop().equals(this.departure) && counter <= 1000){
            previousPath = path;
            for (Chemin p: djikstraPath){
                if (p.actualBusStop != null && previousPath.actualBusStop != null) {
                    isSameBusStop = p.actualBusStop.getNameBusStop().equals(previousPath.previousBusStop.getNameBusStop());
                } else {
                    isSameBusStop = false;
                }
                isBefore = p.timeSinceDeparture < previousPath.timeSinceDeparture;
                if (isSameBusStop && isBefore){
                    path = p;
                }
            }
            upsideDownPath.add(path);
            counter ++;
//            if (path.actualBusStop == null){break;}
        }

        Collections.reverse(upsideDownPath);

//        System.out.println(hour + "");
        for (Chemin p: upsideDownPath){
            System.out.println("______________");
            System.out.println("bus stop: " + p.actualBusStop.getNameBusStop());
            System.out.println("bus: " + p.actualBusStop.getNameBus());
            System.out.println("direction: " + sibra.getDirection(p.getNameBus(), p.getNameLine()));
            if (isFirst) {
                isFirst = false;
                System.out.println("estimated time of departure: " + p.actualBusStop.getHourAfter(this.hour));
            }
            System.out.println("number of stops since start: " + p.timeSinceDeparture);
        }
//        lastPath = upsideDownPath.get(upsideDownPath.size() - 1);
//        intMinutes = Integer.parseInt(hour.substring(0,2)) * 60 + Integer.parseInt(hour.substring(3,5)) + lastPath.timeSinceDeparture;
//        hourArrival =  lastPath.actualBusStop.getHourAfter(intMinutesToHour(intMinutes));
//        System.out.println("estimated time of arrival: " + hourArrival);

    }

}

