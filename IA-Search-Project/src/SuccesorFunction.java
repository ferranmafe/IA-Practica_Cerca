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

        //BUCLE PARA SWAPS
        //
        //Propiedades:
        //  Cada camión solo se cambia órdenes con camiones que están por delante suyo, y consigo mismo
        //  El camión fantasma no cambia órdenes con nadie, ya que todos los camiones han cambiado antes
        //  sus órdenes por órdenes del camión fantasma, y no tiene sentido que cambie consigo mismo
        //  Cuando un camión hace un cambio consigo mismo, no lo hará con dos órdenes de un mismo viaje (canSwap)
        //  Además, canSwap comprueba que al menos uno de los dos

        for (int i = 0; i < trucks.size(); ++i){
            for (int j = 0; j < trucks.get(i).size(); ++j){
                for (int k = 0; k < 2; ++k){
                    for (int l = (i == trucks.size() - 1) ? i + 1 : i; i < trucks.size(); ++i){
                        for (int m = 0; m < trucks.get(l).size(); ++m){
                            State nState = new State(trucks);
                            for (int n = 0; n < 2; ++n){
                                if (nState.canSwap(i, j, k, l, m, n)){
                                    nState.swap(i, j, k, l, m, n);
                                    retVal.add(new Successor("", nState));
                                }
                            }
                        }
                    }
                }
            }
        }

        //BUCLE PARA MOVES

        return (retVal);

    }

}