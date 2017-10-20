/**
 * Created by Zebs on 18/10/17.
 */
public class Trip {

    private Order [] orders;

    public Trip (){

    }

    public Trip (Order[] o){
        orders = new Order[2];
        orders[0] = o[0];
        orders[1] = o[1];
    }

    public Order[] getOrders (){
        return orders.clone();
    }

    public Order changeOrder (Order o, int i){
        Order aux = orders[i];
        orders[i] = o;

        return aux;
    }

    void setOrder(Order o, int i){
        orders[i] = o;
    }

    Order getOrder (int i){
        return orders[i];
    }

}
