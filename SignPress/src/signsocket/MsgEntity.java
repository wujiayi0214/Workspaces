package signsocket;

import android.os.Handler;


/**
 * 瀛樺偍鍙戦�socket鐨勭被锛屽寘鍚鍙戦�鐨凚ufTest锛屼互鍙婂搴旂殑杩斿洖缁撴灉鐨凥andler
 * @author Administrator
 *
 */
public class MsgEntity
{
	//瑕佸彂閫佺殑娑堟伅
	private byte [] bytes;
	//閿欒澶勭悊鐨刪andler
	private Handler mHandler;
	
	public MsgEntity( byte [] bytes, Handler handler)
	{	
		 this.bytes = bytes;
		 mHandler = handler;
	}
	
	public byte []  getBytes()
	{
		return this.bytes;
	}
	
	public Handler getHandler()
	{
		return mHandler;
	}

}
