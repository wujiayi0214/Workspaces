package signapp;

import signdata.Employee;
import signdata.User;

import signsocket.SocketClient;
import android.app.Application;

/* 多个activity之间共享数据结构使用该类通信  
 * http://www.cnblogs.com/wangsx/archive/2012/05/23/2514772.html
 * 
 * 
 * */
public class AppContext extends Application
{
	// 用于通信的套接字结构体
	private SocketClient m_socketClient;				
	public SocketClient getSocketClient(SocketClient socketClient)
	{
		
		return this.m_socketClient;
	}
	public void SetSocketClient(SocketClient client)
	{
		this.m_socketClient = client;
	}
	
	// 用户信息
	private Employee m_employee;
	public Employee getEmployee()
	{
			return this.m_employee;
	}
	public void setEmployee(Employee employee)
	{
			this.m_employee = employee;
	}
	
	// 登录请求信息
	private User m_user;
	public User getUser()
	{
		return this.m_user;
	}
	public void setUser(User user)
	{
		this.m_user = user;
	}
}
