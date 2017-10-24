import IA.Gasolina.*;

import java.lang.reflect.Array;
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

    public void randomTrips() {
        Random randomGenerator = new Random();

        ArrayList<Order> arrayOrders = new ArrayList<Order>();
        for (int i = 0; i < gas.size(); ++i) {
            ArrayList<Integer> gasOrders = gas.get(i).getPeticiones();
            for (int j = 0; j < gasOrders.size(); ++j) {
                Order o = new Order(i, j);
                arrayOrders.add(o);
            }
        }

        int peticiones_totales = arrayOrders.size();
        int nulls = 0;
        int no_nulls = 0;
        for (int i = 0; i < ghost; ++i) {
            for (int j = 0; j < 5; ++j) {
                Order[] orders_to_add = new Order[2];
                boolean ordersNull = false;
                if (arrayOrders.size() > 0) {
                    int random_order_1 = randomGenerator.nextInt(arrayOrders.size());
                    orders_to_add[0] = arrayOrders.get(random_order_1);
                    arrayOrders.remove(orders_to_add[0]);
                    ++no_nulls;
                } else {
                    orders_to_add[0] = null;
                    ordersNull = true;
                    ++nulls;
                }
                if (arrayOrders.size() > 0) {
                    int random_order_2 = randomGenerator.nextInt(arrayOrders.size());
                    orders_to_add[1] = arrayOrders.get(random_order_2);
                    arrayOrders.remove(orders_to_add[1]);
                    ++no_nulls;
                } else {
                    orders_to_add[1] = null;
                    ++nulls;
                }

                if (trucks.get(i).size() < 5) {
                    trucks.get(i).add(new Trip(orders_to_add));
                }
                else if (!ordersNull){
                    trucks.get(ghost).add(new Trip(orders_to_add));
                }
                if (sumDistance(i) > 640) {
                    trucks.get(i).remove(orders_to_add);
                    trucks.get(ghost).add(new Trip(orders_to_add));
                }
            }
        }
        int null_orders = 0;
        int not_null_orders = 0;
        for (int i = 0; i < trucks.size(); ++i) {
            for (int j = 0; j < trucks.get(i).size(); ++j) {
                Order[] orders = trucks.get(i).get(j).getOrders();
                if (orders[0] != null) ++not_null_orders;
                else ++null_orders;
            }
        }

        for (int i = arrayOrders.size() - 1; i >= 0; i-= 2) {
            Order[] o = new Order[2];
            o[0] = arrayOrders.get(i);
            if (i - 1 == -1) {
                o[1] = null;
                ++nulls;
            }
            else {
                o[1] = arrayOrders.get(i - 1);
                ++nulls;
            }
            trucks.get(ghost).add(new Trip(o));
        }

        while (trucks.get(ghost).size() < peticiones_totales / 2) {
            Order[] orders_to_add = new Order[2];
            orders_to_add[0] = null;
            orders_to_add[1] = null;
            nulls += 2;
            trucks.get(ghost).add(new Trip(orders_to_add));
        }


        System.out.println(peticiones_totales);
        System.out.println(null_orders);
        System.out.println(not_null_orders);
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


    public void move(int i, int j, int k, int l, int m, int n) {
        //Hace como el swap pero cambiara una orden nula por una no nula pero lo mira canMove
        Order aux = trucks.get(i).get(j).changeOrder(trucks.get(l).get(m).getOrder(n), k);
        trucks.get(l).get(m).setOrder(aux, n);
    }

    protected boolean canMove(int i, int j, int k, int l, int m, int n) {
        //Miro que el camion de entrada sea el fantasma i su orden sea no nula y el target del intercambio sea nulo
        if (i != ghost || trucks.get(i).get(j).getOrder(k) == null || trucks.get(l).get(m).getOrder(n) != null) {
            return false;
        }

        //Aqui si era nulo recalculo la dist maxima que tendria dependiendo si la otra orden del trip era nula tambien o no
        //Si era nula es el doble de la distancia del centro a la gasolineta
        //Si no era nula quito la vuelta del recorrido anteriro y añado la ditancia entre gasolineras y la nueva vuelta
        boolean dOk;
        int dist_tot = 0;
        int gas_num_new_order = trucks.get(i).get(j).getOrder(k).getGasStation();
        if (trucks.get(l).get(m).getOrder((n+1)%2) == null){
            dist_tot = (sumDistance(l) + 2 * getDistanceCenter(l, trucks.get(i).get(j).getOrder(k).getGasStation()));
        }
        else {
            int gas_num_other_order = trucks.get(l).get(m).getOrder((n+1)%2).getGasStation();
            dist_tot = (sumDistance(l) - //Dist total
                    getDistanceCenter(l, gas_num_other_order) + //La vuelta gasolinera 1 centro distr cuando era orden sola
                    getDistanceGas(gas_num_other_order, gas_num_new_order) + //Dist entre gasolineras
                    getDistanceCenter(l, gas_num_new_order)); //Dist gasolinera 2 al centro distribucion

        }
        return (i != l || j != m) && (dist_tot < max_distance);
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
        /*

        WORSE, SLOWER VERSION

        swap(i, j, k, l, m, n);
        boolean dOk = (i == ghost || sumDistance(i) <= max_distance) && sumDistance(l) <= max_distance;
        swap(i, j, k, l, m, n);
        */
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

        return (i != l || j != m) && dOk;
    }

    protected boolean canSwap2(int i, int j, int k, int l, int m, int n) {
        if (trucks.get(i).get(j).getOrder(k) == null || trucks.get(l).get(m).getOrder(n) == null) {
            return true;
        }
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

    private static int getGhost(){
        return ghost;
    }

    public int getHeuristic1(){
        return -(getBenefits() - getCostTravels());
    }

    public int getHeuristic2() { return -(getBenefits() - (getCostTravels() + getLosses()));}

    public int getHeuristic3(double a, double b, double c) {return (int) (-(a * getBenefits() - (b * getCostTravels() + c* getLosses())));}
}
