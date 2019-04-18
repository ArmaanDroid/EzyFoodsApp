package sanguinebits.com.ezyfoods.home.fragments.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Objects;

import androidx.navigation.Navigation;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dialogs.LogoutDialog;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import utils.AppConst;
import utils.SharedPrefs;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeViewModel vmHome;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        vmHome= ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HomeViewModel.class);

    }

    @BindView(R.id.frameViewFlipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.btnLoginHome)
    Button btnLoginHome;

    @BindView(R.id.btnSignupHome)
    Button btnSignupHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imgSideMenu).setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(2000);
        mViewFlipper.startFlipping();

        if (SharedPrefs.getInstance(getContext()).getBoolean(AppConst.IS_LOGINED, false)) {
            btnLoginHome.setVisibility(View.GONE);
            btnLoginHome.setText(R.string.logout);
            btnSignupHome.setVisibility(View.GONE);
        } else {
            btnLoginHome.setVisibility(View.VISIBLE);
            btnSignupHome.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnSignupHome)
    void openSignup(View view) {
        vmHome.isLogin=false;
        Navigation.findNavController(view).navigate(R.id.action_login_signup);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.frameLayoutContainer, LoginSignupFragment.newInstance(false, ""), "signup").addToBackStack("login").commit();
    }

    @OnClick(R.id.btnLoginHome)
    void openLogin(View view) {
        if (SharedPrefs.getInstance(getContext()).getBoolean(AppConst.IS_LOGINED, false)) {
            LogoutDialog logoutDialog = new LogoutDialog(new LogoutDialog.AdapterItemClickListner() {
                @Override
                public void onClick(int i, String s) {
                    SharedPrefs.getInstance(getContext()).clear();
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Objects.requireNonNull(getActivity()).recreate();
                }
            });

            if (getFragmentManager() != null) logoutDialog.show(getFragmentManager(), "logOut");
        } else {

            vmHome.isLogin=true;
            Navigation.findNavController(view).navigate(R.id.action_login_signup);

//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.frameLayoutContainer, LoginSignupFragment.newInstance(true, ""), "login").addToBackStack("login").commit();
        }
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
