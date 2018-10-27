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
		Button button = (Button) findViewById(R.id.registration_photo); // 获取选择头像按钮
		// iv1=(ImageView)findViewById(R.id.head_IV); //获取布局文件中添加的ImageView组件
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						RegisterHeadActivity.class);
				startActivityForResult(intent, 0x11); // 启动intent对应的Activity

			}
		});
		Button submit = (Button) findViewById(R.id.registration_submit); // 获取提交按钮
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String user = ((EditText) findViewById(R.id.registration_account))
						.getText().toString(); // 获取输入的用户
				final String pwd = ((EditText) findViewById(R.id.registration_password))
						.getText().toString(); // 获取输入的密码
				String repwd = ((EditText) findViewById(R.id.registration_repassword))
						.getText().toString(); // 获取输入的确认密码
				// String
				// email=((EditText)findViewById(R.id.email_ET)).getText().toString();
				// //获取输入的E-mail地址
				if (!"".equals(user) && !"".equals(pwd)) {

					if (!pwd.equals(repwd)) { // 判断两次输入的密码是否一致
						Toast.makeText(RegisterActivity.this,
								"两次输入的密码不一致，请重新输入！", Toast.LENGTH_LONG).show();
						((EditText) findViewById(R.id.registration_password))
								.setText(""); // 清空密码编辑框
						((EditText) findViewById(R.id.registration_repassword))
								.setText(""); // 清空确认密码编辑框
						// ((EditText)findViewById(R.id.email_ET)).requestFocus();
						// //让密码编辑框获得焦点
					} else { // 将输入的信息保存到Bundle中，并启动一个新的Activity显示输入的用户注册信息
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
													"注册成功", 0).show();
										}
									});
								} else if (result.equals("2")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"这个账号已经存在", 0).show();
										}
									});
								} else if (result.equals("0")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"服务器没有返回结果", 0).show();
										}
									});
								}
							}
						}.start();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "请将注册信息输入完整！",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Intent intent;
		iv1 = (ImageView) findViewById(R.id.head_IV); // 获取布局文件中添加的ImageView组件
		if (requestCode == 0x11 && resultCode == 0x11) { // 判断是否为待处理的结果
			// Bundle bundle=data.getExtras(); //获取传递的数据包
			// imageId=bundle.getInt("imageId"); //获取选择的头像ID
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
			// //iv1.setImageResource(imageId); //显示选择的头像
			// }
			// else
			// {
			//
			// //Registration_M.iv1.setImageBitmap(Photo_M.photo);
			// //num=0;
			// iv1.setImageResource(imageId); //显示选择的头像
			//
			// }
		}
	}

}
