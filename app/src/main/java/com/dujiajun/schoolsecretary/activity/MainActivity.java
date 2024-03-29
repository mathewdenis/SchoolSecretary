package com.dujiajun.schoolsecretary.activity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dujiajun.schoolsecretary.fragment.ChouFragment;
import com.dujiajun.schoolsecretary.fragment.DianFragment;
import com.dujiajun.schoolsecretary.adapter.MyPagerAdapter;
import com.dujiajun.schoolsecretary.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyPagerAdapter pagerAdapter;
    private NavigationView navigationView;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private TextView text_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIInit();


        localBroadcastManager= LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.dujiajun.dbstools.CHANGE_USERNAME_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.menu_drawer1: {
                    break;
                }
                case R.id.menu_drawer2: {
                    Intent intent = new Intent(MainActivity.this, ManageActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.menu_drawer3: {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.menu_drawer4: {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
                }

            }
            drawerLayout.closeDrawers();
            return false;
    }

    protected void UIInit() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.draw_open, R.string.draw_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.main_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ArrayList<Fragment> fragments = new ArrayList<>();
        //MainFragment fragment1 = new MainFragment();
        ChouFragment fragment2 = new ChouFragment();
        DianFragment fragment3 = new DianFragment();
        //fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        //String[] strings = {"首页", "抽签", "点名"};
        ArrayList<String> strings = new ArrayList<>();
        strings.add("抽签");
        strings.add("点名");
        pagerAdapter = new MyPagerAdapter(getFragmentManager(), fragments, strings);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        text_username = (TextView) findViewById(R.id.drawer_username);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        text_username.setText(preferences.getString("username","小秘用户"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "校园小秘Android版欢迎您的使用。\n开发者微博：http://weibo.com/u/2789358903");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"分享"));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            text_username.setText(intent.getStringExtra("username"));

        }
    }
}
