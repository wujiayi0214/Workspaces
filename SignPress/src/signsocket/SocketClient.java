package signsocket;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;




public class SocketClient
{
	// 设置服务器IP和端口
    public static Socket m_socket = null;
    
    //private static PrintWriter  _printWriter    = null;         
    
    //public static InputStream in = null;
    
    //public static BufferedReader br=null;
    
    public static DataOutputStream out=null;
    public static DataInputStream in=null;
    //public static BufferedReader inbuff=null;
    private static final String SERVER_IP   = "10.0.51.141"; //"192.168.1.200";
    private static final int    SERVER_PORT      = 6666;//7777;
    
    public static Boolean create()
    {        

        try
        {
        	m_socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(m_socket.getInputStream());
            out = new DataOutputStream(m_socket.getOutputStream());    
                    
                    
            //inbuff=new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            /*System.out.println("socket  1");        
            in=_socket.getInputStream();
            br=new BufferedReader(new InputStreamReader(in));
            
            System.out.println("socket  2");        
            _printWriter=new PrintWriter(SocketHP._socket.getOutputStream(), true); 
            System.out.println("socket  3");        */
                
            return true;        
        

        } 
        catch (IOException e) 
        {
        	System.out.println(e.toString());        
            
        	return false;
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

    public static void sendMessage(String message)
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
}

