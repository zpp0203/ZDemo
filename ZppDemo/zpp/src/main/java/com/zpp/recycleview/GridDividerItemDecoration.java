package com.zpp.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * Gridview
 * item分割线的ItemDecoration,
 *
 * 采用系统默认的分割线
 * 可在AppTheme中设置<item name="android:listDivider">@drawable/divider_bg</item>
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider };
    private Drawable mDivider;
    private final Rect mBounds = new Rect();

    public GridDividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public void setDrawable(Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawDivider(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
    }

    private void drawDivider(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int childCount = parent.getChildCount();
        //int spanCount=getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {
            final View view = parent.getChildAt(i);

            //isLastRaw(parent,i,spanCount,childCount);

            parent.getDecoratedBoundsWithMargins(view, mBounds);
            //left 垂线
            mDivider.setBounds(mBounds.left, mBounds.top, mDivider.getIntrinsicHeight(), mBounds.bottom);
            mDivider.draw(canvas);
            //right 垂线
            mDivider.setBounds(mBounds.right - mDivider.getIntrinsicWidth(), mBounds.top, mBounds.right, mBounds.bottom);
            mDivider.draw(canvas);
            //top 横线
            mDivider.setBounds(mBounds.left, mBounds.top, mBounds.right , mDivider.getIntrinsicHeight());
            mDivider.draw(canvas);
            //bottom 横线
            mDivider.setBounds(mBounds.left, mBounds.bottom - mDivider.getIntrinsicHeight(), mBounds.right , mBounds.bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private int getSpanCount(RecyclerView parent)
    {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,int childCount){

        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }
}