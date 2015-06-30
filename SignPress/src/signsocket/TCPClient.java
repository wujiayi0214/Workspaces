package signsocket;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


/**
 * NIO TCP 瀹㈡埛绔�
 * 
 */
public class TCPClient
{
	// 淇￠亾閫夋嫨鍣�
	private Selector selector;
	
	// 涓庢湇鍔″櫒閫氫俊鐨勪俊閬�
	SocketChannel socketChannel;
	
	// 瑕佽繛鎺ョ殑鏈嶅姟鍣↖p鍦板潃
	private String hostIp;
	
	// 瑕佽繛鎺ョ殑杩滅▼鏈嶅姟鍣ㄥ湪鐩戝惉鐨勭鍙�
	private int hostListenningPort;
	
	private static TCPClient s_Tcp = null;
	
	public boolean isInitialized = false;
	
	public static synchronized TCPClient instance()
	{
		if (s_Tcp == null)
		{
			
			s_Tcp = new TCPClient(Const.SOCKET_SERVER,
					Const.SOCKET_PORT);
		}
		return s_Tcp;
	}
	
	/**
	 * 鏋勯�鍑芥暟
	 * 
	 * @param HostIp
	 * @param HostListenningPort
	 * @throws IOException
	 */
	public TCPClient(String HostIp, int HostListenningPort)
	{
		this.hostIp = HostIp;
		this.hostListenningPort = HostListenningPort;
		
		try
		{
			initialize();
			this.isInitialized = true;
		} catch (IOException e)
		{
			this.isInitialized = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			this.isInitialized = false;
			e.printStackTrace();
		}
	}
	
	/**
	 * 鍒濆鍖�
	 * 
	 * @throws IOException
	 */
	public void initialize() throws IOException
	{
		boolean done = false;
		
		try
		{
			// 鎵撳紑鐩戝惉淇￠亾骞惰缃负闈為樆濉炴ā寮�
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
					hostListenningPort));
			if (socketChannel != null)
			{
				socketChannel.socket().setTcpNoDelay(false);
				socketChannel.socket().setKeepAlive(true);
				// 璁剧疆 璇籹ocket鐨則imeout鏃堕棿
				socketChannel.socket().setSoTimeout(
						Const.SOCKET_READ_TIMOUT);
				socketChannel.configureBlocking(false);
				
				// 鎵撳紑骞舵敞鍐岄�鎷╁櫒鍒颁俊閬�
				selector = Selector.open();
				if (selector != null)
				{
					socketChannel.register(selector, SelectionKey.OP_READ);
					done = true;
				}
			}
		} finally
		{
			if (!done && selector != null)
			{
				selector.close();
			}
			if (!done)
			{
				socketChannel.close();
			}
		}
	}
	
	static void blockUntil(SelectionKey key, long timeout) throws IOException
	{
		
		int nkeys = 0;
		if (timeout > 0)
		{
			nkeys = key.selector().select(timeout);
			
		} else if (timeout == 0)
		{
			nkeys = key.selector().selectNow();
		}
		
		if (nkeys == 0)
		{
			throw new SocketTimeoutException();
		}
	}
	
	/**
	 * 鍙戦�瀛楃涓插埌鏈嶅姟鍣�
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException
	{
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("utf-8"));
		
		if (socketChannel == null)
		{
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}
	
	/**
	 * 鍙戦�鏁版嵁
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	public void sendMsg(byte[] bytes) throws IOException
	{
		ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);
		
		if (socketChannel == null)
		{
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized Selector getSelector()
	{
		return this.selector;
	}
	
	/**
	 * Socket杩炴帴鏄惁鏄甯哥殑
	 * 
	 * @return
	 */
	public boolean isConnect()
	{
		boolean isConnect = false;
		if (this.isInitialized)
		{
			isConnect =  this.socketChannel.isConnected();
		}
		return isConnect;
	}
	
	/**
	 * 鍏抽棴socket 閲嶆柊杩炴帴
	 * 
	 * @return
	 */
	public boolean reConnect()
	{
		closeTCPSocket();
		
		try
		{
			initialize();
			isInitialized = true;
		} catch (IOException e)
		{
			isInitialized = false;
			e.printStackTrace();
		}
		catch (Exception e)
		{
			isInitialized = false;
			e.printStackTrace();
		}
		return isInitialized;
	}
	
	/**
	 * 鏈嶅姟鍣ㄦ槸鍚﹀叧闂紝閫氳繃鍙戦�涓�釜socket淇℃伅
	 * 
	 * @return
	 */
	public boolean canConnectToServer()
	{
		try
		{
			if (socketChannel != null)
			{
				socketChannel.socket().sendUrgentData(0xff);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 鍏抽棴socket
	 */
	public void closeTCPSocket()
	{
		try
		{
			if (socketChannel != null)
			{
				socketChannel.close();
			}
			
		} catch (IOException e)
		{
			
		}
		try
		{
			if (selector != null)
			{
				selector.close();
			}
		} catch (IOException e)
		{
		}
	}
	
	/**
	 * 姣忔璇诲畬鏁版嵁鍚庯紝闇�閲嶆柊娉ㄥ唽selector锛岃鍙栨暟鎹�
	 */
	public synchronized void repareRead()
	{
		if (socketChannel != null)
		{
			try
			{
				selector = Selector.open();
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e)
			{
				e.printStackTrace();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
