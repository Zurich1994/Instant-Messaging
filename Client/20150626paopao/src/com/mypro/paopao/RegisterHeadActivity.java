package com.mypro.paopao;

import com.base.EntityDrawable;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterHeadActivity extends Activity {

	public int[] imageId = new int[] { R.drawable.head01, R.drawable.head02,
			R.drawable.head03, R.drawable.head04, R.drawable.head05,
			R.drawable.head06, R.drawable.head07, R.drawable.head08,
			R.drawable.head09 }; // ���岢��ʼ������ͷ��id������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeration_head); // ���ø�Activityʹ�õĲ���
		GridView gridview = (GridView) findViewById(R.id.gridView1); // ��ȡGridView���
		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageview; // ����ImageView�Ķ���
				if (convertView == null) {
					imageview = new ImageView(RegisterHeadActivity.this); // ʵ����ImageView�Ķ���
					/************* ����ͼ��Ŀ�Ⱥ͸߶� ******************/
					imageview.setAdjustViewBounds(true);
					imageview.setMaxWidth(158);
					imageview.setMaxHeight(150);
					/**************************************************/
					imageview.setPadding(5, 5, 5, 5); // ����ImageView���ڱ߾�
				} else {
					imageview = (ImageView) convertView;
				}
				imageview.setImageResource(imageId[position]); // ΪImageView����Ҫ��ʾ��ͼƬ
				return imageview; // ����ImageView
			}

			/*
			 * ���ܣ���õ�ǰѡ���ID
			 */
			@Override
			public long getItemId(int position) {
				return position;
			}

			/*
			 * ���ܣ���õ�ǰѡ��
			 */
			@Override
			public Object getItem(int position) {
				return position;
			}

			/*
			 * �������
			 */
			@Override
			public int getCount() {
				return imageId.length;
			}
		};

		gridview.setAdapter(adapter); // ����������GridView����
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent(); // ��ȡIntent����
				Resources r = RegisterHeadActivity.this.getResources();
				Drawable b = r.getDrawable(imageId[position]);
				EntityDrawable tmp = new EntityDrawable(b);
				intent.putExtra("pic", tmp);
				setResult(0x11, intent); // ���÷��صĽ���룬�����ص��ø�Activity��Activity
				finish();// �رյ�ǰActivity
			}
		});
		Button button = (Button) findViewById(R.id.photo_B); // ��ȡѡ��ͷ��ť
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterHeadActivity.this,
						RegisterPhotoActivity.class);
				startActivityForResult(intent, 0x11); // ����intent��Ӧ��Activity

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0x11:

			// Toast.makeText(getApplicationContext(), ""+ed, 0).show();
			setResult(0x11, data);
			finish();
			break;
		}
	}

}
