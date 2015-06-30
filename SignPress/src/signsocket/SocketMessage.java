package signsocket;
import signsocket.CLog.CLogType;

import com.google.gson.Gson;

public class SocketMessage 
{
	public String Head;
	public String Message;
	public String Package;
	//
	// 序列化： 
	//　　JsonConvert.SerializeObject（string）； 
	//　　反序列化： 
	//　　JsonConvert.DeserializeObject（obj）； 
	/*
	 * Java可以用开源项目google-gson，
	 * 在项目中导入这个项目的第三方jar包，
	 * 然后添加引用：import com.google.gson.Gson；
	 * 就可使用以下方法： 
	 * Gson gson = new Gson();
序列化： 
　　Gson gson=new Gson（）； 
　　String s=gson.toJson（obj）； 
反序列化： 
　　Gson gson=new Gson（）； 
　　Object obj=gson.fromJson（s，Object.class）； */
	public SocketMessage(ClientRequest request, Object obj)
	{
		Gson gson = new Gson();
		String str = gson.toJson(obj);
		this.Package = request.toString() + ";" + str.length() + ";" + str;
		//CLog.w(Package, str);
		CLog.out(Package);
	}
}
