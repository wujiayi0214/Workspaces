package signsocket;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import signdata.Employee;
import signdata.HDJContract;
import signdata.SHDJContract;
import signdata.SignatureDetail;
import signdata.User;

/*
Socket 提供了getInputStream()和getOutputStream()用来得到输入流和输出流进行读写操作，
这两个方法分别返回InputStream和OutputStream。
为了方便读写，我们常常在InputStream和OutputStream基础上进行包装得到
DataInputStream, DataOutputStream, 
PrintStream, InputStreamReader, 
OutputStreamWriter, printWriter等。

示例代码：

PrintStream printStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), true)));

printWriter.println(String msg);

DataInputStream dis = new DataInputStream(socket.getInputStream());

BufferedReader br =  new BufferedReader(new InputStreamReader(socket.getInputStream()));

String line = br.readLine();
*/
public class SocketClient
{
	// 设置服务器IP和端口
    public static Socket m_socket = null;
    public static SocketClient socketClient = null;
    private static PrintWriter  m_printWriter    = null;         
        
    public static BufferedReader m_buffer=null;
    public static byte[] m_recvBuffer;
    public static DataOutputStream out=null;
    public static DataInputStream in=null;
    //public static BufferedReader inbuff=null;
    private static final String SERVER_IP   = "192.168.253.1"; //"192.168.1.200";
    private static final int    SERVER_PORT      = 6666;//7777;
    
	public static synchronized SocketClient instance()
	{
		if (socketClient == null)
		{
			socketClient = new SocketClient();
		}
		
		return socketClient;
	}
	
	public SocketClient()
	{
		try
		{
			initialize();
			
		} catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}


    public static void close()
    {
    	try
    	{
    		//_printWriter.close();
    		out.close();
            in.close();                
            m_socket.close();
            if(m_socket.isClosed())
            	System.out.println("socket is closed...");        
                
        }        
        catch (Exception e)
    	{
            e.printStackTrace();
            System.out.println("socket isn't closed...");                
        }
    }

    public void sendMessage(String message)
    {
        try
        {
            out.writeBytes(message);
            out.flush();
        }
        catch (Exception e)
        {
        	System.out.println(e.getStackTrace());        

        }
    }
    
    // 处理丢包问题的接收函数
    public int receiveMessage() throws IOException
    {
		int len = in.read(m_recvBuffer, 0, 1024 * 1024);
		
		return len;  
    }

    
	public void  initialize() throws IOException
	{
		m_socket = new Socket( );
        m_socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);              //inbuff=new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        m_recvBuffer = new byte[1024*1024]; 
    	//m_socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
        in = new DataInputStream(m_socket.getInputStream());
        out = new DataOutputStream(m_socket.getOutputStream());    

