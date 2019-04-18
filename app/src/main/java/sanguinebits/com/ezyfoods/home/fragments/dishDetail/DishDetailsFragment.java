package sanguinebits.com.ezyfoods.home.fragments.dishDetail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CartItem;
import model.Dish;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import utils.AppConst;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DishDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishDetailsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Dish dish;
    private String mParam2;


    @BindView(R.id.textViewCookName)
    TextView textViewCookName;

    @BindView(R.id.textViewDishName)
    TextView textViewDishName;

    @BindView(R.id.textViewAvailableOn)
    TextView textViewAvailableOn;

    @BindView(R.id.textViewOrderBy)
    TextView textViewOrderBy;

    @BindView(R.id.textViewSpiceLevel)
    TextView textViewSpiceLevel;

    @BindView(R.id.textViewPrice)
    TextView textViewPrice;

    @BindView(R.id.textViewPickUp)
    TextView textViewPickUp;

    @BindView(R.id.textViewDelivery)
    TextView textViewDelivery;

    @BindView(R.id.textViewQuantity)
    TextView textViewQuantity;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

//    @BindView(R.id.textViewComments)
//    TextView textViewComments;

    @BindView(R.id.textView)
    TextView textViewCommentLabel;

    @BindView(R.id.textViewCounter)
    TextView textViewCounter;

    @BindView(R.id.textViewNotRatedYet)
    TextView textViewNotRatedYet;

    @BindView(R.id.imageView3)
    SimpleDraweeView imageViewDish;

    @BindView(R.id.imageView4)
    SimpleDraweeView imageViewCook;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.recyclerComments)
    RecyclerView recyclerComments;

    private int counter = 0;
    private HomeViewModel vmHome;
    AHBottomNavigation bottomNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vmHome = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        dish = vmHome.selectedDish;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setVisibility(View.VISIBLE);

        Objects.requireNonNull(getActivity()).findViewById(R.id.imageView6).setVisibility(View.VISIBLE);
        bottomNav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNav.hideBottomNavigation(true);
        bottomNav.setVisibility(View.GONE);

        List<CartItem> cartList = vmHome.getCartItems().getValue();
        for (CartItem cartItem : cartList) {
            if (cartItem.getDish().getId().equalsIgnoreCase(dish.getId())) {
                counter = cartItem.getCount();
                break;
            }
        }
        return inflater.inflate(R.layout.fragment_dish_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomNav.setVisibility(View.VISIBLE);
//                Navigation.findNavController(view).navigate(R.id.action_detail_to_cartFragment);
//            }
//        });
        textViewDishName.setText(TextUtils.concat(dish.getTitle(), " - ", dish.getCuisineType()));
        textViewCookName.setText(dish.getPreparedBy().getName());

        textViewCounter.setText(String.valueOf(counter));
        textViewPrice.setText(TextUtils.concat("Price: $", dish.getPrice()));
        textViewSpiceLevel.setText(TextUtils.concat("Spice Level: ", dish.getSpiceLevel()));
        textViewPickUp.setText(TextUtils.concat("Pick Up: ", dish.getPickUp()));
        textViewDelivery.setText(TextUtils.concat("Delivery: ", dish.getDelivery()));
        textViewQuantity.setText(TextUtils.concat("Quantity: ", dish.getQuantity()));
        textViewDescription.setText(dish.getDescription());
        if (dish.getRating() != null) {
            ratingBar.setVisibility(View.VISIBLE);
            textViewNotRatedYet.setVisibility(View.GONE);
            ratingBar.setRating(dish.getRating());
        } else {
            ratingBar.setVisibility(View.GONE);
            textViewNotRatedYet.setVisibility(View.VISIBLE);
        }
        if (dish.getReviews() != null && !dish.getReviews().isEmpty()) {
            recyclerComments.setVisibility(View.VISIBLE);
            recyclerComments.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerComments.setAdapter(new CommentsAdapter(dish.getReviews()));

//            for (int i = 0; i < dish.getReviews().size(); i++) {
//                textViewComments.append(TextUtils.concat(String.valueOf(i + 1), ". ", dish.getReviews().get(i).toString(), "\n"));
//            }
        } else {
            textViewCommentLabel.setVisibility(View.GONE);
            recyclerComments.setVisibility(View.GONE);
        }

        try {
            Date date = new Date();
            date.setTime(Long.parseLong(dish.getAvailableOn()));
            textViewAvailableOn.setText(TextUtils.concat("Available On: ", dateFormatApi.format(date)));

            date.setTime(Long.parseLong(dish.getOrderBy()));
            textViewOrderBy.setText(TextUtils.concat("Order By: ", dateFormatApi.format(date)));

        } catch (Exception e) {
            e.printStackTrace();
        }

//        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
//                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
//                .setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER)
//                .setProgressBarImage(R.drawable.icon_large_process)
//                .setFailureImage(R.color.gradientLast)
//                .build();
//
//        GenericDraweeHierarchy hierarchyCook = new GenericDraweeHierarchyBuilder(getResources())
//                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
//                .setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER)
//                .setProgressBarImage(R.drawable.icon_large_process)
//                .setFailureImage(R.drawable.ic_person)
//                .build();
//
//        imageViewDish.setHierarchy(hierarchy);
//        imageViewCook.setHierarchy(hierarchyCook);

        if (dish.getDishImage() != null && !dish.getDishImage().isEmpty()) {
            String uri = AppConst.DISH_IMAGE_BASE_URL + dish.getDishImage();
            imageViewDish.setImageURI(uri);
        }
        if (dish.getPreparedBy().getProfilePic() != null && !dish.getPreparedBy().getProfilePic().isEmpty()) {
            String uri = AppConst.USER_IMAGE_BASE_URL + dish.getPreparedBy().getProfilePic();
            imageViewCook.setImageURI(uri);
        }
    }


    @OnClick(R.id.textView3)
    void addToCart() {
        if (counter == 0) {
            showToast("Please add some items.");
            return;
        }
        boolean alreadyInCart = false;
        List<CartItem> cartList = vmHome.getCartItems().getValue();
        for (CartItem cartItem : cartList) {
            if (cartItem.getDish().getId().equalsIgnoreCase(dish.getId())) {
                alreadyInCart = true;
                cartItem.setCount(counter);
                break;
            }
        }
        if (!alreadyInCart) {
            CartItem cartItem = new CartItem();
            cartItem.setCount(counter);
            cartItem.setDish(dish);
            cartList.add(cartItem);
        }
        vmHome.getCartItems().setValue(cartList);

        showToast(counter + " " + dish.getTitle() + " added to your cart.");
//        assert getFragmentManager() != null;
//        getFragmentManager().popBackStack();
    }


    @OnClick(R.id.imageMinus)
    void minus() {
        if (counter == 0)
            return;
        counter--;
        textViewCounter.setText(String.valueOf(counter));
    }

    @OnClick(R.id.imageAdd)
    void add() {
        counter++;
        textViewCounter.setText(String.valueOf(counter));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
