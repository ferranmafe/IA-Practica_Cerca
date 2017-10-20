import IA.Gasolina.*;
import java.util.ArrayList;

public class State {

    private static Gasolineras gas;
    private static CentrosDistribucion trucks;

    private ArrayList< ArrayList < Trip> > trips;

    private int ghost;

    public State(){

    }

    public State(Gasolineras g, CentrosDistribucion c){
        gas = g;
        trucks = c;

        ghost = trucks.size();
        trips = new ArrayList<>(ghost + 1);

        emptyTrips();

        //crear estado inicial a partir de los desasignados
    }

    private void emptyTrips(){
        Order [] o = new Order[2];
        int count = 0;

        for (int i = 0; i < gas.size(); ++i){
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j){
                Order order = new Order(i, j);
                if (i % 2 == 0) o[0] = order;
                else{
                    o[1] = order;
                    trips.get(ghost).add(new Trip(o));
                }
            }
        }

        if (count % 2 == 0){
            o[2] = null;
            trips.get(ghost).add(new Trip(o));
        }
    }

    //Generar estado inicial
    private void fillTrips(){

    }

    public void swap(int i, int j, int k, int l, int m, int n){
        //En la generadora de sucesores el swap se realiza, para cada camión i,
        //con todos los camiones j tal que j > i

        // i == l -> j != m
        // i == -1 -> l != -1
        // i != -1 -> l >= i

        // i, l € {-1, ... , trucks.size() - 1}
        // j, m €
        // k, l € {0, 1}

        if (trips.get(i).get(j).getOrder(k) != null && trips.get(l).get(m).getOrder(n) != null) {
            Order aux = trips.get(i).get(j).changeOrder(trips.get(l).get(m).getOrder(n), k);
            trips.get(l).get(m).setOrder(aux, n);
        }
    }

    public void move(){

    }

    public void getState(){

    }

    private int getDistanceGas(int i, int j){
        return Math.abs(gas.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(gas.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    private int getDistanceCenter(int i, int j){
        return Math.abs(trucks.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(trucks.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    public int getDistanceTrip(int i, int j){
        int firstGas = trips.get(i).get(j).getOrder(0).getGasStation();
        if (trips.get(i).get(j) != null) {
            int secondGas = trips.get(i).get(j).getOrder(1).getGasStation();
            return getDistanceCenter(i, firstGas) + getDistanceGas(firstGas, secondGas) + getDistanceCenter(i, secondGas);
        }
        return 2 * getDistanceCenter(i, firstGas);
    }
}
