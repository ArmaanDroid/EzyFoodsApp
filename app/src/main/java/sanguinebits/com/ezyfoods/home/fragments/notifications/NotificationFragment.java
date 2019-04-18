package sanguinebits.com.ezyfoods.home.fragments.notifications;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import model.Notification;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import sanguinebits.com.ezyfoods.orderDetailsNotification.OrderDetailsNotificationActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends BaseFragment implements NotificationView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.recycleNotifications)
    RecyclerView recycleNotifications;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.textViewError)
    TextView textViewError;

    private OnFragmentInteractionListener mListener;
    private NotificationPresenter presenter;
    private NotificationAdapter adapter;
    private HomeViewModel vmHome;
    List<Notification> notificationList = new ArrayList<>();

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        adapter = new NotificationAdapter(notificationList, new NotificationAdapter.AdapterCallBack() {
            @Override
            public void openDishDetails(int adapterPosition) {
                OrderDetailsNotificationActivity.start(getContext(),notificationList.get(adapterPosition).getOrder());
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
        TextView textViewTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.notifications);

        AHBottomNavigation bottomNav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recycleNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleNotifications.setAdapter(adapter);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        presenter.getNotificaitons(mySwipeRefreshLayout);
                    }
                }
        );
        vmHome = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        vmHome.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications) {
                if (notifications == null) {
                    textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                notificationList.clear();
                notificationList.addAll(notifications);
                adapter.notifyDataSetChanged();
                if(notificationList.isEmpty()){
                    textViewError.setVisibility(View.VISIBLE);
                }else{
                    textViewError.setVisibility(View.GONE);

                }
            }
        });
        presenter = new NotificationPresenter(getActivity(), this);

        if (Objects.requireNonNull(vmHome.getNotifications().getValue()).isEmpty())
            presenter.getNotificaitons(mySwipeRefreshLayout);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNotificationsLoaded(List<Notification> notifications) {
        vmHome.getNotifications().setValue(notifications);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
