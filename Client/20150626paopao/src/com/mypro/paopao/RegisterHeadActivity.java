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
			R.drawable.head09 }; // 定义并初始化保存头像id的数组

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeration_head); // 设置该Activity使用的布局
		GridView gridview = (GridView) findViewById(R.id.gridView1); // 获取GridView组件
		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageview; // 声明ImageView的对象
				if (convertView == null) {
					imageview = new ImageView(RegisterHeadActivity.this); // 实例化ImageView的对象
					/************* 设置图像的宽度和高度 ******************/
					imageview.setAdjustViewBounds(true);
					imageview.setMaxWidth(158);
					imageview.setMaxHeight(150);
					/**************************************************/
					imageview.setPadding(5, 5, 5, 5); // 设置ImageView的内边距
				} else {
					imageview = (ImageView) convertView;
				}
				imageview.setImageResource(imageId[position]); // 为ImageView设置要显示的图片
				return imageview; // 返回ImageView
			}

			/*
			 * 功能：获得当前选项的ID
			 */
			@Override
			public long getItemId(int position) {
				return position;
			}

			/*
			 * 功能：获得当前选项
			 */
			@Override
			public Object getItem(int position) {
				return position;
			}

			/*
			 * 获得数量
			 */
			@Override
			public int getCount() {
				return imageId.length;
			}
		};

		gridview.setAdapter(adapter); // 将适配器与GridView关联
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent(); // 获取Intent对象
				Resources r = RegisterHeadActivity.this.getResources();
				Drawable b = r.getDrawable(imageId[position]);
				EntityDrawable tmp = new EntityDrawable(b);
				intent.putExtra("pic", tmp);
				setResult(0x11, intent); // 设置返回的结果码，并返回调用该Activity的Activity
				finish();// 关闭当前Activity
			}
		});
		Button button = (Button) findViewById(R.id.photo_B); // 获取选择头像按钮
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterHeadActivity.this,
						RegisterPhotoActivity.class);
				startActivityForResult(intent, 0x11); // 启动intent对应的Activity

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
