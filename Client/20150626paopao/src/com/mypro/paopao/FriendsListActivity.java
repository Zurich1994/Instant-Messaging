package com.mypro.paopao;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;

import com.base.XmppConnection;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mypro.mymsg.MessageDispatcher;
import com.readystatesoftware.viewbadger.BadgeView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsListActivity extends Activity {
	ListView list = null;
	PullToRefreshListView mPullRefreshListView = null;
	MyApapter adapter = null;

	public void addMenu() {
		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu_friend);
		// menu.setSecondaryMenu(R.layout.menu_friend);
		menu.setFadeEnabled(false);
		menu.setBehindScrollScale(0.25f);
		menu.setFadeDegree(0.25f);
		menu.setBackgroundResource(R.drawable.friendmsgbg);
		// menu.setBackgroundColor(Color.parseColor("#B0E2FF"));

		menu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
			}
		});
		menu.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
		Button bt = (Button) menu.findViewById(R.id.menu_findfriend);
		// Log.e("bt", "" + bt);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "vv", 0).show();
				Intent it = new Intent(FriendsListActivity.this,
						FindFriend.class);
				startActivity(it);
			}
		});
		menu.findViewById(R.id.menu_altermsg).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent it = new Intent(FriendsListActivity.this,
								UpdateOwnMsg.class);
						startActivity(it);
					}
				});
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			getFriendList();
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// item.getItemId()
		// Toast.makeText(
		// getApplicationContext(),
		// adapter.getItem(menuInfo.position - 1).nickname + ""
		// + item.getItemId(), 0).show();
		final FriendData data = adapter.getItem(menuInfo.position - 1);
		// item.getTitle().toString()

		switch (item.getItemId()) {
		case 1:
			String s = data.account;
			if (s.lastIndexOf("@") != -1)
				s = s.substring(0, s.lastIndexOf("@"));
			Intent intent = new Intent(FriendsListActivity.this,
					FriendMsg.class);
			intent.putExtra("name", s);
			startActivity(intent);
			break;
		case 2:
			new Thread() {
				public void run() {
					String s = data.account;
					if (s.lastIndexOf("@") != -1)
						s = s.substring(0, s.lastIndexOf("@"));
					XmppConnection.getInstance().removeUser(s);
				}
			}.start();
			getFriendList();
			adapter.notifyDataSetChanged();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("选择");
		menu.add(0, 1, 0, "查看好友信息");
		menu.add(0, 2, 0, "删除好友");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		// addMenu();
		MessageDispatcher.Init(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.friend_listview);
		list = mPullRefreshListView.getRefreshableView();
		// registerForContextMenu(list);
		// addMenu();
		registerForContextMenu(list);
		// mPullRefreshListView.setonr
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							final PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						new FinishRefresh().execute();
					}
				});

		// list = (ListView) findViewById(R.id.friend_listview);
		getFriendList();
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FriendData m = m_friendData.get(position - 1);
				m.msgcnt=0;
				adapter.notifyDataSetChanged();
				String w = null;
				if (m.account.indexOf("@") == -1)
					w = m.account;
				else {
					w = m.account.substring(0, m.account.indexOf("@"));
				}
				Intent it = new Intent(FriendsListActivity.this,
						NomalChatActivity.class);
				it.putExtra("oppsite", w);
				startActivity(it);
			}
		});

		addMenu();
	}
	
	public void receiveMsgCount(String sender){
		for(FriendData i:m_friendData){
			String to = i.account;
			int tp = to.lastIndexOf("@");
			if(tp!=-1){
				to = to.substring(0,tp);
			}
			Log.e("compare",""+sender+"?"+to);
			if(to.equals(sender))
				i.msgcnt++;
		}
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {				
				adapter.notifyDataSetChanged();
			}
		});
	}

	ArrayList<FriendData> m_friendData = new ArrayList<FriendsListActivity.FriendData>();

	class FriendData {
		String account;
		String nickname;
		Drawable avtor;
		String title;
		int msgcnt;

		FriendData() {
			msgcnt = 0;
		}
	}

	public void getFriendList() {
		m_friendData.clear();
		new Thread() {
			@Override
			public void run() {
				// ArrayList<FriendData> f = new
				// ArrayList<FriendsListActivity.FriendData>();
				List<RosterEntry> x = XmppConnection.getInstance()
						.getAllEntries();
				for (RosterEntry i : x) {
					FriendData data = new FriendData();

					String w = i.getName();
					if (w != null && w.length() != 0)
						data.nickname = i.getName();
					else {
						data.nickname = i.getUser();
						/**
						 * admin做特殊处理
						 * */
						Log.e("Friendlist",
								data.nickname + "*"
										+ data.nickname.lastIndexOf("@"));
						if (data.nickname.lastIndexOf("@") != -1)
							data.nickname = data.nickname.substring(0,
									data.nickname.lastIndexOf("@"));
					}

					data.account = i.getUser();
					m_friendData.add(data);
				}
				adapter = new MyApapter();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						list.setAdapter(adapter);
					}
				});
			}
		}.start();
		// XmppConnection.get
	}

	class ViewHolder {
		ImageView avtor;
		TextView name;
		TextView descripe;
		BadgeView badge;
		View global;
		
		ViewHolder(View view) {
			avtor = (ImageView) view.findViewById(R.id.friend_item_avtor);
			name = (TextView) view.findViewById(R.id.friend_item_nackname);
			descripe = (TextView) view.findViewById(R.id.friend_item_descripte);
			global = view.findViewById(R.id.friend_item_global);
			badge=new BadgeView(FriendsListActivity.this,descripe);
			//badge.setText(2);
			//badge.show();
		}
	}

	class MyApapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyApapter() {
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return m_friendData.size();
		}

		@Override
		public FriendData getItem(int position) {
			return m_friendData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View v = convertView;

			if (null == v) {

				v = mInflater.inflate(R.layout.friend_item, null);
				holder = new ViewHolder(v);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			FriendData dat = getItem(position);
			if (dat.avtor != null)
				holder.avtor.setImageDrawable(dat.avtor);
			if (dat.account != null)
				holder.name.setText(dat.nickname);
			if (dat.title != null)
				holder.descripe.setText(dat.title);
			
			//BadgeView bg = new BadgeView(FriendsListActivity.this, v);
			if (dat.msgcnt != 0){
				holder.badge.setText(""+dat.msgcnt);
				holder.badge.show();
//				bg.setText(dat.msgcnt);
//				bg.show();
			}
			return v;
		}

	}
}
