import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SuccesorFunction6 implements SuccessorFunction {
    private static int steps;
    private static int stiter;
    private static int k;
    private static double lambda;

    public SuccesorFunction6(int steps, int stiter, int k, double lambda) {
        this.steps = steps;
        this.stiter = stiter;
        this.k = k;
        this.lambda = lambda;
    }
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State state = ((State) aState).getCopy();
        ArrayList<ArrayList<Trip>> trucks = state.getState();
        while (retVal.size() > 0) {
            int operator = ThreadLocalRandom.current().nextInt(2);
            int i = ThreadLocalRandom.current().nextInt(trucks.size());
            int j = ThreadLocalRandom.current().nextInt(trucks.get(i).size());
            int k = ThreadLocalRandom.current().nextInt(2);
            int l = ThreadLocalRandom.current().nextInt(trucks.size());
            int m = ThreadLocalRandom.current().nextInt(trucks.get(l).size());
            int n = ThreadLocalRandom.current().nextInt(2);
            if (operator == 0) {
                if (state.canAddOrder(i, j, k, l)) {
                    State nState = state.getCopy();
                    nState.addOrder(i, j, k, l);
                    retVal.add(new Successor(
                            "Add: (" + i + ", " + j + ", " + k + ") -> (" + l + ")",
                            nState));
                }
            } else if (operator == 1) {
                if (state.canSwapOrder4(i, j, k, l, m, n)) {
                    State nState = state.getCopy();
                    nState.swapOrder(i, j, k, l, m, n);
                    retVal.add(new Successor(
                            "Swap: (" + i + ", " + j + ", " + k + ") -> (" + l + ", " + m + ", " + n + ")",
                            nState));
                }
            }
        }
        return (retVal);
    }
}