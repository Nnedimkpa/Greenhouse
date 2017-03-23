package com.nnedimkpa.greenhouse.control;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.nnedimkpa.greenhouse.R;
import com.nnedimkpa.greenhouse.control.chart.ChartFragment;
import com.nnedimkpa.greenhouse.control.manual.ManualFragment;
import com.nnedimkpa.greenhouse.databinding.ActivityControlBinding;

public class ControlActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityControlBinding binding;
    private Fragment chartFragment, controlFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_control);

        int mode = getIntent().getIntExtra("mode", 99);
        int plantData = getIntent().getIntExtra("plantData", 99);
        fragmentManager = getSupportFragmentManager();
        switchFragment(getControlFragment(plantData));
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        navigationView.setOnNavigationItemSelectedListener(this);
    }


    private void switchFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();
    }

    private Fragment getChartFragment(int plantData) {
        if (chartFragment == null) chartFragment = ChartFragment.newInstance(plantData);
        return chartFragment;
    }

    private Fragment getControlFragment(int plantData) {
        if (controlFragment == null) controlFragment = ManualFragment.newInstance(plantData);
        return controlFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_charts:
                switchFragment(getChartFragment(0));
                break;
            case R.id.nav_control:
                switchFragment(getControlFragment(0));
                break;
        }
        return true;
    }
}
