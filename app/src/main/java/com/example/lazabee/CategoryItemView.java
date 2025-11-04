package com.example.lazabee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryItemView extends LinearLayout {

    private ImageView categoryImage1, categoryImage2, categoryImage3, categoryImage4 ;
    private TextView categoryTitle;
    private TextView categoryCount;


    public CategoryItemView(Context context) {
        super(context);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    private void init(Context context) {
        // Nạp layout category_item.xml vào View này
        LayoutInflater.from(context).inflate(R.layout.category_item, this, true);

        // Gắn các view con
        categoryImage1 = findViewById(R.id.categoryImage1);
        categoryImage2 = findViewById(R.id.categoryImage2);
        categoryImage3 = findViewById(R.id.categoryImage3);
        categoryImage4 = findViewById(R.id.categoryImage4);

        categoryTitle = findViewById(R.id.categoryTitle);
        categoryCount = findViewById(R.id.categoryCount);
    }

    // Hàm để set dữ liệu vào component
    public void setCategoryData(int imageRes, String title, int count) {
        categoryImage1.setImageResource(imageRes);
        categoryImage2.setImageResource(imageRes);
        categoryImage3.setImageResource(imageRes);
        categoryImage4.setImageResource(imageRes);

        categoryTitle.setText(title);
        categoryCount.setText(String.valueOf(count));
    }


}
