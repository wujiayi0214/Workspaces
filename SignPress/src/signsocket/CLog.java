package signsocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * <b>绫诲姛鑳芥弿杩帮細</b>
 * 鏃ュ織绠＄悊宸ュ叿(闈炲绾跨▼...浼氶樆濉�..) </div>
 * 
 * @author
 * @version 1.0 </p> 淇敼鏃堕棿锛�/br> 淇敼澶囨敞锛�/br>
 */
public class CLog
{
	private static boolean enable = true;// 鎬婚椄
	private static boolean writetosd = true;// 璁板緱鍔犲啓SD鍗℃潈闄�
	private static boolean showLogInfo = false;
	private static boolean printout = true;
	private static String LogTag = CLog.class.getSimpleName();
	private static String packageName = "com.onevo";// 淇敼鍖呭悕,
	private static int maxSize = 1000;// KB
	private static File folder = new File("/sdcard/"
			+ CLog.class.getSimpleName());
	private static File save = new File(folder.getPath() + "//"
			+ "SaveString.txt");
	private static File log = new File(folder.getPath() + "//" + "Log.txt");
	private static File temp = new File(folder.getPath() + "//" + "Temp.txt");
	private static final String FORMATSTR = "yyyy-MM-dd hh:mm:ss";
	private static int move = 1;
	
	/**
	 * <b>绫诲姛鑳芥弿杩帮細</b><div style="margin-left:40px;margin-top:-10px">
	 * 鏃ュ織杈撳嚭绫诲瀷,涓庝紶缁烲og涓�嚧(V,D,I,W,E)</br> 褰撶被鍨嬩负E鏃�鏃ュ織鏂囦欢璁板綍绫诲瀷涓篍rror,鍏朵粬涓篒nfos </div>
	 * 
	 * @author
	 * @version 1.0 </p> 淇敼鏃堕棿锛�/br> 淇敼澶囨敞锛�/br>
	 */
	public enum CLogType
	{
		D(0), W(1), E(2), I(3), V(4);
		private int value;
		
