package com.example.jahirul.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SlidingActivity extends ActionBarActivity {

    // Declaring Your View and Variables


    ViewPager pager;
    ViewPagerAdapter adapter;
    CharSequence Titles[]={"Home","Events","Test"};
    int Numboftabs =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_activity);




        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (VerticalViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


    }
}