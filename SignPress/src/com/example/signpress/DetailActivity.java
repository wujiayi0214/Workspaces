package com.example.signpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 


import signdata.Employee;
import signdata.HDJContract;
import signdata.SignatureDetail;
import signdata.SignatureTemplate;
import signsocket.SocketClient;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity  {
	
	private Button btnAgree;
	private Button btnRefuse;
	
	private EditText Remarks;
	
	private AppContext app;
	private String contractId;
	
	private  String Title[] = new String[]{};// 标题数据
    private  String contents[] = new String[]{};//内容数据
    private ListView listView = null;
    ArrayList<Map<String,Object>> list = null;
	
    private List<String> titleList;
    private List<String> contentList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		TextView tvAndroid=(TextView)findViewById(R.id.tvCWJ);
		tvAndroid.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		this.btnAgree = (Button)findViewById(R.id.btnAgree);
		this.btnRefuse=(Button)findViewById(R.id.btnRefuse);
		this.Remarks=(EditText)findViewById(R.id.tvCWJ);
		
		titleList=new ArrayList<String>();
		contentList=new ArrayList<String>();
		titleList.add("会签单名称：");
		titleList.add("编号：");
		
		app = (AppContext)getApplication();
		contractId = app.getContractId();
		
		HDJContract contract = new HDJContract();
		contract=SocketClient.instance().GetHDJContract(contractId);
		for(String s : contract.ConTemp.ColumnNames)
		{
			titleList.add(s+"：");
		}
		for(SignatureTemplate s : contract.ConTemp.SignDatas)
		{
			titleList.add(s.SignInfo+"：");
		}
		contentList.add(contract.Name);
		contentList.add(contractId);
		
		for(String s:contract.ColumnDatas)
		{
			contentList.add(s);
		}
		
		for(int i=0;i<contract.ConTemp.SignDatas.size();i++)
		{
			String name=contract.ConTemp.SignDatas.get(i).SignEmployee.Name;
			String result=contract.SignResults.get(i)==1?"同意":(contract.SignResults.get(i)==0?"未处理":"拒绝");
			contentList.add(name+"("+result+")");
		}
		
		Title=new String[titleList.size()];
		for(int i=0;i<titleList.size();i++)
        {
			Title[i]=titleList.get(i);
        }
		
		contents=new String[contentList.size()];
		for(int i=0;i<contentList.size();i++)
		{
			contents[i]=contentList.get(i);
		}
		
		listView = (ListView) this.findViewById(R.id.ContractDetails);
        list = new ArrayList<Map<String, Object>>();// 实例化list
        for (int i = 0; i < Title.length; i++) 
        {// for循环向list中增加数据
            Map<String,Object> map = new HashMap<String,Object>();// 创建map对象
            map.put("title", Title[i]);
            map.put("content", contents[i]);
            list.add(map);// 将map数据增加到list中去
        }
        listView.setAdapter(new SimpleAdapter(DetailActivity.this, // 上下文对象
                list,// List数据
                R.layout.list_item_style,// ListView中数据的显示方式
                new String[] { "title", "content" },// 此处的String数据必须与List当中的key值对应
                new int[] { android.R.id.text1, android.R.id.text2 }));// android.R.layout.simple_list_item_2中提供的文本控件
        // android. R.id.text1,android. R.id.text2 这两个属性是由
        // android.R.layout.simple_list_item_2这个系统布局提供的，大家可以尝试里面其他控件的使用
//      为listView中的数据项设置单击事件
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int psition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(DetailActivity.this, "您选择了" +Title[psition]+ contents[psition],
                        Toast.LENGTH_SHORT).show();//土司提示
            }
        });
        
        //点击同意按钮事件
        this.btnAgree.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				AgreeAsyncTask();
			}
			
			private void AgreeAsyncTask()
			{
		        new AsyncTask<String, Void, Object>()
		        {
		          
		          //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
		          // 后台的计算结果将通过该方法传递到UI 线程，并且在界面上展示给用户.
		          protected void onPostExecute(Object result)
		          {
		            super.onPostExecute(result);
		            //activity_main_btn1.setText("请求结果为："+result);//可以更新UI
		        	//  获取备注中的信息
					String remarks=Remarks.getText().toString();

					if(remarks.equals(""))
					{
						remarks="未填";
					}
					
	
					// 手机能联网，读socket数据
					//if (NetManager.instance().isNetworkConnected())
					{
						SignatureDetail sd = new SignatureDetail();
						sd.Remark=remarks;
						sd.Result=1;
						sd.ConId=contractId;
						
						app=(AppContext)getApplication();
				        final Employee emp = app.getEmployee();
						
				        sd.EmpId=emp.Id;
						boolean re=SocketClient.instance().InsertSignatureDetail(sd);
						if(re)
						{
							Toast.makeText(DetailActivity.this, "签字成功", Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							// 使用弹窗，告诉用户没有签字权限无法登录
							Toast.makeText(DetailActivity.this, "签字失败", Toast.LENGTH_SHORT).show();
						}
					
		              }
		          }

		          //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
		          protected Object doInBackground(String... params)
		          {
						 return true;
		          }

		        }.execute();
		        
		      }
		});

        //点击拒绝按钮事件
        this.btnRefuse.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				RefuseAsyncTask();
			}
			
			private void RefuseAsyncTask()
			{
		        new AsyncTask<String, Void, Object>()
		        {
		          
		          //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
		          // 后台的计算结果将通过该方法传递到UI 线程，并且在界面上展示给用户.
		          protected void onPostExecute(Object result)
		          {
		            super.onPostExecute(result);
		            //activity_main_btn1.setText("请求结果为："+result);//可以更新UI
		        	//  获取备注中的信息
					String remarks=Remarks.getText().toString();

					if(remarks.equals(""))
					{
						Toast.makeText(DetailActivity.this, "需填写拒绝理由", Toast.LENGTH_SHORT).show();
					}
					
	
					// 手机能联网，读socket数据
					//if (NetManager.instance().isNetworkConnected())
					else
					{
						SignatureDetail sd = new SignatureDetail();
						sd.Remark=remarks;
						sd.Result=-1;
						sd.ConId=contractId;
						
						app=(AppContext)getApplication();
				        final Employee emp = app.getEmployee();
						
				        sd.EmpId=emp.Id;
						boolean re=SocketClient.instance().InsertSignatureDetail(sd);
						if(re)
						{
							Toast.makeText(DetailActivity.this, "签字成功", Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							// 使用弹窗，告诉用户没有签字权限无法登录
							Toast.makeText(DetailActivity.this, "签字失败", Toast.LENGTH_SHORT).show();
						}
					
		              }
		          }

		          //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
		          protected Object doInBackground(String... params)
		          {
						 return true;
		          }

		        }.execute();
		        
		      }
		});
	}

}
