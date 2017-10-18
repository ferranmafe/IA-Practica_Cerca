import IA.Gasolina.*;

import java.util.ArrayList;

public class State {

    private static Gasolineras gas;
    private static CentrosDistribucion trucks;

    private int[][] trips;

    public State(){

    }

    public State(Gasolineras g, CentrosDistribucion c){
        gas = g;
        trucks = c;
        fillTrips();
    }

    private void fillTrips(){
        trips = new int[trucks.size()][];
        for (int i = 0; i < trucks.size(); ++i){

        }
    }

    public void swap(){

    }

    public void move(){

    }

    public void getState(){

    }

}
