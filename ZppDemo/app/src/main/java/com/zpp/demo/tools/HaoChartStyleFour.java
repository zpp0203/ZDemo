package com.zpp.demo.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineCircleChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartCircleRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zpp.demo.R;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.data.LineDataSet.Mode.CUBIC_BEZIER;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * @author hao
 * @date 2017/9/11
 * @description 影厅人群画像折线图
 */

public class HaoChartStyleFour {

    static int gray =0xFF333333;
    public static void setLinesChart(final Context context, final LineCircleChart lineChart, final List<String> xAxisValue, List<List<Float>> yXAxisValues, int[] lineColors, final ImageView redPosition) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        lineChart.setDrawGridBackground(true); //表格颜色

        lineChart.setGridBackgroundColor(Color.TRANSPARENT); //表格的颜色，设置一个透明度
        lineChart.setTouchEnabled(true); //可点击
        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(false);  //可缩放
        lineChart.setPinchZoom(true);
        lineChart.setData(generateLineData(context, yXAxisValues, lineColors));  //填充数据
        //        lineChart.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        lineChart.setNoDataText("");//设置无数据文字
        lineChart.setDescription(null);
        //
        lineChart.getAxisLeft().setEnabled(true); // 显示左边 的坐标轴
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setEnabled(true); // 显示x轴
        //
        //        LineChartMarkVeiw markVeiw = new LineChartMarkVeiw(context, R.layout.custom_marker_view);
        //        markVeiw.setChartView(lineChart);
        //        lineChart.setMarker(markVeiw);

