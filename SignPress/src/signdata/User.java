package signdata;

public class User 
{
	public User(String username, String password)
	{
		this.Username = username;
		this.Password = password;
	}
	
	/// 为了与C#的客户端接口通用，我们将属性成员设为PUBLIC
	/// 这样我们就可以与C#使用相同的服务器接口
	//  用户名
	public String Username;
	
	// 密码
	public String Password;

	
	


}
