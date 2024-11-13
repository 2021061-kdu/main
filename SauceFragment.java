package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SauceFragment extends Fragment {

    public SauceFragment() {
        // 기본 생성자
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // add_sauce.xml 레이아웃을 Fragment로 반환
        return inflater.inflate(R.layout.add_sauce, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 버튼 ID 배열 (소스 추가 버튼)
        int[] buttonIds = {
                R.id.btn_add_1, R.id.btn_add_2, R.id.btn_add_3, R.id.btn_add_4,
                R.id.btn_add_5, R.id.btn_add_6, R.id.btn_add_7, R.id.btn_add_8,
                R.id.btn_add_9, R.id.btn_add_10, R.id.btn_add_11, R.id.btn_add_12
        };

        // 재료 이름 배열 (소스 종류)
        String[] itemNames = {
                "케첩", "마요네즈", "머스타드", "바베큐 소스", "핫소스", "간장", "참기름", "소금", "설탕", "고추장", "굴소스", "고춧가루"
        };

        // 재료 이미지 리소스 배열 (소스 이미지)
        int[] itemImages = {
                R.drawable.it_ketchup, R.drawable.it_mayonnaise, R.drawable.it_mustard,R.drawable.it_bbq,
                 R.drawable.it_hot, R.drawable.it_soysauce, R.drawable.it_sesameoil,R.drawable.it_salt,
                 R.drawable.it_sugar, R.drawable.it_gochujang, R.drawable.it_oyster, R.drawable.it_redpepperpowder
        };

        // 각 버튼에 클릭 리스너 추가
        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;  // 클릭 리스너 안에서 사용하기 위한 인덱스
            ImageButton button = view.findViewById(buttonIds[i]);

            // 버튼이 null인지 확인
            if (button == null) {
                Log.e("SauceFragment", "Button with ID " + buttonIds[i] + " not found!");
                continue;  // 버튼을 찾을 수 없으면 다음 버튼으로 넘어감
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // AddDetailActivity로 이동
                    Intent intent = new Intent(getActivity(), AddDetailActivity.class);
                    intent.putExtra("itemName", itemNames[index]); // 재료 이름
                    intent.putExtra("itemImage", itemImages[index]); // 재료 이미지 리소스 ID
                    startActivity(intent);
                }
            });
        }
    }
}
