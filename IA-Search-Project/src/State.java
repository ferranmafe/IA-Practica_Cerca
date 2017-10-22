import IA.Gasolina.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class State {

    private static Gasolineras gas;
    private static CentrosDistribucion distr;

    private static int max_trips;
    private static int max_distance;


    private ArrayList<ArrayList<Trip> > trucks;

    private static int ghost;

    public State() {

    }

    public State(ArrayList<ArrayList<Trip>> t){
        trucks = t;
    }

    public State(Gasolineras g, CentrosDistribucion c) {
        gas = g;
        distr = c;
        ghost = distr.size();
        max_trips = 5;
        max_distance = 640;

        trucks = new ArrayList<>(ghost + 1);

        for (int i = 0; i < ghost + 1; i++){
            trucks.add(new ArrayList<>());
        }

        //crear estado inicial a partir de los desasignados
    }

    public void emptyTrips() {
        Order[] o = new Order[2];
        int count = 0;

        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order order = new Order(i, j);
                if (i % 2 == 0)
                    o[0] = order;
                else {
                    o[1] = order;
                    trucks.get(ghost).add(new Trip(o));
                }
            }
        }

        if (count % 2 == 0) {
            o[1] = null;
            trucks.get(ghost).add(new Trip(o));
        }

        for (int i = 0; i < ghost; i++){
            for (int j = 0; j < max_trips; j++){
                Order[] o2 = new Order[2];
                o2[0] = null;
                o2[1] = null;
                trucks.get(i).add(new Trip(o2));
            }
        }
    }

    public void greedyTrips() {
        //Rellenamos todos los estados como nulos y luego iteraremos calulando distancias modo greedy y actualizando trips
        for (int i = 0; i < ghost; i++){
            for (int j = 0; j < max_trips; j++){
                Order[] o = new Order[2];
                o[0] = null;
                o[1] = null;
                Order[] o2 = new Order[2];
                o2[0] = null;
                o2[1] = null;
                trucks.get(i).add(new Trip(o));
                trucks.get(ghost).add(new Trip (o2));
            }
        }
        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order order = new Order(i, j);
                boolean order_located = false;
                int k = 0;
                while (!order_located && k < trucks.size()){
                    int l = 0;
                    while (!order_located && l < trucks.get(k).size()){
                        int m = 0;
                        while (!order_located && m < 2){
                            int dist_first_order, dist_second_order;
                            if (k == ghost){
                                dist_first_order = 0;
                                dist_second_order = 0;
                            }
                            else if (m == 0){
                                dist_first_order = (sumDistance(k) + 2 * getDistanceCenter(k, order.getGasStation()));
                                dist_second_order = 0;
                            }
                            else if (trucks.get(k).get(l).getOrder(0) == null){
                                dist_first_order = 0;
                                dist_second_order = (sumDistance(k) + 2 * getDistanceCenter(k, order.getGasStation()));
                            }
                            else {
                                dist_first_order = (sumDistance(k) + 2 * getDistanceCenter(k, order.getGasStation()));
                                dist_second_order = (sumDistance(k) - //Dist total
                                        getDistanceCenter(k, trucks.get(k).get(l).getOrder(0).getGasStation()) + //La vuelta gasolinera 1 centro distr cuando era orden sola
                                        getDistanceGas(trucks.get(k).get(l).getOrder(0).getGasStation(), order.getGasStation()) + //Dist entre gasolineras
                                        getDistanceCenter(k, order.getGasStation())); //Dist gasolinera 2 al centro distribucion
                            }
                            //Cuando encontramos un espacio nulo intentamos introducir la orden si cumple la restriccion de distancia
                            if ((trucks.get(k).get(l).getOrder(m) == null) &&
                                (k == ghost || (m == 0 && dist_first_order <= max_distance) || (m == 1 && dist_second_order <= max_distance))){
                                trucks.get(k).get(l).setOrder(order, m);
                                order_located = true;
                            }
                            m++;
                        }
                        l++;
                    }
                    k++;
                }
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

    private int sumDistance(int i){
        int sum = 0;
        for (int j = 0; j < trucks.get(i).size(); ++j){
            sum += getDistanceTrip(i, j);
        }
        return sum;
    }

    protected boolean canSwap(int i, int j, int k, int l, int m, int n) {
        if (trucks.get(i).get(j).getOrder(k) == null && trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }

        boolean dOk;

        if (i == getGhost()){
            if (trucks.get(l).get(m).getOrder(n) == null) {
                int d2 = sumDistance(l);
                int d2c1 = getDistanceCenter(l, trucks.get(i).get(j).getOrder(k).getGasStation());
                dOk = d2+ d2c1 <= max_distance;
            }
            else {
                dOk = true;
            }
        }
        else {
            if (trucks.get(i).get(j).getOrder(k) == null) {
                int d1 = sumDistance(i);
                int d1c2 = getDistanceCenter(i, trucks.get(l).get(m).getOrder(n).getGasStation());
                dOk = d1 + d1c2 <= max_distance;
            } else if (trucks.get(l).get(m).getOrder(n) == null) {
                int d2 = sumDistance(l);
                int d2c1 = getDistanceCenter(l, trucks.get(i).get(j).getOrder(k).getGasStation());
                dOk = d2+ d2c1 <= max_distance;
            } else{
                int d1 = sumDistance(i);
                int d2 = sumDistance(l);

                int d1c1 = getDistanceCenter(i, trucks.get(i).get(j).getOrder(k).getGasStation());
                int d1c2 = getDistanceCenter(i, trucks.get(l).get(m).getOrder(n).getGasStation());
                int d2c2 = getDistanceCenter(l, trucks.get(l).get(m).getOrder(n).getGasStation());
                int d2c1 = getDistanceCenter(l, trucks.get(i).get(j).getOrder(k).getGasStation());


                dOk = d1 - d1c1 + d1c2 <= max_distance && d2 - d2c2 + d2c1 <= max_distance;
            }
        }
        return (i != l || j != m) && dOk;
    }

    protected boolean isNullOrder(int i, int j, int k){
        return trucks.get(i).get(j).getOrder(k) == null;
    }

    public State getCopy(){
        ArrayList<ArrayList<Trip>> copy = new ArrayList<>(getGhost() + 1);

        for (int i = 0; i < ghost + 1; i++){
            copy.add(new ArrayList<>());
            for (int j = 0; j < trucks.get(i).size(); ++j){
                copy.get(i).add(trucks.get(i).get(j).getCopy());
            }
        }

        return new State(copy);
    }

    ArrayList<ArrayList<Trip>> getState(){
        return trucks;
    }

    private int getDistanceGas(int i, int j) {
        return Math.abs(getGas().get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(gas.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    private int getDistanceCenter(int i, int j) {
        return Math.abs(distr.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(distr.get(i).getCoordY() - gas.get(j).getCoordY());
    }

    private int getDistanceTrip(int i, int j) {
        if (trucks.get(i).get(j).getOrder(0) != null){
            int firstGas = trucks.get(i).get(j).getOrder(0).getGasStation();
            if (trucks.get(i).get(j).getOrder(1) != null){
                int secondGas = trucks.get(i).get(j).getOrder(1).getGasStation();
                return getDistanceCenter(i, firstGas) + getDistanceGas(firstGas, secondGas) + getDistanceCenter(i, secondGas);
            }
            else {
                return 2 * getDistanceCenter(i, firstGas);
            }
        }
        else if (trucks.get(i).get(j).getOrder(1) != null){
            int secondGas = trucks.get(i).get(j).getOrder(1).getGasStation();
            return 2 * getDistanceCenter(i, secondGas);
        }
        return 0;
    }

    private int getBenefits() {
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

    private int getCostTravels() {
        int distance_cost = 0;
        for (int i = 0; i < ghost; ++i) {
            distance_cost += sumDistance(i);
        }
        return distance_cost * 2;
    }

    private static CentrosDistribucion getDistr(){
        return distr;
    }

    private static Gasolineras getGas(){
        return gas;
    }

    private static int getGhost(){
        return ghost;
    }

    public int getHeuristic(){
        return -(getBenefits() - getCostTravels());
    }
}