        //x坐标轴设置
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setAxisLineWidth(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);//设置标签居中
        xAxis.setLabelCount(xAxisValue.size());
        //        xAxis.setTextSize(10);
        xAxis.setAxisLineColor(context.getResources().getColor(R.color.green));
        xAxis.setTextColor(context.getResources().getColor(R.color.green));
        xAxis.setGridColor(gray);
        xAxis.setGridLineWidth(1);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                int position = (int) v;
                if (position < xAxisValue.size() && position >= 0) {
                    return xAxisValue.get(position);
                }
                return "" + v;
            }
        });
        //设置y轴数据偏移量(x轴显示文字距离x轴的距离)
        //        xAxis.setYOffset(10);
        xAxis.setAvoidFirstLastClipping(true);
        //设置x轴值距离边界0.5倍间距
        xAxis.setSpaceMin(0.3f);
        xAxis.setSpaceMax(0.3f);


        if (yXAxisValues.size() == 1 || yXAxisValues.size() == 3) {
            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            //显示网格线
            leftAxis.setDrawGridLines(true);
            //不显示坐标线
            leftAxis.setDrawAxisLine(false);
            //把轴的标签放在原来的位置，而不是把它们画在原来的位置(true 不根据y轴值自适应值,而是把y轴长度占满,把能显示的值都放上边)
            //                        leftAxis.setCenterAxisLabels(true);
            leftAxis.setLabelCount(2); //显示格数
            leftAxis.setMinWidth(3f);
            leftAxis.setAxisLineColor(context.getResources().getColor(R.color.grey));
            leftAxis.setTextColor(context.getResources().getColor(R.color.grey));
            leftAxis.setGridColor(gray);
            xAxis.setGridLineWidth(1);
            leftAxis.setAxisMinimum(0);
            leftAxis.setTextSize(10);
            leftAxis.setSpaceMin(2f);
            leftAxis.setSpaceMax(2f);
            leftAxis.setGridLineWidth(1);
            leftAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return value + "%";
                }
            });

            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawLabels(false);
        } else if (yXAxisValues.size() == 2) {
            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setDrawGridLines(true);
            //不显示坐标线
            leftAxis.setDrawAxisLine(false);
            //
            leftAxis.setLabelCount(2); //显示格数
            leftAxis.setMinWidth(3f);
            leftAxis.setGridLineWidth(1);
            leftAxis.setAxisLineColor(context.getResources().getColor(R.color.grey));
            leftAxis.setTextColor(context.getResources().getColor(R.color.grey));
            leftAxis.setAxisMinimum(0);
            leftAxis.setTextSize(10);
            leftAxis.setSpaceMin(2f);
            leftAxis.setSpaceMax(2f);
            leftAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return value + "%";
                }
            });
            //
            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawLabels(false);
        }

        //图例设置
        Legend legend = lineChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setEnabled(false);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTextSize(12f);

        lineChart.animateX(500);//数据显示动画，从左往右依次显示


        LineChartCircleRenderer.setOnDrawEndListenr(new LineChartCircleRenderer.OnDrawEndListenr() {
            @Override
            public void drawEnd(String lable,int position,float x,float y) {

                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) redPosition.getLayoutParams();
                params.topMargin= (int) (lineChart.getTop()+y-params.height/2);
                params.leftMargin= (int) (lineChart.getLeft()+x-params.width/2);
                redPosition.setLayoutParams(params);

                AnimationSet animationSet =new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f,0.5f,1,0.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setRepeatCount(Animation.INFINITE);
                scaleAnimation.setRepeatMode(Animation.REVERSE);
                scaleAnimation.setDuration(1000);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1,0.4f);
                alphaAnimation.setRepeatCount(Animation.INFINITE);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setRepeatMode(Animation.REVERSE);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                redPosition.startAnimation(animationSet);
            }
        });
    }

    public static final int[] LINE_FILL_COLORS = {
            0xFF6DC4CE, Color.rgb(246, 234, 208), Color.rgb(235, 228, 248)
    };
    public static final int[] LINE_COLORS = {
            Color.rgb(140, 210, 118), Color.rgb(159, 143, 186), Color.rgb(233, 197, 23)
    };//绿色，紫色，黄色

    private static int main_blue = 0xFF6DC4CE;

    private static LineData generateLineData(Context context, List<List<Float>> lineValues, int[] lineColors) {
        List<List<Entry>> entriesList = new ArrayList<>();
        if (lineValues != null) {
            for (int i = 0; i < lineValues.size(); ++i) {
                ArrayList<Entry> entries = new ArrayList<>();
                for (int j = 0, n = lineValues.get(i).size(); j < n; j++) {
                    Entry entry = new Entry(j, lineValues.get(i).get(j));
                    entry.setData("" + lineValues.get(i).get(j));
                    entries.add(entry);
                }
                entriesList.add(entries);
            }
        }
        return generateLineData(context, entriesList, lineColors, 0);
    }

    private static LineData generateLineData(Context context, List<List<Entry>> entriesList, int[] lineColors, int what) {
        List<ILineDataSet> dataSets = new ArrayList<>();

        if (entriesList != null) {
            for (int i = 0; i < entriesList.size(); ++i) {
                LineDataSet lineDataSet = new LineDataSet(entriesList.get(i), "");
                lineDataSet.setValues(entriesList.get(i));

                if (lineColors != null) {
                    lineDataSet.setColor(context.getResources().getColor(lineColors[i]));
                    lineDataSet.setCircleColor(context.getResources().getColor(lineColors[i]));
                    lineDataSet.setCircleColorHole(Color.WHITE);
                    lineDataSet.setDrawFilled(false);
                } else {
                    lineDataSet.setColor(LINE_FILL_COLORS[i % 3]);
                    lineDataSet.setCircleColor(LINE_COLORS[i % 3]);
                    lineDataSet.setCircleColorHole(Color.WHITE);
                    lineDataSet.setDrawFilled(false);

                }

                lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);//设置线数据依赖于左侧y轴

                lineDataSet.setHighlightEnabled(false);
                lineDataSet.setHighLightColor(LINE_FILL_COLORS[i % 3]); // 高亮的线的颜色
                lineDataSet.setValueTextColor(main_blue); //数值显示的颜色

                //绘制x轴对应的y轴数据值的圆点
                lineDataSet.setDrawCircles(true);
                //绘制的圆点是否是空心
                lineDataSet.setDrawCircleHole(false);
                //圆点的填充颜色
                lineDataSet.setCircleColor(Color.BLACK);
                //lineDataSet.setCircleColors(LINE_COLORS);

                //圆点的半径
                lineDataSet.setCircleRadius(5f);


                lineDataSet.setLineWidth(1f);//设置线的宽度
                lineDataSet.setDrawValues(false);//不绘制线的数据

                lineDataSet.setMode(CUBIC_BEZIER);
                //阴影
                lineDataSet.setDrawShadow(false);
                lineDataSet.setShadowColor(Color.RED);
                lineDataSet.setDrawFilled(true);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_t_bg);
                    lineDataSet.setFillDrawable(drawable);//设置范围背景填充
                } else {
                    lineDataSet.setFillColor(Color.BLACK);
                }

                //设置折线图模式(直线,曲线...)
                //曲线
                //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSets.add(lineDataSet);
            }
        }

        LineData lineData = new LineData(dataSets);
        lineData.setValueTextSize(10f);
        lineData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return entry.getY() + "%";
            }
        });

        return lineData;
    }


}

