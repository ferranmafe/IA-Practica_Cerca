import IA.Gasolina.*;

import java.util.ArrayList;

public class State {

    private static Gasolineras gas;
    private static CentrosDistribucion distr;

    private static int max_trips;


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
        max_trips = 5;
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

        for (int i = 0; i < distr.size(); i++){
            for (int j = 0; j < max_trips; j++){
                Order[] o2 = new Order[2];
                o2[0] = null;
                o2[1] = null;
                trucks.get(i).add(new Trip(o2));
            }
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

    public int sumDistance(int i){
        int sum = 0;
        for (int j = 0; j < trucks.get(i).size(); ++j){
            sum += getDistanceTrip(i, j);
        }
        return sum;
    }

    public boolean canSwap(int i, int j, int k, int l, int m, int n) {
        if (trucks.get(i).get(j).getOrder(k) == null && trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }
        int d1 = sumDistance(i);
        int d2 = sumDistance(l);

        int dt1 = getDistanceTrip(i, j);
        int dt2 = getDistanceTrip(l, m);

        boolean dOk = d1 - dt1 + dt2 <= 640 && d2 - dt2 + dt1 <= 640;

        return (i != l || j != m) && dOk;
    }

    public boolean isNullOrder(int i, int j, int k){
        return trucks.get(i).get(j).getOrder(k) == null;
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
        if (trucks.get(i).get(j).getOrder(0) != null){
            int firstGas = trucks.get(i).get(j).getOrder(0).getGasStation();
            if (trucks.get(i).get(j).getOrder(0) != null){
                int secondGas = trucks.get(i).get(j).getOrder(1).getGasStation();
                return getDistanceCenter(i, firstGas) + getDistanceGas(firstGas, secondGas) + getDistanceCenter(i, secondGas);
            }
            else {
                return 2 * getDistanceCenter(i, firstGas);
            }
        }
        else if (trucks.get(i).get(j).getOrder(0) != null){
            int secondGas = trucks.get(i).get(j).getOrder(1).getGasStation();
            return 2 * getDistanceCenter(i, secondGas);
        }
        return 0;
    }

    public int getBenefits() {
        int benefits = 0;
        for (int i = 0; i < ghost; ++i) {
            for (int j = 0; j < trucks.get(i).size(); ++j) {
                for (int k = 0; k < 2; ++k){
                    Order order = trucks.get(i).get(j).getOrder(k);
                    if (order != null) {
                        int gas_station_num = order.getGasStation();
                        int order_num = order.getNumOrder();
                        int dias_peticion = gas.get(gas_station_num).getPeticiones().get(order_num);
                        int percentatge_over_total;
                        if (dias_peticion == 0) {
                            percentatge_over_total = 102;
                        } else {
                            percentatge_over_total = (int) (100 - Math.pow((double) 2, (double) dias_peticion));
                        }
                        benefits += 1000 * (percentatge_over_total) / 100;
                    }
                }
            }
        }
        return benefits;
    }

    public boolean isGoal(){
        return true;
    }
}
