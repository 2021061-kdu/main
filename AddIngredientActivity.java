package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddIngredientActivity extends AppCompatActivity {
    private Button btn_fredge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient);

        // btn_fredge 버튼 초기화
        btn_fredge = findViewById(R.id.btn_fredge);

        // btn_fredge 클릭 리스너 설정
        btn_fredge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
