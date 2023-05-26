package org.techtown.btm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Home_Gwanganri extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_gwanganri);
        //상단바 이름 변경
        ActionBar ac = getSupportActionBar();
        ac.setTitle("광안리 해수욕장");

    }
}
