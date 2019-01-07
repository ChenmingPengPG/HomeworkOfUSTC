package water.ustc.dao;

import sc.ustc.dao.BaseDao;
import water.ustc.bean.UserBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao extends BaseDao {
    public UserDao(){
        url ="jdbc:sqlite://c:/sqlite/PCM.db";
        driver ="org.sqlite.JDBC";
    }

    @Override
    public UserBean query(String sql) {
        UserBean user = null;
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
        return user;
    }

    @Override
    public boolean insert(String s) {
        return executeUpdate(s);
    }

    @Override
    public boolean update(String s) {
        return executeUpdate(s);
    }

    @Override
    public boolean delete(String s) {
        return executeUpdate(s);
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
