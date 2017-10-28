import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Test2 extends writer {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; ++i) {
                Date d1, d2, d3, d4;
                Calendar c1, c2, c3, c4;


                int seed = ThreadLocalRandom.current().nextInt(0, 10000);
                CentrosDistribucion centros_distribucion = new CentrosDistribucion(10, 1, seed);
                Gasolineras gasolineras = new Gasolineras(100, seed);

                SuccessorFunction succesorFunction = new SuccesorFunction2();

                HeuristicFunction heuristic = new HeuristicFunction1();

                State initial_state = new State(gasolineras, centros_distribucion);
                Search search = new HillClimbingSearch();
                Problem problem;

                for (int j = 0; j < 4; ++j) {
                    System.out.println("Inistate number: " + j);
                    switch (j) {
                        case 0:
                            initial_state.emptyTrips();
                            break;
                        case 1:
                            initial_state.randomTrips();
                            break;
                        case 2:
                            initial_state.orderedTrips();
                            break;
                        case 3:
                            initial_state.greedyTrips();
                            break;
                        default:
                            initial_state.emptyTrips();
                    }
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
                    write.add(Long.toString(time));
                    write_csv("test1_inistate_" + j + "time.csv", write);
                    write.remove(0);
                    write.add(Integer.toString(val));
                    write_csv("test1_inistate_" + j + "benefits.csv", write);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
