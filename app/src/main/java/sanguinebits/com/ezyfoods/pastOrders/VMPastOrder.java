package sanguinebits.com.ezyfoods.pastOrders;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import model.Order;

public class VMPastOrder extends ViewModel {
    MutableLiveData<List<Order>> orderList;

    public MutableLiveData<List<Order>> getOrderList() {
        if(orderList==null){
            orderList=new MutableLiveData<>();
            orderList.setValue(new ArrayList<Order>());
        }
        return orderList;
    }
}
