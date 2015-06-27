package com.example.signpress;

//import android.support.v7.app.ActionBarActivity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;





import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;




import android.app.Activity;
import android.content.Intent;
import signapp.AppContext;
import signdata.User;
import signsocket.SocketClient;


//@SuppressWarnings("deprecation")
public class LoginActivity extends Activity 
{
	
	//  用户名输入框
	private EditText editTextUsername;
	
	// 密码输入框
	private EditText editTextPassword;

	// 登录按钮
	private Button buttonLogin;
	
	// 取消按钮
	private Button buttonCancle;
    
	
	private PrintWriter output ;
    
	
	
	//private SocketClient m_socketClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		//  设置布局文件为activity_login.xml
		
		// 得到两个EditText对象
		//this.editTextUsername = (EditText)findViewById(R.id.editTextUsername);
		//this.editTextPassword = (EditText)findViewById(R.id.editTextPassword);
		
		// 得到两个button对象
		//this.buttonLogin = (Button)findViewById(R.id.buttonLogin);
		//this.buttonCancle = (Button)findViewById(R.id.buttonCancle);
		
/*
		//this.m_socketClient = new SocketClient();
		SocketClient.create();				// 创建套接字
		
		// 为登录按钮绑定单击事件
		this.buttonLogin.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					//  获取文本框中的用户名和密码
					String username = editTextUsername.getText().toString();
					String password = editTextPassword.getText().toString();
					

					User user = new User(username, password);
					AppContext app =  new AppContext();
					app.setUser(user);
					
					
				} 
				catch (Exception e) 
				{
					
				}
			}
		});
		*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/*********启动客户端方法***********/
    public void Connect()
    {
            
       try 
       {
           InetAddress addr = InetAddress.getByName("10.0.213.117");//服务端手机网络IP地址，连一下wifi就可以知道
           System.out.println("客户端发出请求");
                 
           //客 户端向服务端发出连接请求
           Socket socket = new Socket(addr,6666);
           System.out.println("连接成功，socket=" + socket);
               
           //  通过该条socket通道得到输出流
           output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
           System.out.println("输出流获取成功");
           output.println(editTextUsername.getEditableText().toString());
           output.flush();
             

            
           /****/
           Intent intentdump =new Intent();
           intentdump.putExtra("username", editTextUsername.getText().toString());
           intentdump.putExtra("password", editTextPassword.getText().toString());
           //intentdump.setClass(LoginActivity.this, MainActivity.class);
           
           LoginActivity.this.startActivity(intentdump);
       }
       catch (Exception e) 
       {
           e.printStackTrace();
       }
             
    }
}
