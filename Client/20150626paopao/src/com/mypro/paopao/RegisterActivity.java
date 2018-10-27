package com.mypro.paopao;

import java.io.ByteArrayOutputStream;

import com.base.EntityDrawable;
import com.base.XmppConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	// private Bitmap photoS = null;
	/** Called when the activity is first created. */
	public static ImageView iv1;
	public static int num = 0;
	public int imageId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		Button button = (Button) findViewById(R.id.registration_photo); // ��ȡѡ��ͷ��ť
		// iv1=(ImageView)findViewById(R.id.head_IV); //��ȡ�����ļ�����ӵ�ImageView���
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						RegisterHeadActivity.class);
				startActivityForResult(intent, 0x11); // ����intent��Ӧ��Activity

			}
		});
		Button submit = (Button) findViewById(R.id.registration_submit); // ��ȡ�ύ��ť
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String user = ((EditText) findViewById(R.id.registration_account))
						.getText().toString(); // ��ȡ������û�
				final String pwd = ((EditText) findViewById(R.id.registration_password))
						.getText().toString(); // ��ȡ���������
				String repwd = ((EditText) findViewById(R.id.registration_repassword))
						.getText().toString(); // ��ȡ�����ȷ������
				// String
				// email=((EditText)findViewById(R.id.email_ET)).getText().toString();
				// //��ȡ�����E-mail��ַ
				if (!"".equals(user) && !"".equals(pwd)) {

					if (!pwd.equals(repwd)) { // �ж���������������Ƿ�һ��
						Toast.makeText(RegisterActivity.this,
								"������������벻һ�£����������룡", Toast.LENGTH_LONG).show();
						((EditText) findViewById(R.id.registration_password))
								.setText(""); // �������༭��
						((EditText) findViewById(R.id.registration_repassword))
								.setText(""); // ���ȷ������༭��
						// ((EditText)findViewById(R.id.email_ET)).requestFocus();
						// //������༭���ý���
					} else { // ���������Ϣ���浽Bundle�У�������һ���µ�Activity��ʾ������û�ע����Ϣ
						new Thread() {
							public void run() {

								String result = XmppConnection.getInstance()
										.regist(user, pwd);
								if (result.equals("1")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"ע��ɹ�", 0).show();
										}
									});
								} else if (result.equals("2")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"����˺��Ѿ�����", 0).show();
										}
									});
								} else if (result.equals("0")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"������û�з��ؽ��", 0).show();
										}
									});
								}
							}
						}.start();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "�뽫ע����Ϣ����������",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Intent intent;
		iv1 = (ImageView) findViewById(R.id.head_IV); // ��ȡ�����ļ�����ӵ�ImageView���
		if (requestCode == 0x11 && resultCode == 0x11) { // �ж��Ƿ�Ϊ������Ľ��
			// Bundle bundle=data.getExtras(); //��ȡ���ݵ����ݰ�
			// imageId=bundle.getInt("imageId"); //��ȡѡ���ͷ��ID
			Intent intent = data;
			EntityDrawable ed = (EntityDrawable) intent
					.getSerializableExtra("pic");
			Toast.makeText(getApplicationContext(), "" + ed, 0).show();
			// iv1.setImageBitmap(ed.getBitmapBytes())
			iv1.setImageDrawable(ed.getDrawable());
			//
			// EntityDrawable dw = (EntityDrawable)
			// data.getSerializableExtra("pic");
			// if(dw!=null) iv1.setImageDrawable(dw.drawable);

			// if(num==1)
			// {
			// Registration_M.iv1.setImageBitmap(Photo_M.photo);
			// num=0;
			// //iv1.setImageResource(imageId); //��ʾѡ���ͷ��
			// }
			// else
			// {
			//
			// //Registration_M.iv1.setImageBitmap(Photo_M.photo);
			// //num=0;
			// iv1.setImageResource(imageId); //��ʾѡ���ͷ��
			//
			// }
		}
	}

}
