import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class SuccesorFunction3 implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        State state = ((State) aState).getCopy();
        ArrayList<ArrayList<Trip>> trucks = state.getState();
        for (int i = 0; i < trucks.size();i++) {
            for (int j = 0; j < trucks.get(i).size(); ++j) {
                for (int k = 0; k < 2; ++k) {
                    if (trucks.get(i).get(j).getOrder(k) != null) {
                        for (int l = 0; l < i; ++l) {
                            for (int m = 0; m < trucks.get(l).size(); ++m) {
                                for (int n = 0; n < 2; ++n) {
                                    if (state.canSwap2(i, j, k, l, m, n)) {
                                        State nState = state.getCopy();
                                        nState.swap(i, j, k, l, m, n);
                                        retVal.add(new Successor(
                                            "Swap: (" + i + ", " + j + ", " + k + ") -> (" + l + ", " + m + ", " + n + ")",
                                            nState));
                                    }
                                    if (state.canMove2(i, j, k, l, m, n)) {
                                        State nState = state.getCopy();
                                        nState.move(i, j, k, l, m, n);
                                        retVal.add(new Successor(
                                            "Move: (" + i + ", " + j + ", " + k + ") -> (" + l + ", " + m + ", " + n + ")",
                                            nState));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return (retVal);
    }
}