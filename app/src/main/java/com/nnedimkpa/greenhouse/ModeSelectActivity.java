package com.nnedimkpa.greenhouse;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.nnedimkpa.greenhouse.control.ControlActivity;
import com.nnedimkpa.greenhouse.databinding.ActivityModeSelectBinding;

public class ModeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityModeSelectBinding binding;
    private int plantData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mode_select);
        binding.auto.setOnClickListener(this);
        binding.manual.setOnClickListener(this);
        plantData = getIntent().getIntExtra("plantData", 99);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auto:
                startNewActivity(CONST.AUTO);
                break;
            case R.id.manual:
                startNewActivity(CONST.MANUAL);
                break;
        }
    }

    private void startNewActivity(int mode){
        Intent intent =  new Intent(this, ControlActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("plantData", plantData);
        startActivity(intent);
    }
}
