import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Test6 extends writer {
    public static void main(String[] args) {
        try {
            //El Experimento pretende comparar el número de peticiones atendidas segun el coste de cada kilometro reocrrido
            //y la influencia de el numero de días que lleva la peticion sin atender
            System.out.println("Experiment number 6:");
            for (int i = 0; i < 10; ++i) {
                Date d1, d2, d3, d4;
                Calendar c1, c2, c3, c4;
                //Fijamos el seed random para esta repetición del experimento
                int seed = ThreadLocalRandom.current().nextInt(0, 10000);
                //Generador fijado en el experimento 1
                SuccessorFunction succesorFunction = new SuccesorFunction2();
                //Heurístico fijado en el experimento 1 (No tenemos en cuenta perdidas por no atender una petición)
                HeuristicFunction heuristic = new HeuristicFunction1();
                //El experimento 6 trabaja con HC
                Search search = new HillClimbingSearch();
                //La distribucion creada será identtica solo varia el precio
                CentrosDistribucion centros_distribucion = new CentrosDistribucion(10, 1, seed);
                Gasolineras gasolineras = new Gasolineras(100, seed);
                Problem problem;
                int precio_km = 1;
                //Con este bucle trataremos los casos precio km =[2,...,2^10]
                for (int j = 0; j < 10; ++j) {
                    precio_km = 2*precio_km;
                    System.out.println("Iteration number " + (i+1) + ", km_price: " + precio_km);
                    //Generamos el estado inicial, ahora teniendo en cuenta el precio del km
                    State initial_state = new State(gasolineras, centros_distribucion, precio_km);
                    //Estado inicial fijado en el experimento 2
                    initial_state.emptyTrips();
                    problem = new Problem(initial_state, succesorFunction, new IAGoalTest(), heuristic);
                    d1 = new Date();
                    SearchAgent agent = new SearchAgent(problem, search);
                    d2 = new Date();
                    c1 = Calendar.getInstance();
                    c2 = Calendar.getInstance();
                    c1.setTime(d1);
                    c2.setTime(d2);
                    long time = (c2.getTimeInMillis() - c1.getTimeInMillis());
                    int val = -((State) search.getGoalState()).getHeuristic2();
                    System.out.println("Execution time: " + time);
                    System.out.println("Benefits: " + val);
                    System.out.println("Relation: " + val / time);
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    ArrayList<String> write = new ArrayList<>();
                    write.add("It " + (i+1) + " Mult " + j);
                    write.add("Time");
                    write.add(Long.toString(time));
                    write.add("Benefits");
                    write.add(Integer.toString(val));
                    ArrayList<ArrayList<Trip>> trucks = ((State) search.getGoalState()).getState();
                    int order_count = 0;
                    ArrayList<Integer> order_day = new ArrayList<Integer>();
                    for (int l = 0; l < trucks.size() - 1; l++){
                        for (int m = 0; m < trucks.get(l).size(); m++){
                            for (int n = 0; n < 2; n++){
                                if (trucks.get(l).get(m).getOrder(n) != null){
                                    order_count++;
                                    int gas_station_num = trucks.get(l).get(m).getOrder(n).getGasStation();
                                    int order_num = trucks.get(l).get(m).getOrder(n).getNumOrder();
                                    order_day.add(gasolineras.get(gas_station_num).getPeticiones().get(order_num));
                                }
                            }
                        }
                    }
                    write.add("Iteration number " + (i+1) + ", km_price: " + precio_km);
                    write.add("Orders: " + order_count);
                    write.add("Days: " + order_day.toString());
                    write_csv("test6.csv", write);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
