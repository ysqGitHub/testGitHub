package com.yanshiqian.gpstest;

// import this package for using the configure values or other such as mysql-username etc.
// if you want to use the value in this package, just like that: Configure.DRIVER  --(className.valueName)
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MysqlConnecter {
    /**
     * -------------
     * # if you want to connect mysql, you should go to com.teamghz.configure.MysqlConnecter.java to edit information
     * --------------
     * # insert/update -> int update(String sql) : "sql" is what you want to execute
     * # return a integer, when 0 -> false; when other(n) success and this operation affect n lines
     * --------------
     * # delete        -> int delete(String sql) : "sql" is what you want to execute
     * # return a integer, when 0 -> false; when other(n) success and this operation affect n lines
     * --------------
     * # query         -> ArrayList<Map<String, String>> select(String sql, String tableName) : 
     *                                      "sql" is what you want to execute
     *                                      "tableName" is the table name which you want to operate
     * # return a ArrayList, the elements in the ArrayList is Map<String, String>
     * # every Map is one query result
     * # when you need to use the data returned:
     * ArrayList<Map<String, String>> result = mc.select("select * from User", "User");
     *  for (Map<String, String> map : result) {
     *      System.out.println("______________________");
     *      for(Map.Entry<String, String> entry:map.entrySet()){    
     *            System.out.println(entry.getKey()+"--->"+entry.getValue());    
     *      }   
     *  }
     * --------------
     * *--------------
     * *#如果你想连接mysql，你应该去com.teamghz.configure.MysqlConne..java编辑信息
     * *--------------------
     * *插入/更新-> int更新（String SQL）：“SQL”是您想要执行的
     * *#返回一个整数，当0->false时；当其他（n）成功且此操作影响n行时
     * *--------------------
     * *>删除-> int删除（字符串SQL）：“SQL”是您想要执行的
     * *#返回一个整数，当0->false时；当其他（n）成功且此操作影响n行时
     * *--------------------
     * *#query->ArrayList<Map<String,String>select(String sql,String tableName)：
     * *“SQL”是您想要执行的
     * *“tableName”是您想要操作的表名
     * *返回一个数组，数组中的元素是map < string，string >
     * *每一个地图都是一个查询结果
     * *当您需要使用返回的数据时：
     * *ArrayList<Map<String,String> result=mc.select(“select*from User”、“User”)；
     * *（map < string，string > map：结果）{
     * ＊系统。
     * *（map .cc:string，String >条目：Map .CtrySyt（））{
     * *系统.out .pPRTLN（条目.GETKEY（）+“->”+条目.GETValueE（）；
     * *}
     * *}
	*/
    private Connection connection = null;
    private boolean connected = false;

    public MysqlConnecter() {
        try {
            Class.forName(Configure.DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR AT MysqlConnecter");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(Configure.URL, Configure.USERNAME, Configure.PASSWORD);
            connected = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//插入操作
    public int insert(String sql)  {  
        int lineNum = 0;
        if (!connected) return 0;
        try{  
            PreparedStatement preStmt = connection.prepareStatement(sql);    
            lineNum = preStmt.executeUpdate();  
        }  
        catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return lineNum; 
    }
    /*
//更新操作
    public int update(String sql){  
        int lineNum = 0;
        if (!connected) return 0;
        try{  
            PreparedStatement preStmt = connection.prepareStatement(sql);   
            lineNum = preStmt.executeUpdate();  
        }  
        catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return lineNum;
    }  */
//查找操作
    public ArrayList<Map<String, String>> select(String sql, String tableName)
    {   
        ArrayList<Map<String, String>> result = new ArrayList<>();

        try  
        {  
            Statement stmt = connection.createStatement();  
            ResultSet rs = stmt.executeQuery(sql);
            int count=1;
            while (rs.next()){  	
                Map<String, String> tmp = new HashMap<>();
                tmp.put(String.valueOf(count), rs.getString(tableName));
                count++;
              result.add(tmp);
            }              
        }  
        catch (SQLException e){  
            e.printStackTrace();  
        }  
        return result;  
    }
 //删除操作
    public int delete(String sql){   
        int lineNum = 0;    
        try  {  
            Statement stmt = connection.createStatement();  
            lineNum = stmt.executeUpdate(sql);  
        }  
        catch (SQLException e){  
            e.printStackTrace();  
        }  
        return lineNum;  
    }  
    // ��ȡ��ǰ��Ĺؼ��֣������ַ����������ʽ���أ��硰username������id����
    private String[] getFrame(String tableName) {
        String[] result = new String[Configure.TABLELEN];
         try  
            {  
                Statement stmt = connection.createStatement();  
                ResultSet rs = stmt.executeQuery("show columns from " + tableName);
                int i = 0;
                while (rs.next()){  
                    result[i++] = rs.getString(1);
                }
                result[i] = "#";
            }  
            catch (SQLException e){  
                e.printStackTrace();  
            }  
        return result;
    }
}