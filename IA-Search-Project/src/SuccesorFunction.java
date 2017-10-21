import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
/**
 * @author Javier Bejar
 *
 */
public class SuccesorFunction implements SuccessorFunction {


    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        State state= (State) aState;
        ArrayList<ArrayList<Trip>> trucks = state.getState();

        for (int i = trucks.size() - 1; i >= 0; --i){
            for (int j = 0; j < trucks.get(i).size(); ++j){
                for (int k = 0; k < 2; ++k){
                    for (int l = (i == trucks.size() - 1) ? i - 1 : i){
                        for (int m = 0; m < trucks.get(l).size(); ++m){
                            State nState = new State(trucks);
                            if (nState.canMove(i, j, k, l, m)){
                                Successor s = new Successor("", nState);
                                retVal.add(s);
                            }
                            for (int n = 0; n < 2; ++n){

                            }
                        }
                    }
                }
            }
        }

        return (retVal);

    }

}