        //  发送数据
        PrintStream m_printWriter = new PrintStream(m_socket.getOutputStream()); //发送数据,PrintStream最方便
        //m_printWriter.write(message.getBytes());
        // 接收返回信息
        BufferedReader m_buffer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));; //一次性接收完成读取Socket的输入流，在其中读出返回信息

	}
	
	public Employee loginRequest(User user)
	{
		Employee employee = new Employee();
		try
		{
			//  发送登录请求以及数据
			SocketMessage message = new SocketMessage(ClientRequest.LOGIN_REQUEST, user);
			
			out.write(message.Package.getBytes("utf-8"));
			out.flush();
			
			in.read(m_recvBuffer, 0, 1024 * 1024);
			
			System.out.println(m_recvBuffer);
			message.Package = new String(m_recvBuffer).trim();
			message.Split();   //  将数据进行拆包
			
			if(message.Head.equals(ServerResponse.LOGIN_SUCCESS.toString()))
			{			//  数据头是登录成功
				Gson gson = new Gson();
				employee = gson.fromJson(message.Message, Employee.class);
				
			}
			else
			{
				employee.Id = -1;
			}
				
		}
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return employee;
	}
	
	// 查询未签字的信息
	public ArrayList<SHDJContract> QueryUnsignedHDJContract(int employeeId)
	{
		ArrayList<SHDJContract> contracts = new ArrayList<SHDJContract>();
	    Type type = new TypeToken<ArrayList<SHDJContract>>(){}.getType(); 
		try
		{
			//  发送查询为签字的会签单的信息以及员工的ID
			SocketMessage message = new SocketMessage(ClientRequest.QUERY_UNSIGN_CONTRACT_REQUEST, employeeId);
			
			out.write(message.Package.getBytes("utf-8"));
			out.flush();

			in.read(m_recvBuffer, 0, 1024 * 1024);
			
			System.out.println(m_recvBuffer);
			message.Package = new String(m_recvBuffer).trim();
			message.Split();   //  将数据进行拆包
			
			if(message.Head.equals(ServerResponse.QUERY_UNSIGN_CONTRACT_SUCCESS.toString()))
			{			
				//  数据头是查询成功
				Gson gson = new Gson();

				contracts = gson.fromJson(message.Message, type);	
				return contracts;

			}
			else
			{
				return null;
			}

				
		}
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return contracts;	
	}
	
	// 查询已经签字的会签单信息
	public ArrayList<SHDJContract> QuerySignedHDJContract(int employeeId)
	{
		ArrayList<SHDJContract> contracts = new ArrayList<SHDJContract>();
	    Type type = new TypeToken<ArrayList<SHDJContract>>(){}.getType(); 
		try
		{
			//  发送查询为签字的会签单的信息以及员工的ID
			SocketMessage message = new SocketMessage(ClientRequest.QUERY_SIGNED_CONTRACT_REQUEST, employeeId);
			
			out.write(message.Package.getBytes("utf-8"));
			out.flush();
			
			in.read(m_recvBuffer, 0, 1024 * 1024);
			
			System.out.println(m_recvBuffer);
			message.Package = new String(m_recvBuffer).trim();
			message.Split();   //  将数据进行拆包
			
			if(message.Head.equals(ServerResponse.QUERY_SIGNED_CONTRACT_SUCCESS.toString()))
			{			
				//  数据头是查询成功
				Gson gson = new Gson();
				contracts = gson.fromJson(message.Message, type);				
			}
			else
			{
				return null;
			}

				
		}
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return contracts;	
	}
	
	// 查询某个会签单的详细信息
	public HDJContract GetHDJContract(String contractId)
	{
		HDJContract contract = new HDJContract();
		try
		{
			//  发送查询为签字的会签单的信息以及员工的ID
			SocketMessage message = new SocketMessage(ClientRequest.GET_HDJCONTRACT_REQUEST, contractId);
			
			out.write(message.Package.getBytes("utf-8"));
			out.flush();
			
			in.read(m_recvBuffer, 0, 1024 * 1024);
			
			System.out.println(m_recvBuffer);
			message.Package = new String(m_recvBuffer).trim();
			message.Split();   //  将数据进行拆包
			
			if(message.Head.equals(ServerResponse.GET_HDJCONTRACT_SUCCESS.toString()))
			{			
				//  数据头是查询成功
				Gson gson = new Gson();
				contract = gson.fromJson(message.Message, HDJContract.class);				
			}
			else
			{
				return null;
			}

				
		}
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return contract;	
	}
	
	// 进行签字确认
	public boolean InsertSignatureDetail(SignatureDetail detail)
	{
		try
		{
			//  发送查询为签字的会签单的信息以及员工的ID
			SocketMessage message = new SocketMessage(ClientRequest.INSERT_SIGN_DETAIL_REQUEST, detail);
			
			out.write(message.Package.getBytes("utf-8"));
			out.flush();
			
			in.read(m_recvBuffer, 0, 1024 * 1024);
			
			System.out.println(m_recvBuffer);
			message.Package = new String(m_recvBuffer).trim();
			message.Split();   //  将数据进行拆包
			
			if(message.Head.equals(ServerResponse.INSERT_SIGN_DETAIL_SUCCESS.toString()))
			{			
				//  数据头是查询成功
				return true;
			}
			else
			{
				return false;
			}

				
		}
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;	
	}
    

}

