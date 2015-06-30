package signsocket;

import android.os.Handler;
import android.text.TextUtils;


public class SocketThreadManager
{
	
	private static SocketThreadManager s_SocketManager = null;
	
	private SocketInputThread mInputThread = null;
	
	private SocketOutputThread mOutThread = null;
	
	private SocketHeartThread mHeartThread = null;

	
	// 鑾峰彇鍗曚緥
	public static SocketThreadManager sharedInstance()
	{
		if (s_SocketManager == null)
		{
			s_SocketManager = new SocketThreadManager();
			s_SocketManager.startThreads();
		}
		return s_SocketManager;
	}
	
	// 鍗曚緥锛屼笉鍏佽鍦ㄥ閮ㄦ瀯寤哄璞�
	private SocketThreadManager()
	{
		mHeartThread = new SocketHeartThread();
		mInputThread = new SocketInputThread();
		mOutThread = new SocketOutputThread();
	}
	
	/**
	 * 鍚姩绾跨▼
	 */
	
	private void startThreads()
	{
		mHeartThread.start();
		mInputThread.start();
		mInputThread.setStart(true);
		mOutThread.start();
		mInputThread.setStart(true);
		// mDnsthread.start();
	}
	
	/**
	 * stop绾跨▼
	 */
	public void stopThreads()
	{
		mHeartThread.stopThread();
		mInputThread.setStart(false);
		mOutThread.setStart(false);
	}
	
	public static void releaseInstance()
	{
		if (s_SocketManager != null)
		{
			s_SocketManager.stopThreads();
			s_SocketManager = null;
		}
	}
	
	public void sendMsg(byte [] buffer, Handler handler)
	{
		MsgEntity entity = new MsgEntity(buffer, handler);
		mOutThread.addMsgToSendList(entity);
	}
	
}
