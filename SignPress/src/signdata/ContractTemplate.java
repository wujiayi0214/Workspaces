package signdata;


import java.util.List;
public class ContractTemplate
{
    public String CreateDate;
    
    public  int TempId;
    
    public String Name;
    

    public int ColumnCount = 5;

    protected List<String> ColumnNames;     //  �洢5����Ŀ�����Ϣ
        
    public int SignCount = 8;

    public List<SignatureTemplate> SignDatas;

}
