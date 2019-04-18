package sanguinebits.com.ezyfoods.pastOrders;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.Order;
import model.user.User;
import model.user.UserRepository;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.pastOrderDetail.PastOrderDetailsActivity;
import sanguinebits.com.ezyfoods.review.ReviewActivity;
import utils.SharedPrefs;

public class PastOrdersActivity extends BaseActivity implements PastOrdersView {
    private PastOrderAdapter adapter;
    private PastOrdersPresenter presenter;
    private VMPastOrder vmPastOrder;
    private List<Order> orderList = new ArrayList<>();

    public static void start(Context context) {
        Intent starter = new Intent(context, PastOrdersActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.recyclerPastOrders)
    RecyclerView recyclerViewOrders;
    @BindView(R.id.textViewNothingInCart)
    TextView textViewNothingInCart;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasr_orders);
        ButterKnife.bind(this);
        vmPastOrder = ViewModelProviders.of(this).get(VMPastOrder.class);
        vmPastOrder.getOrderList().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orders) {
                if (orders == null)
                    return;

                orderList.clear();
                orderList.addAll(orders);
                adapter.notifyDataSetChanged();

                if (orderList.isEmpty()) {
                    textViewNothingInCart.setVisibility(View.VISIBLE);
                } else {
                    textViewNothingInCart.setVisibility(View.GONE);
                }
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        presenter.getPastOrders(mySwipeRefreshLayout);
                    }
                }
        );

        presenter = new PastOrdersPresenter(this, this);
        adapter = new PastOrderAdapter(orderList, new PastOrderAdapter.PastOrderAdapterListner() {
            @Override
            public void reviewItem(int position) {
                ReviewActivity.start(PastOrdersActivity.this, orderList.get(position).getDishId());
            }

            @Override
            public void openDetails(int position) {
                PastOrderDetailsActivity.start(PastOrdersActivity.this,orderList.get(position));
            }
        }, SharedPrefs.getInstance(getApplicationContext()).getString(UserRepository.KEY_USER_ID));

        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(adapter);
        if (orderList.isEmpty())
            presenter.getPastOrders(mySwipeRefreshLayout);
    }

    @OnClick(R.id.imageView6)
    void back() {
        onBackPressed();
    }

    @Override
    public void onOrdersLoaded(List<Order> orders) {
        vmPastOrder.getOrderList().postValue(orders);
    }
}
