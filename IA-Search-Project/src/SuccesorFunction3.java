import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class SuccesorFunction3 implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        State state = ((State) aState).getCopy();
        ArrayList<ArrayList<Trip>> trucks = state.getState();

        //Falta aqui un bucle donde se recorra todas las ordenes del camion fantasma y le aplique un canMove y un move
        //Pueden mejorar eficiencia alguna restriccion mas como no meter en un mismo cambion cn 2 huecos nulls la orden...
        //A pelo ha de funcionar por eso


        return (retVal);
    }
}