package com.example.signpress;

import java.util.ArrayList;
import java.util.List;

import signdata.Employee;
import signdata.HDJContract;
import signdata.SHDJContract;
import signsocket.SocketClient;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 

public class TestActivity extends Activity {
    private AppContext app;
    
    private ArrayList<SHDJContract> unsignedList;
    private List<SHDJContract> signedList;
    
    private List<String> unsignedStrs=new ArrayList<String>();
    private List<String> signedStrs=new ArrayList<String>();
    
    private String[] t;
    private String[] r;
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_test);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        
        app=(AppContext)getApplication();
        final Employee emp = app.getEmployee();
        unsignedList = SocketClient.instance().QueryUnsignedHDJContract(emp.Id);
        signedList=SocketClient.instance().QuerySignedHDJContract(emp.Id);
        
        for(SHDJContract s : unsignedList)
        {
        	String str = s.Id+" "+s.ProjectName+" "+s.SubmitEmployeeName;
        	unsignedStrs.add(str);
        }
        
        if(unsignedStrs==null)
        {
        	t= new String[]{};
        }
        else
        {
         //t=(String[])unsignedStrs.toArray( );
         /*
          * ArrayList<String> list=new ArrayList<String>();
           String strings[]=new String[list.size()];
           for(int i=0,j=list.size();i<j;i++){
           strings[i]=list.get(i);
            }
          */
         t=new String[unsignedStrs.size()];
         for(int i=0;i<unsignedStrs.size();i++)
         {
        	 t[i]=unsignedStrs.get(i);
         }
         
        }
        
        for(SHDJContract s : signedList)
        {
        	String str = s.Id+" "+s.ProjectName+" "+s.SubmitEmployeeName;
        	signedStrs.add(str);
        }
        
        if(signedStrs==null)
        {
        	r= new String[]{};
        }
        else
        {
         //t=(String[])unsignedStrs.toArray( );
         /*
          * ArrayList<String> list=new ArrayList<String>();
           String strings[]=new String[list.size()];
           for(int i=0,j=list.size();i<j;i++){
           strings[i]=list.get(i);
            }
          */
         r=new String[signedStrs.size()];
         for(int i=0;i<signedStrs.size();i++)
         {
        	 r[i]=signedStrs.get(i);
         }
         
        }
        final ExpandableListAdapter adapter = new BaseExpandableListAdapter()
        {
            //设置组视图的显示文字
            private String[] generalsTypes = new String[] { "需要签字", "已经签字" };
            //子视图显示文字    
            private String[][] generals = new String[][] {
            		t,
                    r,

            };
            
            //自己定义一个获得文字信息的方法
            TextView getTextView() {
                @SuppressWarnings("deprecation")
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 64);
                TextView textView = new TextView(
                		TestActivity.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(36, 0, 0, 0);
                textView.setTextSize(20);
                textView.setTextColor(Color.BLACK);
                return textView;
            }

            
            //重写ExpandableListAdapter中的各个方法
            @Override
            public int getGroupCount() {
                // TODO Auto-generated method stub
                return generalsTypes.length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                // TODO Auto-generated method stub
                return generalsTypes[groupPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                // TODO Auto-generated method stub
                return groupPosition;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition].length;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition][childPosition];
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                    View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                LinearLayout ll = new LinearLayout(
                		TestActivity.this);
                ll.setOrientation(0);
                TextView textView = getTextView();
                textView.setTextColor(Color.BLACK);
                textView.setText(getGroup(groupPosition).toString());
                textView.setPadding(50, 0, 0, 0);
                ll.addView(textView);

                return ll;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition,
                    boolean isLastChild, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                LinearLayout ll = new LinearLayout(
                		TestActivity.this);
                ll.setOrientation(0);
                TextView textView = getTextView();
                textView.setText(getChild(groupPosition, childPosition)
                        .toString());
                textView.setHeight(100);
                ll.addView(textView);
                return ll;
            }

            @Override
            public boolean isChildSelectable(int groupPosition,
                    int childPosition) {
                // TODO Auto-generated method stub
                return true;
            }

        };

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.list);
        expandableListView.setAdapter(adapter);
        
        
        //设置item点击的监听器
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {

                Toast.makeText(
                		TestActivity.this,
                        "你点击了" + adapter.getChild(groupPosition, childPosition),
                        Toast.LENGTH_SHORT).show();
                
                if(groupPosition==0)
                {
	                HDJContract contract = new HDJContract();
	                contract.Id=adapter.getChild(groupPosition, childPosition).toString().split(" ")[0];
	                
	                //app.setHDJContract(contract);
					app.setContractId(contract.Id);
					
	                Intent intent = new Intent();  
					//  设置Intent的class属性Test跳转到SecondActivity  
					intent.setClass(TestActivity.this, DetailActivity.class);  
					
					//  为intent添加额外的信息  
					//  启动Activity  
					startActivity(intent);  
                }

                return false;
            }
        });
    }

}
