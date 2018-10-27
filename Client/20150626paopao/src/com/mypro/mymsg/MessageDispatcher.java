package com.mypro.mymsg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

import android.app.Activity;
import android.util.Log;

import com.mypro.db.ChatMsgDatabaseHelper;
import com.mypro.paopao.FriendsListActivity;
import com.mypro.paopao.NomalChatActivity;

public class MessageDispatcher {

	private static MessageDispatcher dispatcher = null;

	public static synchronized MessageDispatcher getInstance() {
		if (null == dispatcher)
			dispatcher = new MessageDispatcher();
		return dispatcher;
	}

	private Activity activity = null;

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
		new Thread() {
			public void run() {
				postQ();
			};
		}.start();
	}

	public static void Init(Activity activity) {
		getInstance().setActivity(activity);
	}

	Queue<FileTransferRequest> messageQue = new ArrayDeque<FileTransferRequest>();

	public MessageDispatcher() {
		// while (messageQue.size() != 0) {
		// if (activity != null && activity instanceof NomalChatActivity) {
		// NomalChatActivity nomal = (NomalChatActivity) activity;
		// nomal.OnReceiveFile(messageQue.peek());
		// messageQue.remove();
		// }
		// }
		// new Thread() {
		// @Override
		// public void run() {
		// while (true) {
		// aasdf();
		// }
		// }
		// }.start();
	}

	public synchronized void postQ() {
		if (activity != null && activity instanceof NomalChatActivity) {
			NomalChatActivity nomal = (NomalChatActivity) activity;
			// if (nomal.oppositeName.equals(request.getRequestor()))
			// Log.e("fileRecv", request.getRequestor());
			while (messageQue.size() != 0) {
				if (activity != null && activity instanceof NomalChatActivity) {
					nomal.OnReceiveFile(messageQue.peek());
					messageQue.remove();
				}
			}
		}
	}

	public void dispatchFileRecvMessage(FileTransferRequest request) {
		messageQue.add(request);
		postQ();
		// if (activity != null && activity instanceof NomalChatActivity) {
		// // aasdf();
		// }
	}

	HashMap<String, Integer> m = new HashMap<String, Integer>();

	/**
	 * �ɷ���Ϣ��û�п��̣߳��ڵ����߳�ִ��
	 * */
	public int dispatchMessage(MessageData msg) {

		// Log.e("mydispatch",""+msg.getStrTime());
		ChatMsgDatabaseHelper.getInstance().addMsg(msg);
		int thisid = ChatMsgDatabaseHelper.getInstance().findLastMsgId();
		if (activity != null && activity instanceof NomalChatActivity) {
			NomalChatActivity nomal = (NomalChatActivity) activity;
			nomal.messageChange();
		}else if(activity!=null&&activity instanceof FriendsListActivity ){
			FriendsListActivity friend = (FriendsListActivity) activity;
			if(msg.getChatType()==0)
				friend.receiveMsgCount(msg.getSender());
		}
		return thisid;
	}
}
