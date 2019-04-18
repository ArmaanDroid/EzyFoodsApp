package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import model.user.User;

public class ApiResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("plan_expiry_date")
    @Expose
    private String planExpiryDate;
    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls = null;

    @SerializedName("dishes")
    @Expose
    private List<Dish> dishes = null;

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("stripeUrl")
    @Expose
    private String stripeUrl;

    @SerializedName("orders")
    @Expose
    private List<Order> orders;

    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStripeUrl() {
        return stripeUrl;
    }

    public void setStripeUrl(String stripeUrl) {
        this.stripeUrl = stripeUrl;
    }

    public String getPlanExpiryDate() {
        return planExpiryDate;
    }

    public void setPlanExpiryDate(String planExpiryDate) {
        this.planExpiryDate = planExpiryDate;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}