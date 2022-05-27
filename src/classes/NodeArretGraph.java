import java.util.ArrayList;

public class NodeArretGraph extends NodeArret {

    /*
    =======================================================================
     Initialization
    =======================================================================
    */

    private int timeFromDeparture = -1;
    private ArrayList<NodeArretGraph> nodeBusStopChildren = new ArrayList<>();

    public NodeArretGraph(String nameBus, String nameLine, Arret busStop) {
        super(nameBus, nameLine, busStop);
    }


    /*
    =======================================================================
     Setters
    =======================================================================
    */

    public void setTimeFromDeparture(int timeFromDeparture) {
        this.timeFromDeparture = timeFromDeparture;
    }


    /*
    =======================================================================
     Getters
    =======================================================================
    */

    public int getTimeFromDeparture() {
        return this.timeFromDeparture;
    }

    public ArrayList<NodeArretGraph> getNodeBusStopChildren() {
        return nodeBusStopChildren;
    }

    public ArrayList<NodeArretGraph> getLeaves(){
        ArrayList<NodeArretGraph> leaves = new ArrayList<>();
        for (NodeArretGraph child: this.nodeBusStopChildren){
            if (child.nodeBusStopChildren.size() == 0){
                leaves.add(child);
            } else {
                leaves.addAll(child.getLeaves());
            }
        }
        return leaves;
    }

    public int getShortestTime(){
        int shortestTime = -1;
        for (NodeArretGraph leaf: getLeaves()){
            if (shortestTime > leaf.getTimeFromDeparture()){
                shortestTime = leaf.getTimeFromDeparture();
            }
        }
        return shortestTime;
    }

    /*
    =======================================================================
     Methods
    =======================================================================
    */

    public void addNodeBusStopChild(NodeArretGraph nodeBusStopChild){
        this.nodeBusStopChildren.add(nodeBusStopChild);
    }

}
