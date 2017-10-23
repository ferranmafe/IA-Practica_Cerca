import aima.search.framework.HeuristicFunction;

public class HeuristicFunction5 implements HeuristicFunction
{
    public boolean equals(Object obj)
    {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }

    public double getHeuristicValue(Object state){
        return ((State) state).getHeuristic5();

    }
}

