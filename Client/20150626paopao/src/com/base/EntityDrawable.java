package com.base;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class EntityDrawable implements Serializable {
	private static final long serialVersionUID = 1L;
	private byte[] mBitmapBytes = null;

	public EntityDrawable(byte[] bitmapBytes) {
		// TODO Auto-generated constructor stub
		this.mBitmapBytes = bitmapBytes;
	}

	public EntityDrawable(Drawable drawable) {
		mBitmapBytes = drawableToBytes(drawable);
	}

	public Drawable getDrawable() {
		return bytesToDrawable(mBitmapBytes);
	}

	public byte[] getBitmapBytes() {
		return this.mBitmapBytes;
	}

	/**
	 * Drawableת����byte[]
	 * 
	 * @param d
	 * @return
	 */
	public byte[] drawableToBytes(Drawable d) {
		Bitmap bitmap = this.drawableToBitmap(d);
		return this.bitmapToBytes(bitmap);
	}

	/**
	 * Drawableת����Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmapת����byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[]ת����Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public Bitmap bytesToBitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**
	 * byte[]ת����Drawable
	 * 
	 * @param b
	 * @return
	 */
	public Drawable bytesToDrawable(byte[] b) {
		Bitmap bitmap = this.bytesToBitmap(b);
		return this.bitmapToDrawable(bitmap);
	}

	/**
	 * Bitmapת����Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public Drawable bitmapToDrawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

}
