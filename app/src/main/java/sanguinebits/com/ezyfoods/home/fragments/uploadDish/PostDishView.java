package sanguinebits.com.ezyfoods.home.fragments.uploadDish;

import base.BaseView;
import model.ApiResponse;

public interface PostDishView extends BaseView {
    void onDishPosted(ApiResponse commonPojo);
}
