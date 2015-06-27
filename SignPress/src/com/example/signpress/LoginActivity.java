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
	
	//  �û��������
	private EditText editTextUsername;
	
	// ���������
	private EditText editTextPassword;

	// ��¼��ť
	private Button buttonLogin;
	
	// ȡ����ť
	private Button buttonCancle;
    
	
	private PrintWriter output ;
    
	
	
	//private SocketClient m_socketClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		//  ���ò����ļ�Ϊactivity_login.xml
		
		// �õ�����EditText����
		//this.editTextUsername = (EditText)findViewById(R.id.editTextUsername);
		//this.editTextPassword = (EditText)findViewById(R.id.editTextPassword);
		
		// �õ�����button����
		//this.buttonLogin = (Button)findViewById(R.id.buttonLogin);
		//this.buttonCancle = (Button)findViewById(R.id.buttonCancle);
		
/*
		//this.m_socketClient = new SocketClient();
		SocketClient.create();				// �����׽���
		
		// Ϊ��¼��ť�󶨵����¼�
		this.buttonLogin.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					//  ��ȡ�ı����е��û���������
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
	
	
	
	/*********�����ͻ��˷���***********/
    public void Connect()
    {
            
       try 
       {
           InetAddress addr = InetAddress.getByName("10.0.213.117");//������ֻ�����IP��ַ����һ��wifi�Ϳ���֪��
           System.out.println("�ͻ��˷�������");
                 
           //�� ���������˷�����������
           Socket socket = new Socket(addr,6666);
           System.out.println("���ӳɹ���socket=" + socket);
               
           //  ͨ������socketͨ���õ������
           output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
           System.out.println("�������ȡ�ɹ�");
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
