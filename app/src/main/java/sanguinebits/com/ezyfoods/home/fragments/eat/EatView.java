package sanguinebits.com.ezyfoods.home.fragments.eat;

import java.util.List;

import base.BaseView;
import model.Dish;

public interface EatView extends BaseView {
    void onDishesLoaded(List<Dish> dishes);
}
