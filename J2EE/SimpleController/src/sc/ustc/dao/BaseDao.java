package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDao  {
    protected String driver;
    protected String url;
    protected String user;
    protected String password;

    protected Connection connection;
    protected Statement statement;

    public Connection openDBConnection(){
        try {
            Class.forName(driver);
            //connection = DriverManager.getConnection(url);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();// 获取执行sql语句的statement对象
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean closeDBConnection(){
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setDriver(String driver){
        this.driver = driver;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setUser(String user){
        this.user = user;
    }
    public void setPassword(String password){
        this.password = password;
    }
    abstract public Object query(String sql);
    abstract public boolean insert(String sql);
    abstract public boolean update(String sql);
    abstract public boolean delete(String sql);
}
