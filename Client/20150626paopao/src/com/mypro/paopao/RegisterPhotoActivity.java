package com.mypro.paopao;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.base.EntityDrawable;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterPhotoActivity extends Activity {
	private static final int NONE = 0;
	private static final int PHOTO_GRAPH = 1;// ����
	private static final int PHOTO_ZOOM = 2; // ����
	private static final int PHOTO_RESOULT = 3;// ���
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private ImageView imageView1 = null;
	private Button button1 = null;
	private Button button2 = null;
	private Button button3 = null;
	static Bitmap photo = null;
	private String photoS = null;
	private Uri photoUri = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeration_photo);
		imageView1 = (ImageView) findViewById(R.id.imageView);
		button1 = (Button) findViewById(R.id.btnPhone);
		button1.setOnClickListener(onClickListener);
		button2 = (Button) findViewById(R.id.btnTakePicture);
		button2.setOnClickListener(onClickListener);
		button3 = (Button) findViewById(R.id.Photo_OK_B);
		button3.setOnClickListener(onClickListener);
		
	}

	private final View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == button1) { // ������ȡͼƬ
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, PHOTO_ZOOM);
			} else if (v == button2) { // �����ջ�ȡͼƬ
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intent, PHOTO_GRAPH);
			} else if (v == button3) {
				// Registration_M.num=1;
				// Registration_M.iv1.setImageBitmap(photo);
//				Intent intent = new Intent(Photo_M.this,
//				 Registration_M.class);
				Intent intent = getIntent();
				// Toast.makeText(Photo_M.this, "6666",1000).show();
				EntityDrawable tmp = new EntityDrawable(imageView1.getDrawable());
				Toast.makeText(RegisterPhotoActivity.this, "" + imageView1.getDrawable(),
						2000).show();
				//tmp.setDrawable(imageView1.getDrawable());
				intent.putExtra("pic", tmp);

				// Toast.makeText(Photo_M.this, "OK?",1000).show();
				// //intent.putExtra("s", imageView1.getDrawable()) ;
				// //Bundle bundle = new Bundle(); // ������ʵ����һ��Bundle����
				// finish();
				//startActivityForResult(intent, 0x11);
				RegisterPhotoActivity.this.setResult(0x11,intent);
				RegisterPhotoActivity.this.finish();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// ����
		if (requestCode == PHOTO_GRAPH) {
			// �����ļ�����·��
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
		}
		if (data == null)
			return;
		// ��ȡ�������ͼƬ
		if (requestCode == PHOTO_ZOOM) {
			startPhotoZoom(data.getData());
		}
		// ������
		if (requestCode == PHOTO_RESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)ѹ���ļ�
				// �˴����԰�Bitmap���浽sd���У������뿴��http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
				imageView1.setImageBitmap(photo); // ��ͼƬ��ʾ��ImageView�ؼ���
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");// ����Androidϵͳ�Դ���һ��ͼƬ����ҳ��,
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");// �����޼�
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_RESOULT);
	}
}