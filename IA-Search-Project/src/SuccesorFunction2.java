import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class SuccesorFunction2 implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        State state = ((State) aState).getCopy();
        ArrayList<ArrayList<Trip>> trucks = state.getState();

        int i = trucks.size() - 1;

        boolean inserted;

        //Falta aqui un bucle donde se recorra todas las ordenes del camion fantasma y le aplique un canMove y un move
        //Pueden mejorar eficiencia alguna restriccion mas como no meter en un mismo cambion cn 2 huecos nulls la orden...
        //A pelo ha de funcionar por eso
        for (int j = 0; j < trucks.get(i).size(); ++j){
            for (int k = 0; k < 2; ++k){
                if (trucks.get(i).get(j).getOrder(k) != null){
                    for (int l = 0; l < i; ++l){
                        inserted = false;
                        for (int m = 0; !inserted && m <  trucks.get(l).size(); ++m){
                            for (int n = 0; n < 2; ++n){
                                if (state.canMove(i, j, k, l, m, n)){
                                    State nState = state.getCopy();
                                    nState.move(i, j, k, l, m, n);
                                    retVal.add(new Successor("", nState));
                                    inserted = true;
                                    break;
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