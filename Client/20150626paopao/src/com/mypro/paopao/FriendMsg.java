package com.mypro.paopao;

import org.jivesoftware.smackx.packet.VCard;

import com.base.XmppConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FriendMsg extends Activity {
	String nickname = null;
	String phone = null;
	String birthday = null;
	String school = null;
	TextView tx_account = null;
	TextView tx_nickname = null;
	TextView tx_birthday = null;
	TextView tx_school = null;
	TextView tx_phone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendmsg);

		tx_account = (TextView) findViewById(R.id.frindemsg_account);
		tx_nickname = (TextView) findViewById(R.id.frindemsg_nackname);
		tx_school = (TextView) findViewById(R.id.frindemsg_school);
		tx_birthday = (TextView) findViewById(R.id.frindemsg_birth);
		tx_phone = (TextView) findViewById(R.id.frindemsg_phone);

		Intent it = getIntent();
		final String account = it.getStringExtra("name");
		new Thread() {
			public void run() {
				VCard card = XmppConnection.getInstance().getUserVCard(account);
				nickname = card.getNickName();
				phone = card.getPhoneHome("phone");
				birthday = card.getField("birthday");
				school = card.getField("school");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tx_account.setText(account == null ? "" : account);
						tx_nickname.setText(nickname == null ? "" : nickname);
						tx_school.setText(school == null ? "" : school);
						tx_birthday.setText(birthday == null ? "" : birthday);
						tx_phone.setText(phone == null ? "" : phone);
					}
				});
			}
		}.start();

		final AlertDialog.Builder bu = new AlertDialog.Builder(FriendMsg.this);
		final EditText et = new EditText(FriendMsg.this);
		bu.setTitle("请输入备注名");
		bu.setView(et);
		//bu.show();
		bu.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Thread() {
					public void run() {
						final boolean bl = XmppConnection.getInstance()
								.addUser(
										account,
										et.getText() == null ? null : et
												.getText().toString());
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String str = "添加成功";
								if (bl == false)
									str = "添加失败";
								Toast.makeText(getApplicationContext(), str, 0)
										.show();
							}
						});
					}
				}.start();
				dialog.dismiss();
			}
		});
		bu.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		final AlertDialog dlg = bu.create();
		findViewById(R.id.friendmsg_addfriend).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.show();
					}
				});

	}
}
