package signsocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * 瀹㈡埛绔啓娑堟伅绾跨▼
 * 
 * @author way
 * 
 */
public class SocketOutputThread extends Thread
{
	private boolean isStart = true;
	private static String tag = "socketOutputThread";
	private List<MsgEntity> sendMsgList;
	
	public SocketOutputThread( )
	{

		sendMsgList = new CopyOnWriteArrayList<MsgEntity>();
	}
	
	public void setStart(boolean isStart)
	{
		this.isStart = isStart;
		synchronized (this)
		{
			notify();
		}
	}

	// 浣跨敤socket鍙戦�娑堟伅
	public boolean sendMsg(byte[] msg) throws Exception
	{
				
		
		if (msg == null)
		{
			CLog.e(tag, "sendMsg is null");
			return false;
		}
		
		try
		{
			TCPClient.instance().sendMsg(msg);
			
		} catch (Exception e)
		{
			throw (e);
		}
		
		return true;
	}
	
	// 浣跨敤socket鍙戦�娑堟伅
	public void addMsgToSendList(MsgEntity msg) 
	{

		synchronized (this)
		{
			this.sendMsgList.add(msg);
			notify();
		}
	}
	
	@Override
	public void run()
	{
		while (isStart)
		{
			// 閿佸彂閫乴ist
			synchronized (sendMsgList)
			{
				// 鍙戦�娑堟伅
				for (MsgEntity msg : sendMsgList)
				{
					
					Handler handler = msg.getHandler();
					try
					{
						sendMsg(msg.getBytes());
						sendMsgList.remove(msg);
						// 鎴愬姛娑堟伅锛岄�杩噃ander鍥炰紶
						if (handler != null)
						{
							Message message =  new Message();
							message.obj = msg.getBytes();
							message.what =1;
						   handler.sendMessage(message);
						//	handler.sendEmptyMessage(1);
						}
						
					} catch (Exception e)
					{
						e.printStackTrace();
						CLog.e(tag, e.toString());
						// 閿欒娑堟伅锛岄�杩噃ander鍥炰紶
						if (handler != null)
						{
							Message message =  new Message();
							message.obj = msg.getBytes();
							message.what = 0;;
						    handler.sendMessage(message);
						
						}
					}
				}
			}
			
			synchronized (this)
			{
				try
				{
					wait();
					
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 鍙戦�瀹屾秷鎭悗锛岀嚎绋嬭繘鍏ョ瓑寰呯姸鎬�
			}
		}
		
	}
}
