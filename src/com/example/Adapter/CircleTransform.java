package com.example.Adapter;

import com.squareup.picasso.Transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleTransform implements Transformation {
	@Override
	public Bitmap transform(Bitmap source) {
		int size = Math.min(source.getWidth(), source.getHeight());

		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
		if (squaredBitmap != source) {
			source.recycle();// ��������
		}

		Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		BitmapShader shader = new BitmapShader(squaredBitmap,
				BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);// ����һ����Ⱦ��
		paint.setShader(shader);// ������Ⱦ��
		paint.setAntiAlias(true);// ���ÿ��ܳݣ�ͼƬ��Ե������

		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);// ����ͼ��

		squaredBitmap.recycle();
		return bitmap;
	}

	@Override
	public String key() {
		return "circle";
	}
}
