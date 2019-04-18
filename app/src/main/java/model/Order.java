package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import model.user.User;

public class Order implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dishId")
    @Expose
    private String dishId;
    @SerializedName("buyerId")
    @Expose
    private String buyerId;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("dish")
    @Expose
    private Dish dish;
    @SerializedName("buyer")
    @Expose
    private User buyer;

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

}