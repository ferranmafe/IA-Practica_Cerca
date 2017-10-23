import aima.search.framework.HeuristicFunction;

public class HeuristicFunction3 implements HeuristicFunction
{
    private double x1;
    private double x2;
    private double x3;

    public HeuristicFunction3(double a, double b, double c) {
        this.x1 = a;
        this.x2 = b;
        this.x3 = c;
    }

    public boolean equals(Object obj)
    {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }

    public double getHeuristicValue(Object state){
        return ((State) state).getHeuristic3(this.x1, this.x2, this.x3);

    }
}