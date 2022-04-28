package com.xxx.xxx.utils;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.List;

public class LinePathUtil {
    private static final float SMOOTH_FACTOR = 0.16f;

    /**
     * 根据指定点创建曲线Path
     * <p>默认使用贝塞尔曲线<p/>
     *
     * @param originPoints 指定点
     * @return Path
     */
    public static void createLinePath(Path outPath, List<PointF> originPoints) {
        createLinePath(outPath, originPoints, true);
    }

    /**
     * 根据指定点创建曲线Path
     *
     * @param originPoints 指定点
     * @param isCubic      是否使用贝塞尔曲线
     * @return Path
     */
    public static void createLinePath(Path outPath, List<PointF> originPoints, boolean isCubic) {
        if (isCubic) {
            createCubicPath(outPath, originPoints);
            return;
        }
        createNormalLinePath(outPath, originPoints);
    }

    /**
     * 根据指定点创建曲线Path
     *
     * @param originPoints 指定点
     */
    private static void createCubicPath(Path outPath, List<PointF> originPoints) {
        if (outPath == null || originPoints == null || originPoints.isEmpty()) {
            return;
        }
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;

        for (int i = 0; i < originPoints.size(); ++i) {
            if (Float.isNaN(currentPointX)) {
                currentPointX = originPoints.get(i).x;
                currentPointY = originPoints.get(i).y;
            }
            if (Float.isNaN(previousPointX)) {
                if (i > 0) {
                    previousPointX = originPoints.get(i - 1).x;
                    previousPointY = originPoints.get(i - 1).y;
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }
            if (Float.isNaN(prePreviousPointX)) {
                if (i > 1) {
                    prePreviousPointX = originPoints.get(i - 2).x;
                    prePreviousPointY = originPoints.get(i - 2).y;
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }
            // nextPoint is always new one or it is equal currentPoint.
            if (i < originPoints.size() - 1) {
                nextPointX = originPoints.get(i + 1).x;
                nextPointY = originPoints.get(i + 1).y;
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (i == 0) {
                // Move to start point.
                outPath.moveTo(currentPointX, currentPointY);
            } else {
                // Calculate control points.
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (SMOOTH_FACTOR * firstDiffX);
                final float firstControlPointY = previousPointY + (SMOOTH_FACTOR * firstDiffY);
                final float secondControlPointX = currentPointX - (SMOOTH_FACTOR * secondDiffX);
                final float secondControlPointY = currentPointY - (SMOOTH_FACTOR * secondDiffY);
                outPath.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // Shift values by one back to prevent recalculation of values that have
            // been already calculated.
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
    }

    /**
     * 根据指定点创建折线Path
     *
     * @param outPath      path
     * @param originPoints 指定点
     */
    private static void createNormalLinePath(Path outPath, List<PointF> originPoints) {
        if (outPath == null || originPoints == null || originPoints.isEmpty()) {
            return;
        }
        outPath.moveTo(originPoints.get(0).x, originPoints.get(0).y);

        float nextPointX;
        float nextPointY;

        for (int i = 0; i < originPoints.size() - 1; i++) {
            nextPointX = originPoints.get(i + 1).x;
            nextPointY = originPoints.get(i + 1).y;

            outPath.lineTo(nextPointX, nextPointY);
        }
    }

    private static int si(int setSize, int i) {
        if (i > setSize - 1) {
            return setSize - 1;
        } else if (i < 0) return 0;
        return i;
    }
}