		CLogType(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
	/*--姝ゅ伐鍏风被鏃犻渶瀹炰緥鍖�-*/
	private CLog()
	{
	}
	
	/**
	 * 淇濆瓨瀛楃涓插埌鏂囨湰(寤鸿淇℃伅鏃堕噺澶ф椂浣跨敤)<br/>
	 * 
	 * @param toSave
	 *            娆蹭繚瀛樼殑鏂囨湰
	 * @return true-鎴愬姛 </br>false-澶辫触
	 */
	public static boolean saveStringToTxt(String toSave)
	{
		if (!enable)
		{
			return false;
		}
		DateFormat sdf = new SimpleDateFormat(FORMATSTR);
		String t = sdf.format(new Date());
		StackTraceElement element = new Exception().getStackTrace()[1];
		StringBuilder sb = new StringBuilder();
		sb.append("\n-------------------- ");
		sb.append(t);
		sb.append(" --------------------");
		sb.append(element.getFileName() == null ? "" : "\nCaller:\n"
				+ element.getFileName().substring(0,
						element.getFileName().indexOf(".")) + "_"
				+ element.getMethodName() + ":" + element.getLineNumber());
		sb.append("\nString:\n");
		sb.append(toSave);
		sb.append("\n--------------------------   End   --------------------------");
		return save(save, sb.toString());
	}
	
	/**
	 * LogCat涓樉绀烘柟娉曠殑璋冪敤鑰呯浉鍏充俊鎭�
	 * 
	 * @return 鏂规硶璋冪敤鑰呯被涓庢柟娉曞悕
	 */
	public static String showCallerInfo()
	{
		if (!enable)
		{
			return "";
		}
		StackTraceElement element = new Exception().getStackTrace()[2];
		move = 2;
		if (element.getClassName().indexOf(packageName) != -1)
		{
			out(CLogType.D, "<-Callee    鈫機aller,Double Click Goto Source");
		} else
		{
			out(CLogType.D, "<-Callee    鈫機aller(Nonnative)");
		}
		move = 2;
		out(CLogType.D,
				"at "
						+ element.getClassName()
						+ "."
						+ element.getMethodName()
						+ (element.getFileName() == null ? "(UnKnow Source)"
								: ("(" + element.getFileName() + ":"
										+ element.getLineNumber() + ")")));
		return element.getClassName() + "." + element.getMethodName();
	};
	
	/**
	 * 鎺у埗鍙癓ogCat杈撳嚭淇℃伅(涓嶆斁鍏ユ棩蹇楁枃浠�
	 */
	public static void out(Object infos)
	{
		move = 2;
		if (infos == null)
		{
			infos = "";
		}
		out(CLogType.D, infos.toString());
	}
	
	/**
	 * 鎺у埗鍙癓ogCat杈撳嚭淇℃伅(涓嶆斁鍏ユ棩蹇楁枃浠�
	 */
	public static void out(CLogType logType, Object infos)
	{
		if (!enable)
		{
			return;
		}
		if (!printout)
		{
			return;
		}
		StackTraceElement element = new Exception().getStackTrace()[move];
		move = 1;
		String tag = LogTag;
		if (element.getFileName() != null)
		{
			tag = LogTag
					+ "_"
					+ element.getFileName().substring(0,
							element.getFileName().indexOf(".")) + "_"
					+ element.getMethodName() + ":" + element.getLineNumber();
		}
		if (logType == CLogType.D)
		{
			Log.d(tag, infos.toString());
		} else if (logType == CLogType.W)
		{
			Log.w(tag, infos.toString());
		} else if (logType == CLogType.I)
		{
			Log.i(tag, infos.toString());
		} else if (logType == CLogType.E)
		{
			Log.e(tag, infos.toString());
		} else if (logType == CLogType.V)
		{
			Log.v(tag, infos.toString());
		}
	}
	
	/**
	 * 淇濇寔涓庝紶缁烲og涓�嚧鐨勪娇鐢�
	 * 
	 * @param tag
	 *            鏍囧織
	 * @param infos
	 *            淇℃伅
	 */
	public static void d(String tag, Object infos)
	{
		if (!enable)
		{
			return;
		}
		Log.d(tag, infos.toString());
	}
	
	/**
	 * 淇濇寔涓庝紶缁烲og涓�嚧鐨勪娇鐢�
	 * 
	 * @param tag
	 *            鏍囧織
	 * @param infos
	 *            淇℃伅
	 */
	public static void e(String tag, Object infos)
	{
		if (!enable)
		{
			return;
		}
		Log.e(tag, infos.toString());
	}
	
	/**
	 * 淇濇寔涓庝紶缁烲og涓�嚧鐨勪娇鐢�
	 * 
	 * @param tag
	 *            鏍囧織
	 * @param infos
	 *            淇℃伅
	 */
	public static void v(String tag, Object infos)
	{
		if (!enable)
		{
			return;
		}
		Log.v(tag, infos.toString());
	}
	
	/**
	 * 淇濇寔涓庝紶缁烲og涓�嚧鐨勪娇鐢�
	 * 
	 * @param tag
	 *            鏍囧織
	 * @param infos
	 *            淇℃伅
	 */
	public static void w(String tag, Object infos)
	{
		if (!enable)
		{
			return;
		}
		Log.w(tag, infos.toString());
	}
	
	/**
	 * 淇濇寔涓庝紶缁烲og涓�嚧鐨勪娇鐢�
	 * 
	 * @param tag
	 *            鏍囧織
	 * @param infos
	 *            淇℃伅
	 */
	public static void i(String tag, Object infos)
	{
		if (!enable)
		{
			return;
		}
		
		if (infos == null)
		{
			Log.i(tag, "null");
		} else
		{
			Log.i(tag, infos.toString());
		}
		
	}
	
	/**
	 * 鎺у埗鍙癓ogCat杈撳嚭淇℃伅(鍚屾椂淇濆瓨鍒版棩蹇楁枃浠�<br/>
	 */
	public static void record(Object infos)
	{
		move = 2;
		out(CLogType.D, infos.toString());
		saveInfoToLog(infos.toString(), false);
	}
	
	/**
	 * 鎺у埗鍙癓ogCat杈撳嚭淇℃伅(鍚屾椂淇濆瓨鍒版棩蹇楁枃浠�<br/>
	 */
	public static void record(CLogType logType, Object infos)
	{
		move = 2;
		out(logType, infos.toString());
		saveInfoToLog(infos.toString(), logType == CLogType.E);
	}
	
	/**
	 * 淇濆瓨淇℃伅鍒版棩蹇楁枃浠�
	 * 
	 * @param infos
	 *            淇℃伅
	 * @param error
	 *            鏄惁浣跨敤閿欒淇℃伅鏍囧織 </br> 鏍囧織涓簍rue-[Error] or false-[Infos]
	 */
	public static void write(Object infos, boolean error)
	{
		saveInfoToLog(infos.toString(), error);
	}
	
	/*--淇濆瓨淇℃伅鍒版棩蹇楁枃浠�-*/
	private static boolean saveInfoToLog(String infos, boolean error)
	{
		DateFormat sdf = new SimpleDateFormat(FORMATSTR);
		String t = sdf.format(new Date());
		infos = t + (error ? "  [Error]  " : "  [Infos]  ") + infos;
		return save(log, infos);
	}
	
	/*--鎶藉彇鍑虹殑鍐欏叆鏂囦欢鏂规硶--*/
	private static boolean save(File f, String s)
	{
		if (!(writetosd && enable))
		{
			return true;
		}
		boolean result = true;
		FileInputStream input = null;
		BufferedInputStream inBuff = null;
		FileOutputStream output = null;
		BufferedOutputStream outBuff = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try
		{
			folder.mkdirs();
			/*--鑾峰彇鍘熸枃浠朵互淇濆瓨鐨勪俊鎭�-*/
			if (f.exists())
			{
				input = new FileInputStream(f);
				inBuff = new BufferedInputStream(input);
			}
			/*--End--*/
			/*--鍐欏叆鏂扮殑淇℃伅--*/
			fw = new FileWriter(temp);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			if (f == log)
			{
				pw.write(s.replace("\n", "\n                              ")
						+ "\n");
			} else
			{
				pw.write(s + "\n");
			}
			/*--End--*/
			try
			{
				if (temp.exists())
				{
					pw.close();
					bw.close();
					fw.close();
				}
			} catch (Exception e)
			{
				if (showLogInfo)
					out("Exception Occur!");
				result = false;
			}
			/*--杩藉姞鍘熸湁鏂囦欢淇℃伅--*/
			int currentSize = (int) ((temp.length() / 1024) + 1);
			if (f.exists())
			{
				output = new FileOutputStream(temp, true);
				outBuff = new BufferedOutputStream(output);
				byte[] b = new byte[1024];
				int len;
				while ((len = inBuff.read(b)) != -1)
				{
					currentSize++;
					if (currentSize > maxSize)
						break;
					outBuff.write(b, 0, len);
				}
			}
			/*--End--*/
			move = 4;
			if (showLogInfo)
				out(CLogType.D, ("Successfully to Save " + (f == log ? "Log"
						: "String")));
		} catch (Exception e)
		{
			move = 4;
			if (showLogInfo)
				out(CLogType.D, ("Successfully to Save " + (f == log ? "Log"
						: "String")));
			result = false;
		} finally
		{
			try
			{
				if (f.exists())
				{
					outBuff.flush();
					inBuff.close();
					outBuff.close();
					output.close();
					input.close();
				}
			} catch (Exception e)
			{
				if (showLogInfo)
					out("Exception Occur!");
				result = false;
			}
		}
		return result && temp.renameTo(f);
	}
	
	/*--閫掑綊鍒犻櫎鏃ュ織鏂囦欢澶�-*/
	private static void deleteFolder(File f)
	{
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile())
			{
				files[i].delete();
			} else
			{
				deleteFolder(files[i]);
			}
		}
		folder.delete();
	}
	
