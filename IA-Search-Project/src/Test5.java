import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Test5 extends writer {
    public static void main(String[] args) {
        try {
            //El Experimento pretende comparar distancias reocorridas y beneficions obtenidos al reducir el numero de
            //centros de distribucion (augmentar multiplicidad de los camiones)
            //Estudiaremos 10 iteraciones y en cada una se generará un seed con multiplicidad 1 y 2 para obtener diferencias
            System.out.println("Experiment number 5:");
            ArrayList<ArrayList<String>> beneficios = new ArrayList<ArrayList<String>>();
            beneficios.add(new ArrayList<>());
            beneficios.add(new ArrayList<>());
            ArrayList<ArrayList<String>> km_recorridos = new ArrayList<ArrayList<String>>();
            km_recorridos.add(new ArrayList<>());
            km_recorridos.add(new ArrayList<>());

            for (int i = 0; i < 10; ++i) {
                Date d1, d2, d3, d4;
                Calendar c1, c2, c3, c4;
                //Fijamos el seed random para esa iteración
                int seed = ThreadLocalRandom.current().nextInt(0, 10000);
                //Generador fijado en el experimento 1
                SuccessorFunction succesorFunction = new SuccesorFunction2();
                //Heurístico fijado en el experimento 1 (No tenemos en cuenta perdidas por no atender una petición)
                HeuristicFunction heuristic = new HeuristicFunction1();
                //El experimento 5 trabaja con HC
                Search search = new HillClimbingSearch();
                Problem problem;
                for (int j = 1; j <= 2; ++j) {
                    System.out.println("Iteration number " + (i+1) + ", multiplicity of the distirbution centers: " + j);
                    //Estudiamos el caso con multiplicidad J (J[1,2])
                    CentrosDistribucion centros_distribucion = new CentrosDistribucion(10/j, j, seed);
                    Gasolineras gasolineras = new Gasolineras(100, seed);
                    //Generamos el estado inicial
                    State initial_state = new State(gasolineras, centros_distribucion);
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
                    beneficios.get(j-1).add(Integer.toString(val));

                    ArrayList<ArrayList<Trip>> trucks = ((State) search.getGoalState()).getState();
                    int suma_dist = 0;
                    for (int k = 0; k < trucks.size(); k++){
                        suma_dist += ((State) search.getGoalState()).sumDistance(k);
                    }
                    km_recorridos.get(j-1).add(Integer.toString(suma_dist));

                    /*
                    ArrayList<String> write = new ArrayList<>();
                    write.add("It " + (i+1) + " Mult " + j);
                    write.add("Time");
                    write.add(Long.toString(time));
                    write.add("Benefits");
                    write.add(Integer.toString(val));
                    ArrayList<ArrayList<Trip>> trucks = ((State) search.getGoalState()).getState();
                    for (int k = 0; k < trucks.size(); k++){
                        write.add(Integer.toString(k));
                        write.add(Integer.toString(((State) search.getGoalState()).sumDistance(k)));
                    }
                    write_csv("test5.csv", write);
                    */
                }
            }
            write_csv2("test5_benefits.csv", beneficios);
            write_csv2("test5_distances.csv", km_recorridos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
