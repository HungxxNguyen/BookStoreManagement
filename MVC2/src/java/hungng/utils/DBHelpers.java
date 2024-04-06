
package hungng.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DBHelpers implements Serializable {
    public static Connection makeConnection() 
        throws /*ClassNotFoundException, SQLException*/ NamingException, SQLException, ClassNotFoundException{
        
        Connection con=null;
        String IP="localhost";
        String instanceName="LAPTOP-2KM49M7D\\HUNGNGUYEN";
        String port="1433";
        String uid="sa";
        String pwd="12345";
        String db="PRJ301_MVC2";
        String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url="jdbc:sqlserver://" +IP+"\\"+ instanceName+":"+port
                 +";databasename="+db+";user="+uid+";password="+pwd;
        Class.forName(driver);
        con=DriverManager.getConnection(url);
        return con;
}
}
