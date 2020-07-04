package com.zandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zandroid.R;

import java.math.BigDecimal;

import com.zandroid.tools.DensityUtils;
/**
 * 只适用于展示的评价View
 * @author zpp 2018.9.11
* */
public class CustomStartView extends LinearLayout {
    private Context context;

    private int start_number;//总星星
    private float rating;//显示的星星
    private int start_size;
    private int full_image;
    private int haif_image;
    private int no_image;
    private int orientation;
    private int start_margin;


    public CustomStartView(Context context) {
        super(context);
        this.context=context;
        setDault();
        setStar(rating);
    }

    public CustomStartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setDault();

        final TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.custom_start_view);

        start_number=attrsArray.getInt(R.styleable.custom_start_view_start_number,start_number);
        rating=attrsArray.getFloat(R.styleable.custom_start_view_rating,rating);
        start_size= (int) attrsArray.getDimension(R.styleable.custom_start_view_start_size,start_size);
        start_margin= (int) attrsArray.getDimension(R.styleable.custom_start_view_start_margin,start_margin);
        full_image=attrsArray.getInt(R.styleable.custom_start_view_full_image,full_image);
        haif_image=attrsArray.getInt(R.styleable.custom_start_view_haif_image,haif_image);
        no_image=attrsArray.getInt(R.styleable.custom_start_view_no_image,no_image);
        orientation=attrsArray.getInt(R.styleable.custom_start_view_orientation,orientation);
        setStar(rating);
    }
    private void setDault(){
        start_number=5;
        rating=3.5f;
        start_size=DensityUtils.dip2px(context,16);
        full_image=R.drawable.stars_complete;
        haif_image=R.drawable.stars_half;
        no_image=R.drawable.stars_no;
        start_margin=10;
        orientation=0;
    }
    /**
     * 设置星星的个数
     *
     * @param rating
     */
    public void setStar(float rating) {
        this.removeAllViews();

        //浮点数的整数部分
        int fint = (int) rating;
        BigDecimal b1 = new BigDecimal(Float.toString(rating));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();

        //设置选中的星星
        for (int i = 0; i < fint; ++i) {
            ImageView view=getFullImageView(0);
            this.addView(view);
        }
        //小数点默认增加半颗星
        if (fPoint > 0) {
            ImageView view=getFullImageView(1);
            this.addView(view);
        }
        for(int j=0;j<(int) (start_number-rating);j++){
            ImageView view=getFullImageView(2);
            this.addView(view);
        }
    }
    private ImageView getFullImageView(int isfull) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );//设置每颗星星在线性布局的大小

        if(orientation==0) {
            layout.setMargins(0, 0, start_margin, 0);//设置每颗星星在线性布局的间距
        } else {
            layout.setMargins(0, 0, 0, start_margin);//设置每颗星星在线性布局的间距
        }
        layout.width = start_size;
        layout.height = start_size;
        imageView.setLayoutParams(layout);
        imageView.setAdjustViewBounds(true);
        switch (isfull){
            case 0:
                imageView.setImageDrawable(this.getResources().getDrawable(full_image));
                break;
            case 1:
                imageView.setImageDrawable(this.getResources().getDrawable(haif_image));
                break;
                default:
                    imageView.setImageDrawable(this.getResources().getDrawable(no_image));
        }

        imageView.setMinimumWidth(start_size);
        imageView.setMaxHeight(start_size);

        return imageView;
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
