import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccesorFunction4 implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        State state = ((State) aState).getCopy();
        ArrayList<ArrayList<Trip>> trucks = state.getState();
        for (int i = trucks.size() - 1 ; i >= 0; --i) {
            for (int j = 0; j < trucks.get(i).size(); ++j) {
                for (int k = 0; k < 2; ++k) {
                    for (int l = trucks.size() - 1; l >= 0; --l) {
                        if (state.canAddOrder(i,j,k,l)) {
                            State nState = state.getCopy();
                            nState.addOrder(i, j, k, l);
                            retVal.add(new Successor(
                                    "Add: (" + i + ", " + j + ", " + k + ") -> (" + l + ")",
                                    nState));
                        }
                        for (int m = 0; m < trucks.get(l).size(); ++m) {
                            for (int n = 0; n < 2; ++n) {
                                if (state.canSwapOrder4(i, j, k, l, m, n)) {
                                    State nState = state.getCopy();
                                    nState.swapOrder(i, j, k, l, m, n);
                                    retVal.add(new Successor(
                                            "Swap: (" + i + ", " + j + ", " + k + ") -> (" + l + ", " + m + ", " + n + ")",
                                            nState));
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