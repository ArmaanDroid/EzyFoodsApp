package sanguinebits.com.ezyfoods.home.fragments.cart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.CartItem;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import sanguinebits.com.ezyfoods.payment.PayActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.recycleCart)
    RecyclerView recyclerView;
    @BindView(R.id.textViewNothingInCart)
    TextView textViewNothingInCart;
    @BindView(R.id.buttonPurchase)
    Button buttonPurchase;
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @BindView(R.id.textViewContinueShopping)
    TextView textViewContinueShopping;

    List<CartItem> cartItemList = new ArrayList<>();
    private CartAdapter adapter;
    private HomeViewModel vmHome;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        vmHome = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        vmHome.getCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(@Nullable List<CartItem> cartItems) {
                cartItemList.clear();
                if (cartItems == null || cartItems.isEmpty()) {
                    textViewNothingInCart.setVisibility(View.VISIBLE);
                    buttonPurchase.setVisibility(View.GONE);
                    textViewContinueShopping.setVisibility(View.GONE);
                    checkBox.setVisibility(View.GONE);
                    return;
                }
                cartItemList.addAll(cartItems);
                adapter.notifyDataSetChanged();

                float price = 0;
                for (CartItem cartItem : cartItems) {
                    price = price + Float.valueOf(cartItem.getDish().getPrice()) * cartItem.getCount();
                }
                buttonPurchase.setVisibility(View.VISIBLE);
                textViewContinueShopping.setVisibility(View.VISIBLE);

                checkBox.setVisibility(View.VISIBLE);
                buttonPurchase.setText(TextUtils.concat("Pay($", String.valueOf(price), ")"));
                textViewNothingInCart.setVisibility(View.GONE);
            }
        });

        adapter = new CartAdapter(cartItemList, new CartAdapter.CartAdapterListner() {
            @Override
            public void deleteItem(int position) {
//                cartItemList.remove(position);
//                adapter.notifyItemRemoved(position);
//                adapter.notifyItemRangeChanged(position, cartItemList.size());
                List<CartItem> list = vmHome.getCartItems().getValue();
                list.remove(position);
                vmHome.getCartItems().setValue(list);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageView6).setVisibility(View.GONE);
        TextView textViewTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.mycart);
        AHBottomNavigation bottomNav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.restoreBottomNavigation(true);
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        SpannableString string = new SpannableString(getString(R.string.continue_shopping));

        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                mListener.openFoodScreen();
            }
        }, 0, string.length(), 0);

        textViewContinueShopping.setText(string);
        textViewContinueShopping.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @OnClick(R.id.buttonPurchase)
    void pay() {
        if (!checkBox.isChecked()) {
            showToast("Please check the check box.");
            return;
        }
        float price = 0;
        String dishIds[] = new String[cartItemList.size()];
        String counArray[] = new String[cartItemList.size()];
        int index = 0;
        for (CartItem cartItem : cartItemList) {
            price = price + Float.valueOf(cartItem.getDish().getPrice()) * cartItem.getCount();
            dishIds[index] = cartItem.getDish().getId();
            counArray[index] = String.valueOf(cartItem.getCount());
            index++;
        }
        Intent intent = new Intent(getContext(), PayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PayActivity.CART_PRICE, price);
        intent.putExtra(PayActivity.CART_ITEM_IDS, dishIds);
        intent.putExtra(PayActivity.CART_COUNT_ARRAY, counArray);
        startActivity(intent);
    }


}
