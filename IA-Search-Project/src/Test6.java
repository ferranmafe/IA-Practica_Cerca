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
            ArrayList<ArrayList<String>> datos_csv = new ArrayList<ArrayList<String>>();
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
                    if (i == 0) {
                        if (j == 0) {
                            datos_csv.add(new ArrayList<>());
                            datos_csv.get(0).add("Cost_km");
                        }
                        datos_csv.add(new ArrayList<>());
                        datos_csv.get(5*j+1).add("O" + j);
                        datos_csv.add(new ArrayList<>());
                        datos_csv.get(5*j+2).add("P0" + j);
                        datos_csv.add(new ArrayList<>());
                        datos_csv.get(5*j+3).add("P1" + j);
                        datos_csv.add(new ArrayList<>());
                        datos_csv.get(5*j+4).add("P2" + j);
                        datos_csv.add(new ArrayList<>());
                        datos_csv.get(5*j+5).add("P3" + j);
                        datos_csv.get(0).add(Integer.toString(precio_km));
                    }
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

                    ArrayList<ArrayList<Trip>> trucks = ((State) search.getGoalState()).getState();
                    int order_count = 0;
                    ArrayList<Integer> order_day = new ArrayList<Integer>();
                    for (int l = 0; l < trucks.size() - 1; l++) {
                        for (int m = 0; m < trucks.get(l).size(); m++) {
                            for (int n = 0; n < 2; n++) {
                                if (trucks.get(l).get(m).getOrder(n) != null) {
                                    order_count++;
                                }
                            }
                        }
                    }
                    datos_csv.get(5*j+1).add(Integer.toString(order_count));

                    for (int day_num = 0; day_num < 4; day_num++){
                        double order_tot = 0;
                        double order_valid = 0;
                        for (int l = 0; l < trucks.size(); l++) {
                            for (int m = 0; m < trucks.get(l).size(); m++) {
                                for (int n = 0; n < 2; n++) {
                                    if (trucks.get(l).get(m).getOrder(n) != null ) {
                                        int gas_station_num = trucks.get(l).get(m).getOrder(n).getGasStation();
                                        int order_num = trucks.get(l).get(m).getOrder(n).getNumOrder();
                                        if (gasolineras.get(gas_station_num).getPeticiones().get(order_num) == day_num) {
                                            order_tot++;
                                            if (l != trucks.size() - 1) {
                                                order_valid++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (order_tot == 0.0){
                            datos_csv.get(5 * j + 2 + day_num).add(Double.toString(0.0));
                        }
                        else {
                            datos_csv.get(5 * j + 2 + day_num).add(Double.toString(order_valid / order_tot));
                        }
                    }


                }
            }
            write_csv2("test6.csv", datos_csv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
