package signapp;

import signdata.Employee;
import signdata.User;

import signsocket.SocketClient;
import android.app.Application;

/* ���activity֮�乲�����ݽṹʹ�ø���ͨ��  
 * http://www.cnblogs.com/wangsx/archive/2012/05/23/2514772.html
 * 
 * 
 * */
public class AppContext extends Application
{
	// ����ͨ�ŵ��׽��ֽṹ��
	private SocketClient m_socketClient;				
	public SocketClient getSocketClient(SocketClient socketClient)
	{
		
		return this.m_socketClient;
	}
	public void SetSocketClient(SocketClient client)
	{
		this.m_socketClient = client;
	}
	
	// �û���Ϣ
	private Employee m_employee;
	public Employee getEmployee()
	{
			return this.m_employee;
	}
	public void setEmployee(Employee employee)
	{
			this.m_employee = employee;
	}
	
	// ��¼������Ϣ
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
