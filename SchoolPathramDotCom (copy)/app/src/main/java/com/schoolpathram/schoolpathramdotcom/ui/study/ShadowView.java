package com.schoolpathram.schoolpathramdotcom.ui.study;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ShadowView extends View {
//    private Bitmap bitmap = null;

    public ShadowView(Context context) {
        super(context);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//没有这句不显示
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 建立Paint 物件
        Paint paint3 = new Paint();
        paint3.setColor(Color.WHITE);
        paint3.setAntiAlias(true);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        paint3.setShadowLayer(21, 0, 10, 0x2600A4A0);
        // 实心矩形& 其阴影
        canvas.drawCircle(400, 500, 96, paint3);
    }

}