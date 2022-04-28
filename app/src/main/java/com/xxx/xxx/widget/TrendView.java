package com.xxx.xxx.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.xxx.xxx.utils.LinePathUtil;

import java.util.ArrayList;
import java.util.List;

public class TrendView extends View {

    private final int DEFAULT_FULL_WIDTH_COUNT = 6;//默认满屏宽度展示多少
    private final int ALL_COUNT = 10;//默认Item数量
    private final int DEFAULT_VIEW_HEIGHT = 300;//默认View高度
    private final int DEFAULT_TREND_HEIGHT = 150;//默认趋势图区域高度
    //默认高温曲线颜色
    private static final int DEFAULT_HIGH_POINT_COLOR = Color.parseColor("#FF8F8F");
    //默认高温曲线阴影颜色
    private static final int DEFAULT_HIGH_POINT_SHADOW_COLOR = Color.parseColor("#4DFF8F8F");
    private static final float DEFAULT_CURRENT_POINT_RADIUS = 3.5f;
    private static final int DEFAULT_OUT_DATE_ALPHA = 0x66;

    private int itemWidth;//每个子View的宽度
    private Rect drawRect = new Rect();//画布
    private List<PointF> mHeightPoints = new ArrayList<>();
    private Paint mTodayPaint;//当前日期描边的画笔
    private Paint mPointPaint;//点的画笔
    private Paint mLinePaint;//线的画笔
    private Path mAllPath = new Path();//总曲线路径
    private Path mHighPath = new Path();//未过期曲线路径
    private Path mHighOutDatePath = new Path();//过期曲线路径
    private PathMeasure mPathMeasure = new PathMeasure();//路径测量
    private int mCurIndex = 1;//过期坐标
    private Integer[] pointData = {4, 1, 5, 6, 7, 8, 10, 4, 3, 5};

    public TrendView(Context context) {
        super(context);
        init();
    }

    public TrendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TrendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //今日描边的画笔
        mTodayPaint = new Paint();
        mTodayPaint.setColor(Color.YELLOW);
        mTodayPaint.setAlpha(180);
        mTodayPaint.setAntiAlias(true);
        mTodayPaint.setStrokeWidth(3);
        mTodayPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //设置坐标点画笔
        mPointPaint = new Paint();
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStrokeWidth(3);
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //设置曲线画笔
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(dp2px(1.5f));
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据满屏展示几个，计算出单个item展示的宽度
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (specWidth == 0) {
            specWidth = getResources().getDisplayMetrics().widthPixels;
        }
        itemWidth = (specWidth - getPaddingLeft() - getPaddingRight()) / DEFAULT_FULL_WIDTH_COUNT;
        //计算出所有item的总宽度
        int width = itemWidth * ALL_COUNT + getPaddingLeft() + getPaddingRight();
        //高度
        int height = dp2px(DEFAULT_VIEW_HEIGHT) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //设置画布坐标
        drawRect.left = left + getPaddingLeft();
        drawRect.right = right - getPaddingRight();
        drawRect.top = top + getPaddingTop();
        drawRect.bottom = bottom + getPaddingBottom();

        //起始X轴坐标
        int startX = itemWidth / 2 + getPaddingLeft();
        //起始Y轴位置
        int startY = dp2px(DEFAULT_VIEW_HEIGHT - DEFAULT_TREND_HEIGHT) / 2 - getPaddingTop();
        //计算点位
        buildPoint(startX, startY);
        //计算曲线路径
        buildLinePath();
    }

    private void buildPoint(int startX, int startY) {
        //获取高线数据的最高点和最低点
        int max = 10;
        int min = 1;
        float trendUnitHeight = dp2px(DEFAULT_TREND_HEIGHT) / (max - min);
        for (int i = 0; i < pointData.length; i++) {
            PointF pointF = new PointF();
            pointF.x = itemWidth * i + startX;
            pointF.y = startY + pointData[i] * trendUnitHeight;
            mHeightPoints.add(pointF);
        }
    }

    private void buildLinePath() {
        //计算曲线路径
        mAllPath.reset();
        mHighPath.reset();
        mHighOutDatePath.reset();

        //总曲线路径
        LinePathUtil.createLinePath(mAllPath, mHeightPoints);
        //过期曲线路径
        LinePathUtil.createLinePath(mHighOutDatePath, mHeightPoints.subList(0, mCurIndex + 1));

        //计算过期曲线的长度
        mPathMeasure.setPath(mHighOutDatePath, false);
        float length = mPathMeasure.getLength();

        //设置测量总曲线
        mPathMeasure.setPath(mAllPath, false);
        //重置过期曲线路径，从总曲线中截取。这是因为当过期曲线只有2个点的时候路径是直线，不是曲线。
        mHighOutDatePath.reset();
        //取出过期曲线的path
        mPathMeasure.getSegment(0, length, mHighOutDatePath, true);
        //取出未过期曲线的path
        mPathMeasure.getSegment(length, mPathMeasure.getLength(), mHighPath, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawItem(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        //虚线设置
        PathEffect effect = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);

        //绘制过期曲线
        mLinePaint.setPathEffect(null);
        mLinePaint.setColor(DEFAULT_HIGH_POINT_COLOR);
        mLinePaint.setAlpha(DEFAULT_OUT_DATE_ALPHA);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(effect);
        canvas.drawPath(mHighOutDatePath, mLinePaint);
        //绘制未过期曲线
        mLinePaint.setPathEffect(null);
        mLinePaint.setAlpha(0xFF);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setShadowLayer(dp2px(8), 0, dp2px(5), DEFAULT_HIGH_POINT_COLOR);
        canvas.drawPath(mHighPath, mLinePaint);
    }

    private void drawItem(Canvas canvas) {
        for (int i = 0; i < pointData.length; i++) {
            //绘制今日描边
            if (i == mCurIndex) {
                int radius = dp2px(8);
                canvas.drawRoundRect((i * itemWidth) + getPaddingLeft(), 0, ((i + 1) * itemWidth) + getPaddingLeft(), drawRect.bottom, radius, radius, mTodayPaint);
            }

            //绘制点
            mPointPaint.setColor(DEFAULT_HIGH_POINT_COLOR);
            mPointPaint.setShadowLayer(2f, 0, dp2px(3), DEFAULT_HIGH_POINT_SHADOW_COLOR);
            PointF point = mHeightPoints.get(i);
            canvas.drawCircle(point.x, point.y, dp2px(DEFAULT_CURRENT_POINT_RADIUS), mPointPaint);

        }
    }

    private int dp2px(float dp) {
        Resources res = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}
