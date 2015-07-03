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
	
	private  String Title[] = new String[]{};// ��������
    private  String contents[] = new String[]{};//��������
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
		titleList.add("��ǩ�����ƣ�");
		titleList.add("��ţ�");
		
		app = (AppContext)getApplication();
		contractId = app.getContractId();
		
		HDJContract contract = new HDJContract();
		contract=SocketClient.instance().GetHDJContract(contractId);
		for(String s : contract.ConTemp.ColumnNames)
		{
			titleList.add(s+"��");
		}
		for(SignatureTemplate s : contract.ConTemp.SignDatas)
		{
			titleList.add(s.SignInfo+"��");
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
			String result=contract.SignResults.get(i)==1?"ͬ��":(contract.SignResults.get(i)==0?"δ����":"�ܾ�");
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
        list = new ArrayList<Map<String, Object>>();// ʵ����list
        for (int i = 0; i < Title.length; i++) 
        {// forѭ����list����������
            Map<String,Object> map = new HashMap<String,Object>();// ����map����
            map.put("title", Title[i]);
            map.put("content", contents[i]);
            list.add(map);// ��map�������ӵ�list��ȥ
        }
        listView.setAdapter(new SimpleAdapter(DetailActivity.this, // �����Ķ���
                list,// List����
                R.layout.list_item_style,// ListView�����ݵ���ʾ��ʽ
                new String[] { "title", "content" },// �˴���String���ݱ�����List���е�keyֵ��Ӧ
                new int[] { android.R.id.text1, android.R.id.text2 }));// android.R.layout.simple_list_item_2���ṩ���ı��ؼ�
        // android. R.id.text1,android. R.id.text2 ��������������
        // android.R.layout.simple_list_item_2���ϵͳ�����ṩ�ģ���ҿ��Գ������������ؼ���ʹ��
//      ΪlistView�е����������õ����¼�
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int psition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(DetailActivity.this, "��ѡ����" +Title[psition]+ contents[psition],
                        Toast.LENGTH_SHORT).show();//��˾��ʾ
            }
        });
        
        //���ͬ�ⰴť�¼�
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
		          
		          //��doInBackground ִ����ɺ�onPostExecute ��������UI �̵߳��ã�
		          // ��̨�ļ�������ͨ���÷������ݵ�UI �̣߳������ڽ�����չʾ���û�.
		          protected void onPostExecute(Object result)
		          {
		            super.onPostExecute(result);
		            //activity_main_btn1.setText("������Ϊ��"+result);//���Ը���UI
		        	//  ��ȡ��ע�е���Ϣ
					String remarks=Remarks.getText().toString();

					if(remarks.equals(""))
					{
						remarks="δ��";
					}
					
	
					// �ֻ�����������socket����
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
							Toast.makeText(DetailActivity.this, "ǩ�ֳɹ�", Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							// ʹ�õ����������û�û��ǩ��Ȩ���޷���¼
							Toast.makeText(DetailActivity.this, "ǩ��ʧ��", Toast.LENGTH_SHORT).show();
						}
					
		              }
		          }

		          //�÷��������ں�̨�߳��У���˲����ڸ��߳��и���UI��UI�߳�Ϊ���߳�
		          protected Object doInBackground(String... params)
		          {
						 return true;
		          }

		        }.execute();
		        
		      }
		});

        //����ܾ���ť�¼�
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
		          
		          //��doInBackground ִ����ɺ�onPostExecute ��������UI �̵߳��ã�
		          // ��̨�ļ�������ͨ���÷������ݵ�UI �̣߳������ڽ�����չʾ���û�.
		          protected void onPostExecute(Object result)
		          {
		            super.onPostExecute(result);
		            //activity_main_btn1.setText("������Ϊ��"+result);//���Ը���UI
		        	//  ��ȡ��ע�е���Ϣ
					String remarks=Remarks.getText().toString();

					if(remarks.equals(""))
					{
						Toast.makeText(DetailActivity.this, "����д�ܾ�����", Toast.LENGTH_SHORT).show();
					}
					
	
					// �ֻ�����������socket����
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
							Toast.makeText(DetailActivity.this, "ǩ�ֳɹ�", Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							// ʹ�õ����������û�û��ǩ��Ȩ���޷���¼
							Toast.makeText(DetailActivity.this, "ǩ��ʧ��", Toast.LENGTH_SHORT).show();
						}
					
		              }
		          }

		          //�÷��������ں�̨�߳��У���˲����ڸ��߳��и���UI��UI�߳�Ϊ���߳�
		          protected Object doInBackground(String... params)
		          {
						 return true;
		          }

		        }.execute();
		        
		      }
		});
	}

}
