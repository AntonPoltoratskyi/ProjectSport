package com.polant.projectsport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.SpecificFood;
import com.polant.projectsport.data.model.UserParametersInfo;
import com.polant.projectsport.fragment.CalculateDetailsFoodFragment;
import com.polant.projectsport.fragment.CalculateFoodFragment;
import com.polant.projectsport.fragment.dialog.TodayFoodDialogFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * ������ �������� ������������ ��� ����������� �������� ������������: ��������� ���� �
 * ��������������� ���� ����.
 */
public class ActivityCalculateFood extends AppCompatActivity
        implements CalculateDetailsFoodFragment.FoodCheckListener, TodayFoodDialogFragment.TodayListFoodChangeListener{

    private static final int LAYOUT = R.layout.activity_calculate_food;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Database DB;

    //������ ��� ���������� ����� ������, ��� ��� � ����������� onClick() � AlertDialog ��� ����� ������ ��
    //��������, � ���� ��������� ���� ������� �� final, ���� ��� ��������� ����������.
    private float deltaCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        DB = new Database(this);
        DB.open();

        CalculateFoodFragment fragment = new CalculateFoodFragment();
        //�������� Fragment �����������, ����� � ���� ����������� ��� ����� �������� ������� replace()
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(
                R.id.containerKindsFood,
                fragment,
                getResources().getString(R.string.tag_fragment_calculate_food));
        transaction.commit();

        //�������� ���� ���������� � ������������.
        initInfoWHS();

        initToolbar();
        initNavigationView();
        initButtonChangeYourInfo();
        initButtonShowListTodayFood();

        //������� false - ������, ��� ��������� �� ��� ���������� ����� ����.
        notifyLayoutTextViews(false);
        Log.d("MY_DB_LOGS", "OnCreate");
    }

    //���������� ���������� ��� ��������� ������ �� TodayFoodDialogFragment.
    @Override
    public void changeTodayListFood() {
        //������� false, ��� ��� � �� �������� ����� ������ � ����� ����.
        notifyLayoutTextViews(false);
    }

    //����� �������� �� ������ � CalculateDetailsFoodFragment.
    @Override
    public void changeSelectedCaloriesCount(SpecificFood specificFood, boolean isInserting) {

        if (isInserting) {
            buildAlertDialogAddFood(specificFood);
        }
    }

    //���������� AlertDialog ��� ���������� ���� ��� �������� �������.
    private void buildAlertDialogAddFood(final SpecificFood food) {

        //���������� �������, � ������� ������������ ������ ���������� ��������� ���.
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCalculateFood.this);
        //������� �� id �������, � ������ View, ����� ����� �������� ������ � ����.
        final View alertView = getLayoutInflater().inflate(R.layout.alert_food_add, null);

        builder.setTitle(R.string.alertAddFoodTitle)
                .setMessage(R.string.alertAddFoodMessage)
                .setCancelable(true)
                .setIcon(R.drawable.alert_add_food_icon)
                .setView(alertView)
                .setPositiveButton(getString(R.string.alertAddFoodPositiveBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText text = (EditText) alertView.findViewById(R.id.editTextCountFood);
                        if (!TextUtils.isEmpty(text.getText().toString())) {
                            float count = Float.valueOf(text.getText().toString());
                            deltaCalories = count * food.getCaloriesCount() / 100;
                            Toast.makeText(ActivityCalculateFood.this, "+" + String.valueOf(deltaCalories), Toast.LENGTH_SHORT)
                                    .show();

                            DB.addSpecificFood(food, deltaCalories);
                            //������� true, ��� ��� � �������� ����.
                            notifyLayoutTextViews(true);
                        }
                        else {
                            Toast.makeText(ActivityCalculateFood.this, getString(R.string.toastMistake), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.alertAddFoodNegativeBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //���������� �������� ������� � TextView.
    private void notifyLayoutTextViews(boolean isInserting) {

        TextView countCalText = (TextView) findViewById(R.id.textViewCurrentCalories);

        //���� �������� ����, �� ������ �������� �������� �� TextView � ������� deltaCalories.
        if  (isInserting) {
            int count = Integer.valueOf(countCalText.getText().toString());
            count += deltaCalories;
            countCalText.setText(String.valueOf(count));
        }
        else {
            Cursor c = DB.getTodayFoodStatistics();
            if (c != null && c.moveToFirst()){

                int indexDelta = c.getColumnIndex(Database.DELTA);

                float countTodayCal = 0;
                float temp = 0;

                temp = c.getFloat(indexDelta);
                countTodayCal += temp;

                while (c.moveToNext()){
                    temp = c.getFloat(indexDelta);
                    countTodayCal += temp;
                }
                countCalText.setText(String.valueOf((int)countTodayCal));
                c.close();
            }
            else{
                //������������ �������� TextView ������ 0, ��� ��� ������� ������������ ��� �� �������� ����.
                countCalText.setText(String.valueOf(0));
            }
        }
    }

    //���������� AlertDialog ��� ��������� ���������� ����, �����, ���� � �������� �����.
    private void buildAlertDialogChangeUserInfo(){

        //���������� �������, � ������� ������������ ������ ���������� ��������� ���.
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCalculateFood.this);
        //������� �� id �������, � ������ View, ����� ����� �������� ������ � ����.
        final View alertView = getLayoutInflater().inflate(R.layout.alert_user_info, null);

        builder.setTitle(R.string.alertChangeUserInfoTitle)
                .setMessage(R.string.alertChangeUserInfoMessage)
                .setCancelable(true)
                .setIcon(R.drawable.alert_change_user_parameters_info_icon)
                .setView(alertView)
                .setPositiveButton(getString(R.string.alertChangeUserInfoButtonPositive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText wText = (EditText) alertView.findViewById(R.id.editTextAlertUserWeight);
                        EditText hText = (EditText) alertView.findViewById(R.id.editTextAlertUserHeight);
                        EditText aText = (EditText) alertView.findViewById(R.id.editTextAlertUserAge);
                        RadioGroup group = (RadioGroup) alertView.findViewById(R.id.radioSexGroup);

                        if (!TextUtils.isEmpty(wText.getText().toString()) &&
                                !TextUtils.isEmpty(hText.getText().toString()) &&
                                !TextUtils.isEmpty(aText.getText().toString())) {

                            int idSelectedSex = group.getCheckedRadioButtonId();
                            RadioButton radioBtSex = (RadioButton) alertView.findViewById(idSelectedSex);

                            String userSex = radioBtSex.getText().toString();
                            String userWeight = wText.getText().toString();
                            String userHeight = hText.getText().toString();
                            String userAge = aText.getText().toString();

                            UserParametersInfo user = DB.getUserParametersInfo();
                            user.setSex(userSex);
                            user.setAge(Integer.valueOf(userAge));
                            user.setWeight(Float.valueOf(userWeight));
                            user.setHeight(Float.valueOf(userHeight));

                            //�������� � ����.
                            DB.updateUserParametersInfo(user);
                            //�������� � ���������.
                            ThemeSettings.setUserParametersInfo(user,
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

                            //� ����� ��� �������� ��� ������ TextView.
                            initInfoWHS(user);
                        } else {
                            Toast.makeText(ActivityCalculateFood.this, getString(R.string.toastMistakeUserInfo), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.alertChangeUserInfoButtonNegative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //������������� ������ ������ ����� ������� ��� ��������� ���������� ����, �����, ���� � �������� �����.
    private void initButtonChangeYourInfo() {
        Button bt = (Button) findViewById(R.id.buttonChangeYourHW);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialogChangeUserInfo();
            }
        });
    }
    //������������� ������ ������ ������� �� ������� ����������� ������������� ����.
    private void initButtonShowListTodayFood(){
        Button bt = (Button) findViewById(R.id.buttonShowListTodayFood);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodayFoodDialogFragment fragment = TodayFoodDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), "show_today_food");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_DB_LOGS", "OnDestroy");
        DB.close();
    }

    //�������� Navigation Drawer, ���� �� ������.
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && navigationView != null && drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(navigationView);
        }
        else {
            super.onBackPressed();
        }
    }

    //���������� TextView ���������� ����, �����, ���� � �������� ����� ����� �� ��������� � AlertDialog.
    //� ����� ������� ����� ������� ��� ������������.
    private void initInfoWHS() {
        TextView textWeight = (TextView) findViewById(R.id.textViewYourWeight);
        TextView textHeight = (TextView) findViewById(R.id.textViewYourHeight);
        TextView textSex = (TextView) findViewById(R.id.textViewYourSex);
        TextView textAge = (TextView) findViewById(R.id.textViewYourAge);

        UserParametersInfo user = DB.getUserParametersInfo();

        String w = getString(R.string.text_your_weight) + String.valueOf(user.getWeight());
        String h = getString(R.string.text_your_height) + String.valueOf(user.getHeight());
        String s = getString(R.string.text_your_sex) + String.valueOf(user.getSex());
        String a = getString(R.string.text_your_age) + String.valueOf(user.getAge());

        textWeight.setText(w);
        textHeight.setText(h);
        textSex.setText(s);
        textAge.setText(a);

        TextView normalCaloriesCount = (TextView) findViewById(R.id.textViewMaxCalories);
        int normalCalories = user.normalCaloriesCount(this);
        normalCaloriesCount.setText(String.valueOf(normalCalories).concat(" ").concat(getString(R.string.text_ccal)));
    }
    private void initInfoWHS(UserParametersInfo user){
        TextView textWeight = (TextView) findViewById(R.id.textViewYourWeight);
        TextView textHeight = (TextView) findViewById(R.id.textViewYourHeight);
        TextView textSex = (TextView) findViewById(R.id.textViewYourSex);
        TextView textAge = (TextView) findViewById(R.id.textViewYourAge);

        String w = getString(R.string.text_your_weight) + String.valueOf(user.getWeight());
        String h = getString(R.string.text_your_height) + String.valueOf(user.getHeight());
        String s = getString(R.string.text_your_sex) + user.getSex();
        String a = getString(R.string.text_your_age) + user.getAge();

        textWeight.setText(w);
        textHeight.setText(h);
        textSex.setText(s);
        textAge.setText(a);

        TextView normalCaloriesCount = (TextView) findViewById(R.id.textViewMaxCalories);
        int normalCalories = user.normalCaloriesCount(this);
        normalCaloriesCount.setText(String.valueOf(normalCalories).concat(" ").concat(getString(R.string.text_ccal)));
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.nav_menu_item_calc_food));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        //������ ���� �������� ������ ������, �� �.�. ������� ������ ���� ���, �� �� ��������� ����.
        //toolbar.inflateMenu(R.menu.menu);
    }

    public void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_view_open,
                R.string.navigation_view_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.actionStepCounterItem:
                        Intent stepCounterIntent = new Intent();
                        stepCounterIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
                                ActivityOtherCalculators.ACTION_STEP_COUNTER);
                        setResult(RESULT_OK, stepCounterIntent);
                        finish();
                        break;
                    case R.id.ActionIndexBodyWeight:
                        Intent indexBodyIntent = new Intent();
                        indexBodyIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
                                ActivityOtherCalculators.ACTION_INDEX_BODY);
                        setResult(RESULT_OK, indexBodyIntent);
                        finish();
                        break;
                    case R.id.ActionDayNeedCalories:
                        Intent needCaloriesIntent = new Intent();
                        needCaloriesIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
                                ActivityOtherCalculators.ACTION_NEED_CALORIES);
                        setResult(RESULT_OK, needCaloriesIntent);
                        finish();
                        break;
                    case R.id.actionSettingsItem:
                        //������� ������������� �� ������� �������� ���������.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intentSettings = new Intent(ActivityCalculateFood.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intentSettings, PreferencesNewActivity.SHOW_PREFERENCES);
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateFromPreferences();
    }

    //---------------���������---------------------//
    //���������� �������� ����������.
    private void updateFromPreferences(){
        //�������� ����.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setUpdatedTheme(this, sp);

        //�������� ���������� � ������������.
        DB.updateUserParametersInfo(sp);
        initInfoWHS();
    }
}
