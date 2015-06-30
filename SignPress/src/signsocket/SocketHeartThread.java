package signsocket;

import java.io.IOException;

import android.text.TextUtils;


class SocketHeartThread extends Thread
{
	boolean isStop = false;
	boolean mIsConnectSocketSuccess = false;
	static SocketHeartThread s_instance;
	
	private TCPClient mTcpClient = null;
	
	static final String tag = "SocketHeartThread";
	
	public static synchronized SocketHeartThread instance()
	{
		if (s_instance == null)
		{
			s_instance = new SocketHeartThread();
		}
		return s_instance;
	}
	
	public SocketHeartThread()
	{
	   TCPClient.instance();
				// 杩炴帴鏈嶅姟鍣�
	//	mIsConnectSocketSuccess = connect();

	}

	public void stopThread()
	{
		isStop = true;
	}
	
	/**
	 * 杩炴帴socket鍒版湇鍔″櫒, 骞跺彂閫佸垵濮嬪寲鐨凷ocket淇℃伅
	 * 
	 * @return
	 */
	
	
	private boolean reConnect()
	{
		return TCPClient.instance().reConnect();
	}

	
	public void run()
	{
		isStop = false;
		while (!isStop)
		{
				// 鍙戦�涓�釜蹇冭烦鍖呯湅鏈嶅姟鍣ㄦ槸鍚︽甯�
				boolean canConnectToServer = TCPClient.instance().canConnectToServer();
				
				if(canConnectToServer == false){
					reConnect();
				}
				try
				{
					Thread.sleep(Const.SOCKET_HEART_SECOND * 1000);
					
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
	}
}
