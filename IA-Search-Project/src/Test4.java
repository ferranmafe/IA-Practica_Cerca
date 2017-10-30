import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Test4 extends writer {
    private static final int steps = 2500;
    private static final int stiter = 100;
    private static final int k = 60;
    private static final double lambda = 0.0003;
    private static int ndc = 10;
    private static int ngas = 100;

    public static void main(String[] args) {
        try {
            for (int z = 0; z < 20; ++z) {
                for (int i = 0; i < 10; ++i) {
                    Date d1, d2;
                    Calendar c1, c2;

                    CentrosDistribucion centros_distribucion = new CentrosDistribucion(ndc, 1, 1234);
                    Gasolineras gasolineras = new Gasolineras(ngas, 1234);

                    SuccesorFunction6 succesorFunction = new SuccesorFunction6();

                    HeuristicFunction heuristic = new HeuristicFunction1();

                    State initial_state = new State(gasolineras, centros_distribucion);
                    Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
                    Problem problem;
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
                    System.out.println("Execution time: " + time + "ms");
                    System.out.println("Benefits: " + val);
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    ArrayList<String> write = new ArrayList<>();
                    write.add(Long.toString(time));
                    write_csv("test4_SA" + i +".csv", write);
                }
                ndc += 10;
                ngas += 100;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
