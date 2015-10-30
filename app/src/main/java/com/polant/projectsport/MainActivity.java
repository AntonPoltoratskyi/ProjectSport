package com.polant.projectsport;

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

import com.polant.projectsport.activity.ActivityOtherCalculators;
import com.polant.projectsport.adapter.TabsPagerFragmentAdapter;
import com.polant.projectsport.adapter.TabsStatisticsFragmentAdapter;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Данная Активити используется для отображения всех статей согласно их категориям,
 * а также статистики потребленной пользователем пищи в виде графиков.
 */
public class MainActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_main;

    private static final int VIEW_PAGER_CONTENT_ARTICLE = 1;
    private static final int VIEW_PAGER_CONTENT_STATISTICS = 2;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Database DB;

    //Переменная, которая хранит текущее состояние viewPager - статьи или статистика.
    private int viewPagerContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        initTabArticleLayout();

        DB = new Database(this);
        DB.open();
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //указываю title, когда инициализирую tab layout;

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_view_open,
                R.string.navigation_view_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.actionArticleItem:
                        initTabArticleLayout();
                        showFoodTab();
                        break;
                    case R.id.actionStatisticsItem:
                        initTabStatisticsLayout();
                        break;
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
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intent = new Intent(MainActivity.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intent, PreferencesNewActivity.SHOW_PREFERENCES);
                        break;
                }

                return true;
            }
        });
    }

    //Статьи.
    public void initTabArticleLayout() {
        toolbar.setTitle(R.string.title_articles);
        viewPagerContent = VIEW_PAGER_CONTENT_ARTICLE;

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(
                this,
                getSupportFragmentManager()
        );
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //Статистика.
    public void initTabStatisticsLayout() {
        toolbar.setTitle(R.string.title_statistics);
        viewPagerContent = VIEW_PAGER_CONTENT_STATISTICS;

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabsStatisticsFragmentAdapter adapter = new TabsStatisticsFragmentAdapter(
                this,
                getSupportFragmentManager()
        );
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //Открытие таба "Статьи" из NavigationView.
    private void showFoodTab() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_DB_LOGS", "OnDestroyMainActivity");
        //Закрываю базу.
        DB.close();
    }

    //Получение БД во фрагментах.
    public Database getDatabase(){
        return DB;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Не проверяю это через if, потому что Активити настроей приложения может вызваться в
        //любой другой Активити - и тогда requestCode не будет равен PreferencesNewActivity.SHOW_PREFERENCES;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        updateFromPreferences(sp);
    }

    //Применение настроек приложения.
    private void updateFromPreferences(SharedPreferences sp){
        //Применяю тему.
        ThemeSettings.setUpdatedTheme(this, sp);

        //Обновляю информация о пользователе.
        DB.updateUserParametersInfo(sp);

        if (viewPagerContent == VIEW_PAGER_CONTENT_STATISTICS) {
            //Обновление графиков, после применения настроек.
            initTabStatisticsLayout();
        }
    }

}
