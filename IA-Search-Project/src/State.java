import IA.Gasolina.*;

import java.util.ArrayList;
import java.util.Random;

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
        initializeStateToNull();
        int k = 0;
        int l = 0;
        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order order = new Order(i, j);
                trucks.get(ghost).get(k).setOrder(order, l % 2);
                ++l;
                if (l % 2 == 0) ++k;
            }
        }
    }

    public void greedyTrips() {
        //Rellenamos todos los estados como nulos y luego iteraremos calulando distancias modo greedy y actualizando trips
        initializeStateToNull();

        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order order = new Order(i, j);
                boolean order_allocated = false;
                int k = 0;
                while (!order_allocated && k < trucks.size()){
                    int l = 0;
                    while (!order_allocated && l < trucks.get(k).size()){
                        int m = 0;
                        while (!order_allocated && m < 2){
                            //Cuando encontramos un espacio nulo intentamos introducir la orden si cumple la restriccion de distancia
                            if ((trucks.get(k).get(l).getOrder(m) == null) && isAllowedAddDistance(k, l, m, order)){
                                trucks.get(k).get(l).setOrder(order, m);
                                order_allocated = true;
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

    public void randomTrips() {
        initializeStateToNull();
        Random randomGenerator = new Random();
        ArrayList<Order> arrayOrders = new ArrayList<Order>();
        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order o = new Order(i, j);
                boolean order_allocated = false;
                while (!order_allocated){
                    int l = randomGenerator.nextInt(trucks.size());
                    int m = randomGenerator.nextInt(trucks.get(l).size());
                    int n = randomGenerator.nextInt(2);
                    if (trucks.get(l).get(m).getOrder(n) == null && isAllowedAddDistance(l, m, n, o)){
                        trucks.get(l).get(m).setOrder(o, n);
                        order_allocated = true;
                    }
                }

            }
        }
    }

    protected  void initializeStateToNull() {
        for (int i = 0; i < ghost; ++i){
            for (int j = 0; j < max_trips; ++j){
                Order[] o = new Order[2];
                o[0] = null;
                o[1] = null;
                trucks.get(i).add(new Trip(o));
            }
        }

        Order[] o = new Order[2];
        o[0] = null;
        o[1] = null;
        int count = 0;

        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                count++;
                if (count % 2 == 1) {
                    trucks.get(ghost).add(new Trip(o));
                }
            }
        }
        if (count % 2 == 0) {
            trucks.get(ghost).add(new Trip(o));
        }
    }

    public void swapOrder(int i, int j, int k, int l, int m, int n) {
        //En la generadora de sucesores el swapOrder se realiza, para cada camión i,
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

    public void addOrder(int i, int j, int k, int l) {
        //Hace como el swapOrder pero cambiara una orden nula por una no nula pero lo mira canAddOrder
        for (int m = 0; m < trucks.get(l).size(); ++m){
            for (int n = 0; n < 2; ++n) {
                if (isNullOrder(l, m, n)) {
                    Order aux = trucks.get(i).get(j).changeOrder(trucks.get(l).get(m).getOrder(n), k);
                    trucks.get(l).get(m).setOrder(aux, n);
                    return;
                }
            }
        }

    }

    protected boolean canAddOrder(int i, int j, int k, int l) {
        if (i != ghost || isNullOrder(i,j,k)) return false;

        for (int m = 0; m < trucks.get(l).size(); ++m){
            for (int n = 0; n < 2; ++n) {
                if (isNullOrder(l, m, n)) {
                    return isAllowedAddDistance(l,m,n, trucks.get(i).get(j).getOrder(k));
                }
            }
        }
        return false;
    }

    private int sumDistance(int i){
        int sum = 0;
        if (i == ghost){
            return 0;
        }
        for (int j = 0; j < trucks.get(i).size(); ++j){
            sum += getDistanceTrip(i, j);
        }
        return sum;
    }

    protected boolean canSwapOrder(int i, int j, int k, int l, int m, int n) {
        //Cambia todos con todos

        if (trucks.get(i).get(j).getOrder(k) == null && trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }
        /*

        WORSE, SLOWER VERSION

        swapOrder(i, j, k, l, m, n);
        boolean dOk = (i == ghost || sumDistance(i) <= max_distance) && sumDistance(l) <= max_distance;
        swapOrder(i, j, k, l, m, n);
        */

        return (i != l || j != m) && isAllowedSwapDistance(i,j,k,l,m,n);
    }

    protected boolean canSwapOrder2(int i, int j, int k, int l, int m, int n) {
        //Cambia solo entre los camiones, pero no con el ghost
        // Sol Ini Vacía : 95680 / 4s
        // Sol Ini Llena : 93316 / 3.5s


        if (trucks.get(i).get(j).getOrder(k) == null || trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }

        return i != ghost && (i > l || (i == l && j < m)) && isAllowedSwapDistance(i,j,k,l,m,n);
    }

    protected boolean canSwapOrder3(int i, int j, int k, int l, int m, int n) {
        //Cambia solo entre un mismo camión

        if (trucks.get(i).get(j).getOrder(k) == null || trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }

        return  (i > l || (i == l && j < m && i != ghost)) && isAllowedSwapDistance(i,j,k,l,m,n);
    }

    protected boolean canSwapOrder4(int i, int j, int k, int l, int m, int n) {
        //Cambia entre un mismo camión, y con el ghost: BEST :)
        // Sol Ini Vacía : 95680 / 4s
        // Sol Ini Llena : 93316 / 3.5s

        if (trucks.get(i).get(j).getOrder(k) == null || trucks.get(l).get(m).getOrder(n) == null) {
            return false;
        }

        return ((i == ghost && i > l) || (i == l && j < m && i != ghost)) && isAllowedSwapDistance(i,j,k,l,m,n);
    }

    protected boolean isAllowedAddDistance(int i, int j, int k, Order o){
        //Aqui si era nulo recalculo la dist maxima que tendria dependiendo si la otra orden del trip era nula tambien o no
        //Si era nula es el doble de la distancia del centro a la gasolineta
        //Si no era nula quito la vuelta del recorrido anteriro y añado la ditancia entre gasolineras y la nueva vuelta

        Order order1 = o;
        Order compOrder1 = trucks.get(i).get(j).getOrder(1-k);
        int d1 = (i == ghost) ? 0 : sumDistance(i);
        int d1g1 = (order1 == null || i == ghost) ? 0 : getDistanceCenter(i, order1.getGasStation());
        int d1g2 = (compOrder1 == null ||i == ghost) ? 0 : getDistanceCenter(i, compOrder1.getGasStation());
        int nd1 = (compOrder1 == null) ? d1 + 2 * d1g1 :
                d1 - d1g2 + getDistanceGas(compOrder1.getGasStation(), order1.getGasStation()) + d1g1;
        return (nd1 <= max_distance || i == ghost);
    }

    protected boolean isAllowedSwapDistance(int i, int j, int k, int l, int m, int n){

        boolean dOk;

        Order order1 = trucks.get(i).get(j).getOrder(k);
        Order order2 = trucks.get(l).get(m).getOrder(n);
        Order compOrder1 = trucks.get(i).get(j).getOrder(1-k);
        Order compOrder2 = trucks.get(l).get(m).getOrder(1-n);

        int d1 = (i == ghost) ? 0 : sumDistance(i);
        int d2 = sumDistance(l);

        int d1g1 = (order1 == null || i == ghost) ? 0 : getDistanceCenter(i, order1.getGasStation());
        int d1g2 = (order2 == null || i == ghost) ? 0 : getDistanceCenter(i, order2.getGasStation());
        int d2g1 = (order1 == null) ? 0 : getDistanceCenter(l, order1.getGasStation());
        int d2g2 = (order2 == null) ? 0 : getDistanceCenter(l, order2.getGasStation());


        int nd1;
        int nd2;

        if (order1 == null){
            if (i == ghost) dOk = true;
            else {
                nd1 = (compOrder1 == null) ? d1 + 2 * d1g2 :
                        d1 - getDistanceCenter(i, compOrder1.getGasStation())
                                + getDistanceGas(compOrder1.getGasStation(), order2.getGasStation()) + d1g2;
                dOk = nd1 <= max_distance;
            }
        }

        else if (order2 == null){
            nd2 = (compOrder2 == null) ? d2 + 2 * (d2g1 - d2g2) :
                    d2 - getDistanceCenter(l, compOrder2.getGasStation()) +
                            getDistanceGas(compOrder2.getGasStation(), order1.getGasStation()) + d2g1;
            dOk = nd2 <= max_distance;
        }

        else {
            nd1 = (compOrder1 == null) ? d1 + 2 * (d1g2 - d1g1) :
                    d1 - d1g1 - getDistanceGas(order1.getGasStation(), compOrder1.getGasStation())
                            + getDistanceGas(compOrder1.getGasStation(), order2.getGasStation()) + d1g2;
            nd2 = (compOrder2 == null) ? d2 + 2 * (d2g1 - d2g2) :
                    d2 - d2g2 - getDistanceGas(order2.getGasStation(), compOrder2.getGasStation()) +
                            getDistanceGas(compOrder2.getGasStation(), order1.getGasStation()) + d2g1;
            dOk = (nd1 <= max_distance || i == ghost) && nd2 <= max_distance;
        }

        return dOk;
    }

    protected boolean isNullOrder(int i, int j, int k){
        return trucks.get(i).get(j).getOrder(k) == null;
    }

    public State getCopy(){
        ArrayList<ArrayList<Trip>> copy = new ArrayList<>(ghost + 1);

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
        return Math.abs(gas.get(i).getCoordX() - gas.get(j).getCoordX()) + Math.abs(gas.get(i).getCoordY() - gas.get(j).getCoordY());
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

    private int getLosses() {
        int losses = 0;
        for (int i = 0; i < trucks.get(ghost).size(); ++i) {
            for (int j = 0; j < 2; ++j) {
                Order order = trucks.get(ghost).get(i).getOrder(j);
                if (order != null) {
                    int gas_station_num = order.getGasStation();
                    int order_num = order.getNumOrder();
                    int dias_peticion = gas.get(gas_station_num).getPeticiones().get(order_num);
                    int percentatge_over_total;
                    int percentatge_over_total_tomorrow;
                    if (dias_peticion == 0) {
                        percentatge_over_total = 102;
                    } else {
                        percentatge_over_total = (int) (100 - Math.pow((double) 2, (double) dias_peticion));
                    }
                    percentatge_over_total_tomorrow = (int) (100 - Math.pow((double) 2, (double) (dias_peticion + 1.0)));
                    losses += (1000 * (percentatge_over_total) / 100) - (1000 * (percentatge_over_total_tomorrow) / 100);
                }
            }
        }
        return losses;
    }

    public int getHeuristic1(){
        return -(getBenefits() - getCostTravels());
    }

    public int getHeuristic2() {
        return -(getBenefits() - (getCostTravels() + getLosses()));
    }

    public int getHeuristic3(double a, double b, double c) {
        return (int) (-(a * getBenefits() - (b * getCostTravels() + c* getLosses())));
    }
}
