package water.ustc.dao;

import org.xml.sax.SAXException;
import sc.ustc.dao.BaseDao;
import sc.ustc.dao.Configuration;
import sc.ustc.dao.Conversation;
import sc.ustc.items.JDBCItems.BaseBean;
import water.ustc.bean.UserBean;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao extends BaseDao {
    public UserDao(){
        /*url ="jdbc:mysql://localhost:3306/pcm?serverTimezone=GMT";
        driver = "com.mysql.jdbc.Driver";
        user = "root";
        password = "123456";*/

        /*url ="jdbc:sqlite:d:PCM/Desktop/pcm.db";
        driver ="org.sqlite.JDBC";*/
        try {
            Configuration configuration = new Configuration();
            driver = configuration.getDriver();
            url = configuration.getUrl();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public UserBean query(String name) {
        try {
            BaseBean bb  = Conversation.getObject(new UserBean(name));
            String password = bb.getUserPass();
            UserBean ub = new UserBean(bb.getUserName(),bb.getUserPass());
            return ub;
        } catch (ClassNotFoundException | ParserConfigurationException | SAXException | IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        /*UserBean user = null;
        try {
            //statement.execute
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                user = new UserBean();
                user.setUserId(resultSet.getString("userId"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPass(resultSet.getString("userPass"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;*/
    }

    @Override
    public boolean insert(String name) {
        try {
            return Conversation.insert(new UserBean(name,password));
        } catch (ClassNotFoundException | ParserConfigurationException | SAXException | IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        //return executeUpdate(s);
    }

    @Override
    public boolean update(String name) {
        try {
            return Conversation.update(new UserBean(name,password));
        } catch (ClassNotFoundException | ParserConfigurationException | SAXException | IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        //return executeUpdate(s);
    }

    @Override
    public boolean delete(String userName) {
        try {
            return Conversation.deleteObject(new UserBean(userName));
        } catch (ClassNotFoundException | ParserConfigurationException | SAXException | IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
//		String sql = "delete from "+TABLE_NAME+" where "+TABLE_USER_NAME+" like '"+name+"'";
//		System.out.println(sql);
//		try {
//			statement.executeUpdate(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
    }

    private boolean executeUpdate(String s){
        boolean result = false;
        try {
            result = statement.execute(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Statement getStatement(){
        return statement;
    }
}
