package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 修改源码使得 x轴坐标换行 ，下面属性自己可以调整
 */
public class CustomXAxisRenderer extends XAxisRenderer {
    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        float labelHeight = mXAxis.getTextSize();
        float labelInterval = 5f;
        String[] labels = formattedLabel.split("\n");
        for(int i=0;i<labels.length;i++){
            Utils.drawXAxisValue(c, labels[i], x, y+(labelHeight + labelInterval)*i, mAxisLabelPaint, anchor, angleDegrees);
        }

    }
}
