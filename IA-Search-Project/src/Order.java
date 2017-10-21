/**
 * Created by Zebs on 18/10/17.
 */
public class Order {

    private int gasStation;
    private int numOrder;

    public Order(){

    }

    public Order(int g, int n){
        gasStation = g;
        numOrder = n;
    }

    public int getGasStation(){

        return gasStation;
    }

    public int getNumOrder(){
        return numOrder;
    }
}
