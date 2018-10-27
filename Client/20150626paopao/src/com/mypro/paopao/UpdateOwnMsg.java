package com.mypro.paopao;

import org.jivesoftware.smackx.packet.VCard;

import com.base.XmppConnection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateOwnMsg extends Activity {
	VCard card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_altermydata);

		final TextView tx_account = (TextView) findViewById(R.id.altermy_account);
		final EditText ed_phone = (EditText) findViewById(R.id.altermy_phone);
		final EditText ed_birthday = (EditText) findViewById(R.id.altermy_birth);
		final EditText ed_nickname = (EditText) findViewById(R.id.altermy_nackname);
		final EditText ed_school = (EditText) findViewById(R.id.altermy_school);

		new Thread() {
			public void run() {
				VCard card = XmppConnection.getInstance().getUserVCard(
						XmppConnection.getInstance().getLocalUserAccount());
				final String account = card.getJabberId();
				final String nickname = card.getNickName();
				final String school = card.getField("school");
				final String birthday = card.getField("birthday");
				final String phone = card.getPhoneHome("phone");
				runOnUiThread(new Runnable() {
					public void run() {
						tx_account.setText(account == null ? "" : account);
						ed_nickname.setText(nickname == null ? "" : nickname);
						ed_school.setText(school == null ? "" : school);
						ed_birthday.setText(birthday == null ? "" : birthday);
						ed_phone.setText(phone == null ? "" : phone);
					}
				});
			}
		}.start();
		
		findViewById(R.id.altercancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						UpdateOwnMsg.this.finish();
					}
				});
		
		findViewById(R.id.altersure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						final boolean flag = XmppConnection.getInstance()
								.modifyMessage(
										ed_nickname.getText().toString(),
										ed_phone.getText().toString(),
										ed_birthday.getText().toString(),
										ed_school.getText().toString());
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (flag)
									Toast.makeText(getApplicationContext(),
											"保存成功", 0).show();
								else
									Toast.makeText(getApplicationContext(),
											"保存失败，请重试", 0).show();
							}
						});
						// Toast.makeText(get, text, duration)
					}
				}.start();
			}
		});
	}
}
