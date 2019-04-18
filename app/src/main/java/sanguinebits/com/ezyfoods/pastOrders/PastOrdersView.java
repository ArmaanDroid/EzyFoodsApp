package sanguinebits.com.ezyfoods.pastOrders;

import java.util.List;

import base.BaseView;
import model.Order;

public interface PastOrdersView extends BaseView {
    void onOrdersLoaded(List<Order> orders);
}
