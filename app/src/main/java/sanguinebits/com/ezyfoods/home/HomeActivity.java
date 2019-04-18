package sanguinebits.com.ezyfoods.home;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import base.BaseActivity;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dialogs.LogoutDialog;
import model.CartItem;
import model.user.UserRepository;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.fragments.loginSignup.LoginSignupFragment;
import sanguinebits.com.ezyfoods.pastOrders.PastOrdersActivity;
import sanguinebits.com.ezyfoods.profile.ProfileActivity;
import utils.AppConst;
import utils.AppUserType;
import utils.LocationHelper;
import utils.SharedPrefs;

public class HomeActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, LoginSignupFragment.OnFragmentInteractionListener {

    public static final String CLEAR_DATA = AppConst.PACKAGE + "clearData";
    private HomeViewModel vmHome;
    private boolean mExitOnBackPress;
    private NavController controller;
    private int currentFragmentId;

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        context.startActivity(starter);
    }

    private TextView mTextMessage;
    private AHBottomNavigation bottomNavigation;

    @BindView(R.id.relativeLayout)
    RelativeLayout toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        controller = Navigation.findNavController(this, R.id.fragment);

        setBottomNavigationBar();
        bottomNavigation.setCurrentItem(0);
        mTextMessage = (TextView) findViewById(R.id.message);

        vmHome = ViewModelProviders.of(this).get(HomeViewModel.class);
        vmHome.getCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(@Nullable List<CartItem> cartItems) {
                if (cartItems == null || cartItems.isEmpty()) {
                    return;
                }
            }
        });

        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                currentFragmentId = destination.getId();
            }
        });

        LocationHelper.checkLocationPermission(this, this);
    }

    private void setBottomNavigationBar() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.eat, R.drawable.ic_eat, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.cook, R.drawable.ic_cook, R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.mycart, R.drawable.ic_cart, R.color.colorAccent);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.notifications, R.drawable.ic_more, R.color.colorAccent);


        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#4278D0"));
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));
        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#ffffff"));
        bottomNavigation.setInactiveColor(Color.parseColor("#252422"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setForceTint(false);
        if (SharedPrefs.getInstance(this).getBoolean(AppConst.IS_LOGINED, false)) {
            bottomNavigation.enableItemAtPosition(1);
            bottomNavigation.enableItemAtPosition(2);
            bottomNavigation.enableItemAtPosition(3);
            bottomNavigation.enableItemAtPosition(4);
        } else {
            bottomNavigation.disableItemAtPosition(1);
            bottomNavigation.disableItemAtPosition(2);
            bottomNavigation.disableItemAtPosition(3);
            bottomNavigation.disableItemAtPosition(4);
        }

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position != 2)
                    controller.popBackStack();
                switch (position) {
                    case 0:
                        controller.navigate(R.id.homeFragmentDestination);
                        break;
                    case 1:
                        if (!wasSelected)
                            controller.navigate(R.id.eatFragmentDestintation);
                        break;
                    case 2:
                        if (SharedPrefs.getInstance(getApplicationContext()).getString(UserRepository.KEY_USER_TYPE).equalsIgnoreCase(AppUserType.chef)) {
                            if (!wasSelected) {
                                controller.popBackStack();
                                controller.navigate(R.id.whatYouCookFragmentDestination);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("Become Chef").setMessage("You are currently login as foodie. Do you want to become Chef?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            openPreviousFragment();
                                            dialogInterface.dismiss();
                                            startActivity(new Intent(HomeActivity.this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    openPreviousFragment();
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                        break;
                    case 3:
                        if (!wasSelected) {
                            bottomNavigation.setNotification("", 3);
                            controller.navigate(R.id.cartFragmentDestination);
                        }
                        break;
                    case 4:
                        if (!wasSelected)
                            controller.navigate(R.id.notificationFragmentDestination);
                        break;
                }
                return true;
            }
        });
    }

    private void openPreviousFragment() {
        switch (currentFragmentId) {
            case R.id.homeFragmentDestination:
                bottomNavigation.setCurrentItem(0);
                break;
            case R.id.eatFragmentDestintation:
                bottomNavigation.setCurrentItem(1);
                break;
            case R.id.cartFragmentDestination:
                bottomNavigation.setCurrentItem(3);
                break;
            case R.id.notificationFragmentDestination:
                bottomNavigation.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoginSuccess() {
        SharedPrefs.getInstance(this).setBoolean(AppConst.IS_LOGINED, true);
        bottomNavigation.enableItemAtPosition(1);
        bottomNavigation.enableItemAtPosition(2);
        bottomNavigation.enableItemAtPosition(3);
        bottomNavigation.enableItemAtPosition(4);
    }

    public void back(View view) {
        onBackPressed();
    }

    @OnClick(R.id.imgSideMenu)
    void openSideMenu(View textView) {
        PopupMenu mMenu = new PopupMenu(this, textView);
        mMenu.getMenuInflater().inflate(R.menu.popup_side_menu, mMenu.getMenu());
        mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = null;
                switch (item.getItemId()) {
                    case R.id.logOut:
                        LogoutDialog logoutDialog = new LogoutDialog(new LogoutDialog.AdapterItemClickListner() {
                            @Override
                            public void onClick(int i, String s) {
                                SharedPrefs.getInstance(HomeActivity.this).clear();
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                bottomNavigation.setCurrentItem(0);
                                Objects.requireNonNull(HomeActivity.this).recreate();
                            }
                        });

                        logoutDialog.show(getSupportFragmentManager(), "logOut");
                        break;

                    case R.id.menu_profile:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case R.id.menu_my_orders:
                        PastOrdersActivity.start(HomeActivity.this);
                        break;
                }
                return false;
            }
        });
        mMenu.show();
    }

    @OnClick(R.id.imageViewCart)
    void openCart() {
        controller.popBackStack();
        bottomNavigation.setCurrentItem(3);
    }

    @Override
    public void openFoodScreen() {
        bottomNavigation.setCurrentItem(1);
    }
}
