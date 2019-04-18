package sanguinebits.com.ezyfoods.home.fragments.eat;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.Dish;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeActivity;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import sanguinebits.com.ezyfoods.home.fragments.dishDetail.DishDetailsFragment;
import utils.LocationHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EatFragment extends BaseFragment implements EatView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EatAdapter adapter;
    private EatPresenter presenter;
    private List<Dish> dishes = new ArrayList<>();
    @BindView(R.id.recyclerEat)
    RecyclerView recyclerViewEat;
    @BindView(R.id.textViewError)
    TextView textViewError;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    private HomeViewModel vmHome;

    public EatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EatFragment newInstance(String param1, String param2) {
        EatFragment fragment = new EatFragment();
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
        vmHome.getDishes().observe(this, new Observer<List<Dish>>() {
            @Override
            public void onChanged(@Nullable List<Dish> dishesList) {
                if (dishesList == null) {
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }
                dishes.clear();
                dishes.addAll(dishesList);
                adapter.notifyDataSetChanged();

                if (dishes.isEmpty())
                    textViewError.setVisibility(View.VISIBLE);
                else
                    textViewError.setVisibility(View.GONE);

            }
        });

        presenter = new EatPresenter(getActivity(), this);
        adapter = new EatAdapter(dishes, new EatAdapter.EatAdapterInterface() {
            @Override
            public void onClick(int position) {
                vmHome.selectedDish=dishes.get(position);
                Navigation.findNavController(recyclerViewEat).navigate(R.id.action_open_dish_details);
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayoutContainer, DishDetailsFragment.newInstance(dishes.get(position), ""), "dishDetail").addToBackStack("login").commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageView6).setVisibility(View.GONE);

        AHBottomNavigation bottomNav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
        bottomNav.restoreBottomNavigation(true);
        TextView textViewTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.eat);
        return inflater.inflate(R.layout.fragment_eat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerViewEat.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEat.setAdapter(adapter);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        presenter.getDishes(mySwipeRefreshLayout);
                    }
                }
        );

        if(!LocationHelper.isLocationAvailable(getContext())){
            LocationHelper.checkLocationPermission(getActivity(),(HomeActivity)getActivity());
        }else if (Objects.requireNonNull(vmHome.getDishes().getValue()).isEmpty() || vmHome.isDishesChanged)
            presenter.getDishes(mySwipeRefreshLayout);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDishesLoaded(List<Dish> disheList) {
        vmHome.isDishesChanged = false;
        vmHome.getDishes().postValue(disheList);
    }
}
