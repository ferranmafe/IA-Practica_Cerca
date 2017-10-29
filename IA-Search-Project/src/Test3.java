import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Test3 extends writer {
    private static final int steps = 2000;
    private static final int stiter = 80;

    public static void main(String[] args) {
        try {
            ArrayList<Integer> k = new ArrayList<Integer>(){{add(1); add(5); add(25); add(125);}};
            ArrayList<Double> lambda = new ArrayList<Double>() {{add(1.0); add(0.01); add(0.0001);}};
            for (int j = 0; j < k.size(); ++j) {
                for (int z = 0; z < lambda.size(); ++z) {
                    for (int i = 0; i < 10; ++i) {
                        Date d1, d2, d3, d4;
                        Calendar c1, c2, c3, c4;

                        CentrosDistribucion centros_distribucion = new CentrosDistribucion(10, 1, 1234);
                        Gasolineras gasolineras = new Gasolineras(100, 1234);

                        SuccesorFunction6 succesorFunction = new SuccesorFunction6(steps, stiter, k.get(j), lambda.get(z));

                        HeuristicFunction heuristic = new HeuristicFunction1();

                        State initial_state = new State(gasolineras, centros_distribucion);
                        Search search = new SimulatedAnnealingSearch(steps, stiter, k.get(j), lambda.get(z));
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
                        System.out.println("Execution time: " + time);
                        System.out.println("Benefits: " + val);
                        System.out.println();
                        System.out.println();
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