	/**
	 * 鏃ュ織鍔熻兘鏄惁宸插惎鐢�
	 * 
	 * @return true-鍚敤 <br/>
	 *         false-鍏抽棴
	 */
	public static boolean isEnable()
	{
		return enable;
	}
	
	/**
	 * 璁剧疆鏃ュ織鍔熻兘寮�叧
	 * 
	 * @param enable
	 *            true-鍚敤 or false-鍏抽棴
	 */
	public static void setEnable(boolean enable)
	{
		CLog.enable = enable;
		if (!enable && folder.exists())
		{
			deleteFolder(folder);
		}
	}
	
	/**
	 * 鏃ュ織鍔熻兘鏄惁鑳藉皢淇℃伅鍐欏叆鏂囦欢
	 * 
	 * @return true-鑳�</br> false-涓嶈兘
	 */
	public static boolean canWritetosd()
	{
		return writetosd;
	}
	
	/**
	 * 璁剧疆鏃ュ織鍔熻兘鍐欐枃浠跺紑鍏�
	 * 
	 * @param writetosd
	 *            enable true-鍚敤 or false-鍏抽棴
	 */
	public static void setWriteToSD(boolean writetosd)
	{
		CLog.writetosd = writetosd;
	}
	
	/**
	 * 鏃ュ織鍔熻兘鏄惁鏄剧ず鐩稿叧鏃ュ織鎿嶄綔淇℃伅
	 * 
	 * @return true-鏄�</br> false-鍚�
	 */
	public static boolean isShowLogInfo()
	{
		return showLogInfo;
	}
	
