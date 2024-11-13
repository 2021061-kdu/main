package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SeafoodFragment extends Fragment {

    public SeafoodFragment() {
        // 기본 생성자
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 해산물 카테고리 Fragment 레이아웃을 반환
        return inflater.inflate(R.layout.add_seafood, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 버튼 ID 배열
        int[] buttonIds = {
                R.id.btn_add_1, R.id.btn_add_2, R.id.btn_add_3, R.id.btn_add_4,
                R.id.btn_add_5, R.id.btn_add_6, R.id.btn_add_7, R.id.btn_add_8,
                R.id.btn_add_9

        };

        // 재료 이름 배열
        String[] itemNames = {
                "고등어", "오징어", "새우", "게","문어", "연어", "조게", "방어", "성게"
        };

        // 재료 이미지 리소스 배열
        int[] itemImages = {
                R.drawable.it_mackerel, R.drawable.it_squid, R.drawable.it_shrimp, R.drawable.it_crab,
                R.drawable.it_octopus, R.drawable.it_salmon, R.drawable.it_clam, R.drawable.it_yellowtail,
                R.drawable.it_urchin

        };

        // 각 버튼에 클릭 리스너 추가
        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;  // 클릭 리스너 안에서 사용하기 위한 인덱스
            View button = view.findViewById(buttonIds[i]);

            // 버튼이 null인지 확인
            if (button == null) {
                Log.e("SeafoodFragment", "Button with ID " + buttonIds[i] + " not found!");
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
