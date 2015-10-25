package com.polant.projectsport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.polant.projectsport.Constants;
import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.adapter.TabsPagerFragmentAdapter;
import com.polant.projectsport.adapter.TabsStatisticsFragmentAdapter;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Created by ����� on 25.10.2015.
 */
public class StatisticsActivity extends AppCompatActivity {

//    private static final int LAYOUT = R.layout.activity_main;
//
//
//    private DrawerLayout drawerLayout;
//    private Toolbar toolbar;
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
//
//    private Database DB;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
//        super.onCreate(savedInstanceState);
//        setContentView(LAYOUT);
//
//        initToolbar();
//        initNavigationView();
//        initTabArticleLayout();
//
//        DB = new Database(this);
//        DB.open();
//    }
//
//
//    private void initToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        toolbar.setTitle(R.string.app_name);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return false;
//            }
//        });
//    }
//
//    private void initNavigationView() {
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_view_open,
//                R.string.navigation_view_close);
//
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//
//                drawerLayout.closeDrawers();
//
//                switch (item.getItemId()) {
//                    case R.id.actionArticleItem:
//                        showWeekTab();
//                        break;
//                    case R.id.actionStatisticsItem:
//                        initTabChartLayout();
//                        break;
//                    case R.id.actionStepCounterItem:
//                        Intent stepCounterIntent = new Intent();
//                        stepCounterIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
//                                ActivityOtherCalculators.ACTION_STEP_COUNTER);
//                        setResult(RESULT_OK, stepCounterIntent);
//                        finish();
//                        break;
//                    case R.id.ActionIndexBodyWeight:
//                        Intent indexBodyIntent = new Intent();
//                        indexBodyIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
//                                ActivityOtherCalculators.ACTION_INDEX_BODY);
//                        setResult(RESULT_OK, indexBodyIntent);
//                        finish();
//                        break;
//                    case R.id.ActionDayNeedCalories:
//                        Intent needCaloriesIntent = new Intent();
//                        needCaloriesIntent.putExtra(ActivityOtherCalculators.CURRENT_ACTION_STRING,
//                                ActivityOtherCalculators.ACTION_NEED_CALORIES);
//                        setResult(RESULT_OK, needCaloriesIntent);
//                        finish();
//                        break;
//                    case R.id.actionSettingsItem:
//                        //������� ������������� �� ������� �������� ���������.
//                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
//                                PreferencesOldActivity.class : PreferencesNewActivity.class;
//
//                        Intent intent = new Intent(StatisticsActivity.this, c);
//                        Log.d("Class in intent", c.getName());
//                        startActivityForResult(intent, PreferencesNewActivity.SHOW_PREFERENCES);
//                        break;
//                }
//
//                return true;
//            }
//        });
//    }
//
//    //������.
//    public void initTabArticleLayout() {
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//
//        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    //���������� � �����������.
//    public void initTabChartLayout() {
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//
//        TabsStatisticsFragmentAdapter adapter = new TabsStatisticsFragmentAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    //�������� ���� "������" �� NavigationView.
//    private void showWeekTab() {
//        viewPager.setCurrentItem(Constants.TAB_ONE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //�� �������� ��� ����� if, ������ ��� �������� �������� ���������� ����� ��������� �
//        //����� ������ �������� - � ����� requestCode �� ����� ����� PreferencesNewActivity.SHOW_PREFERENCES;
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        updateFromPreferences(sp);
//    }
//
//    //���������� �������� ����������.
//    private void updateFromPreferences(SharedPreferences sp){
//        //�������� ����.
//        ThemeSettings.setUpdatedTheme(this, sp);
//
//        //�������� ���������� � ������������.
//        DB.updateUserParametersInfo(sp);
//    }

}
