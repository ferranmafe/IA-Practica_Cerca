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
        State state = (State) aState;
        ArrayList<ArrayList<Trip>> trucks = state.getState();

        //BUCLE PARA SWAPS
        //
        //Propiedades:
        //  Cada camión solo se cambia órdenes con camiones que están por delante suyo, y consigo mismo
        //  El camión fantasma no cambia órdenes con nadie, ya que todos los camiones han cambiado antes
        //  sus órdenes por órdenes del camión fantasma, y no tiene sentido que cambie consigo mismo
        //  Cuando un camión hace un cambio consigo mismo, no lo hará con dos órdenes de un mismo viaje (canSwap)
        //  Además se comprueban las distancias y que no sean los dos nulos

        boolean nullSwapAllowed, nullSwapDetected;
        nullSwapAllowed = true;
        nullSwapDetected = false;

        for (int i = trucks.size() - 1; i >= 0; --i){
            for (int j = 0; j < trucks.get(i).size(); ++j){
                for (int k = 0; k < 2; ++k){
                    if (i == trucks.size() - 1 && !nullSwapDetected){
                        nullSwapDetected = state.isNullOrder(i, j, k);
                    }
                    if (i == trucks.size() - 1 && state.isNullOrder(i, j, k) && nullSwapAllowed) {
                        for (int l = (i == trucks.size() - 1) ? i - 1 : i; l >= 0; --l) {
                            for (int m = 0; m < trucks.get(l).size(); ++m) {
                                for (int n = 0; n < 2; ++n) {
                                    if (state.canSwap(i, j, k, l, m, n)) {
                                        State nState = new State(trucks);
                                        nState.swap(i, j, k, l, m, n);
                                        retVal.add(new Successor("", nState));
                                    }
                                }
                            }
                        }
                    }
                    nullSwapAllowed = !nullSwapDetected;
                }
            }
        }
        return (retVal);
    }
}