package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
    private Button btnEdit, btnDelete, btnPlus, btnSetting, btnRecommend;
    private TextView tvTitle;
    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewIngredientsFreezer;
    private RecyclerView recyclerViewRecipes;
    private IngredientAdapter ingredientAdapter;
    private IngredientAdapter freezerAdapter;
    private RecipeAdapter recipeAdapter; // 레시피 어댑터 추가
    private boolean isDeleteMode = false;  // 삭제 모드 상태
    private List<Ingredient> fridgeIngredients = new ArrayList<>();
    private List<Ingredient> freezerIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Android 13 이상에서 알림 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }

        // 레이아웃 요소 초기화
        tvTitle = findViewById(R.id.tv_title);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnPlus = findViewById(R.id.btn_plus);
        btnSetting = findViewById(R.id.btn_setting);
        btnRecommend = findViewById(R.id.btn_recommend); // 추천 버튼
        recyclerViewIngredients = findViewById(R.id.recycler_view_ingredients_fridge);
        recyclerViewIngredientsFreezer = findViewById(R.id.recycler_view_ingredients_freezer);
        recyclerViewRecipes = findViewById(R.id.recycler_view_recipes); // 레시피 RecyclerView

        // RecyclerView 레이아웃 매니저 설정
        recyclerViewIngredients.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewIngredients.addItemDecoration(new GridSpacingItemDecoration(4, 8, true));

        recyclerViewIngredientsFreezer.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewIngredientsFreezer.addItemDecoration(new GridSpacingItemDecoration(4, 8, true));

        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this)); // 세로형 레이아웃 매니저

        // 재료 리스트 불러오기
        loadIngredients();

        // 삭제 버튼 클릭 리스너 설정
        btnDelete.setOnClickListener(v -> {
            isDeleteMode = !isDeleteMode;
            ingredientAdapter.setDeleteMode(isDeleteMode);
            freezerAdapter.setDeleteMode(isDeleteMode);
        });

        // 설정 버튼 클릭 리스너
        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        // 추가 버튼 클릭 리스너 설정
        btnPlus.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });

        // 레시피 추천 버튼 클릭 리스너
        btnRecommend.setOnClickListener(v -> recommendRecipes());
    }

    private void recommendRecipes() {
        // 추천 알고리즘: 단순히 이름으로 레시피 생성 (예시)
        List<Recipe> recommendedRecipes = new ArrayList<>();

        if (fridgeIngredients.isEmpty() && freezerIngredients.isEmpty()) {
            Toast.makeText(this, "추천할 재료가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hasIngredients("김치", "밥")) {
            recommendedRecipes.add(new Recipe("김치볶음밥", Arrays.asList("김치", "밥")));
        }
        if (hasIngredients("계란", "파")) {
            recommendedRecipes.add(new Recipe("계란말이", Arrays.asList("계란", "파")));
        }

        if (recommendedRecipes.isEmpty()) {
            Toast.makeText(this, "추천 가능한 레시피가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            recipeAdapter = new RecipeAdapter(recommendedRecipes);
            recyclerViewRecipes.setAdapter(recipeAdapter);
        }
    }

    private boolean hasIngredients(String... ingredients) {
        List<String> allIngredients = new ArrayList<>();
        for (Ingredient ingredient : fridgeIngredients) {
            allIngredients.add(ingredient.getName());
        }
        for (Ingredient ingredient : freezerIngredients) {
            allIngredients.add(ingredient.getName());
        }

        for (String required : ingredients) {
            if (!allIngredients.contains(required)) {
                return false;
            }
        }
        return true;
    }

    private void loadIngredients() {
        ApiRequest apiRequest = new ApiRequest(this);
        apiRequest.fetchIngredients(new ApiRequest.IngredientFetchListener() {
            @Override
            public void onFetchSuccess(List<Ingredient> ingredients) {
                fridgeIngredients.clear();
                freezerIngredients.clear();

                for (Ingredient ingredient : ingredients) {
                    if ("냉동".equals(ingredient.getStorageLocation())) {
                        freezerIngredients.add(ingredient);
                    } else {
                        fridgeIngredients.add(ingredient);
                    }
                }

                ingredientAdapter = new IngredientAdapter(fridgeIngredients, HomeActivity.this);
                recyclerViewIngredients.setAdapter(ingredientAdapter);

                freezerAdapter = new IngredientAdapter(freezerIngredients, HomeActivity.this);
                recyclerViewIngredientsFreezer.setAdapter(freezerAdapter);

                ingredientAdapter.notifyDataSetChanged();
                freezerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchError(VolleyError error) {
                Toast.makeText(HomeActivity.this, "재료를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;

                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