	/**
	 * 璁剧疆鏃ュ織鍔熻兘璁剧疆鏄惁鏄剧ず鐩稿叧鏃ュ織鎿嶄綔淇℃伅
	 * 
	 * @param showLogInfo
	 */
	public static void setShowLogInfo(boolean showLogInfo)
	{
		CLog.showLogInfo = showLogInfo;
	}
	
	/**
	 * 鏄惁鍏佽杈撳嚭淇℃伅鍒癓ogCat
	 * 
	 * @return true-鏄�</br> false-鍚�
	 */
	public static boolean isPrintout()
	{
		return printout;
	}
	
	/**
	 * 鏄惁鍏佽杈撳嚭淇℃伅鍒癓ogCat
	 * 
	 * @param printout
	 */
	public static void setPrintout(boolean printout)
	{
		CLog.printout = printout;
	}
	
	/**
	 * 鏃ュ織鍔熻兘鍙啓鍏ョ殑鏈�ぇ鏂囦欢澶у皬
	 * 
	 * @return N kb
	 */
	public static int getMaxSize()
	{
		return maxSize;
	}
	
	/**
	 * 璁剧疆鏃ュ織鍔熻兘璁剧疆鍙啓鍏ョ殑鏈�ぇ鏂囦欢澶у皬
	 * 
	 * @param maxSize
	 *            N kb
	 */
	public static void setMaxSize(int maxSize)
	{
		CLog.maxSize = maxSize;
	}
	
	/**
	 * 褰撳墠鐢ㄦ潵鍖归厤鐨勯」鐩寘鍚�
	 * 
	 * @return
	 */
	public static String getPackageName()
	{
		return packageName;
	}
	
	/**
	 * 璁剧疆鐢ㄦ潵鍖归厤褰撳墠椤圭洰鐨勫寘鍚�
	 * 
	 * @param packageName
	 */
	public static void setPackageName(String packageName)
	{
		CLog.packageName = packageName;
	}
	
}
