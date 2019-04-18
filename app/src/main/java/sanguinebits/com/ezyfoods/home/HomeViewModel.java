package sanguinebits.com.ezyfoods.home;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import model.CartItem;
import model.Dish;
import model.Notification;

public class HomeViewModel extends android.arch.lifecycle.ViewModel {
    public Dish selectedDish;
    private MutableLiveData<List<Dish>> dishes;
    private MutableLiveData<List<CartItem>> cartItems;
    private MutableLiveData<List<Notification>> notifications;

    public boolean isDishesChanged = false;
    public boolean isLogin= false;

    public MutableLiveData<List<Dish>> getDishes() {
        if (dishes == null) {
            dishes = new MutableLiveData<>();
            dishes.setValue(new ArrayList<Dish>());
        }
        return dishes;
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        if (cartItems == null) {
            cartItems = new MutableLiveData<>();
            cartItems.setValue(new ArrayList<CartItem>());
        }
        return cartItems;
    }

    public MutableLiveData<List<Notification>> getNotifications() {
        if (notifications == null) {
            notifications = new MutableLiveData<>();
            notifications.setValue(new ArrayList<Notification>());
        }
        return notifications;
    }
}
