import IA.Gasolina.*;

import java.util.ArrayList;

public class State {

    private static Gasolineras gas;
    private static CentrosDistribucion distr;

    private ArrayList<ArrayList<Trip>> trucks;

    private int ghost;

    public State() {

    }

    public State(ArrayList<ArrayList<Trip>> t){
        trucks = t;
    }

    public State(Gasolineras g, CentrosDistribucion c) {
        gas = g;
        distr = c;

        ghost = distr.size();
        trucks = new ArrayList<>(ghost + 1);

        emptyTrips();

        //crear estado inicial a partir de los desasignados
    }

    private void emptyTrips() {
        Order[] o = new Order[2];
        int count = 0;

        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order order = new Order(i, j);
                if (i % 2 == 0) o[0] = order;
                else {
                    o[1] = order;
                    trucks.get(ghost).add(new Trip(o));
                }
            }
        }

        if (count % 2 == 0) {
            o[2] = null;
            trucks.get(ghost).add(new Trip(o));
        }
    }

    //Generar estado inicial
    private void fillTrips() {

    }

    public void swap(int i, int j, int k, int l, int m, int n) {
        //En la generadora de sucesores el swap se realiza, para cada camión i,
        //con todos los camiones j tal que j > i

        // i == l -> j != m
        // i == ghost -> l < ghost
        // i != ghost -> l <= i

        // i, l € {0, ... , ghost}
        // j, m € {0, 4} if (i, l != ghost), else {0, numOrders}
        // k, l € {0, 1}

        Order aux = trucks.get(i).get(j).changeOrder(trucks.get(l).get(m).getOrder(n), k);
        trucks.get(l).get(m).setOrder(aux, n);
    }

    public boolean canSwap(int i, int j, int k, int l, int m, int n) {

        return trucks.get(i).get(j).getOrder(k) != null && trucks.get(l).get(m).getOrder(n) != null
                && (i != l || j != m);
    }

    public boolean canMove(int i, int j, int k, int l, int m) {
        return true;
    }

    public void move(int i, int j, int k, int l, int m) {

    }

    public ArrayList<ArrayList<Trip>> getState() {
        return trucks;
    }

    private int getDistanceGas(int i, int j) {
        return Math.abs(gas.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(gas.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    private int getDistanceCenter(int i, int j) {
        return Math.abs(distr.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(distr.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    public int getDistanceTrip(int i, int j) {
        int firstGas = trucks.get(i).get(j).getOrder(0).getGasStation();
        if (trucks.get(i).get(j) != null) {
            int secondGas = trucks.get(i).get(j).getOrder(1).getGasStation();
            return getDistanceCenter(i, firstGas) + getDistanceGas(firstGas, secondGas) + getDistanceCenter(i, secondGas);
        }
        return 2 * getDistanceCenter(i, firstGas);
    }
}
