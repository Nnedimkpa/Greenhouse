package com.nnedimkpa.greenhouse;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.nnedimkpa.greenhouse.control.ControlActivity;
import com.nnedimkpa.greenhouse.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /*Code to sign in user into firebase anonymously*/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously();

        //Making the buttons clickable
        binding.beans.setOnClickListener(this);
        binding.pepper.setOnClickListener(this);
        binding.tomato.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.beans:
                startNewActivity(CONST.BEANS);
                break;
            case R.id.tomato:
                startNewActivity(CONST.TOMATO);
                break;
            case R.id.pepper:
                startNewActivity(CONST.PEPPER);
                break;
        }
    }

    private void startNewActivity(int plantData) {
        Intent intent = new Intent(this, ControlActivity.class);
        intent.putExtra("plantData", plantData);
        startActivity(intent);
    }


}
