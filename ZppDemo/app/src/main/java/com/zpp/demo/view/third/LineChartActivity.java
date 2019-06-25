package com.zpp.demo.view.third;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.LineCircleChart;
import com.github.mikephil.charting.renderer.LineChartCircleRenderer;
import com.zpp.demo.R;
import com.zpp.demo.tools.HaoChartStyleFour;
import com.zpp.demo.tools.LineChartHodel;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity extends AppCompatActivity {
    private LineCircleChart mChart;
    private LineChart lineChart;
    private ImageView redPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        initView();
    }

    private void initView() {
        mChart =  findViewById(R.id.chart_main);
        lineChart = findViewById(R.id.LineChart_main);
        redPosition=findViewById(R.id.red_position);
        initChart(mChart);

    }

    private void initChart(LineCircleChart mChart) {
        //模拟数据
        List<String> xValue = new ArrayList<>();
        List<Float> yValue = new ArrayList<>();
        List<List<Float>> yValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            xValue.add(i + "f");
            yValue.add(i % 2 == 0 ? i * 10f : i * 1f);
        }
        yValues.add(yValue);

        //设置那几个位置的圆点显示
        List<Integer> position = new ArrayList<>();
        position.add(0);
        position.add(3);
        position.add(6);
        LineChartCircleRenderer.setCirclePoints("",position);
        LineChartCircleRenderer.setCircleColor(Color.RED);//position点的颜色

        //设置图表
        HaoChartStyleFour.setLinesChart(this,mChart,xValue,yValues,new int[]{R.color.red},redPosition);
        LineChartHodel.setLinesChart(this,lineChart,xValue,yValues,new int[]{R.color.red});
    }
}
