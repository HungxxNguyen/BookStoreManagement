
package hungng.orders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import hungng.utils.DBHelpers;

public class OrdersDAO implements Serializable{
    public int createNewOrder(String name, String address, String total) 
        throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into Orders(Name, Address, Total) "
                        + "Output inserted.OrderID, inserted.Date "
                        + "Values (?, ?, ?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, name);
                stm.setString(2, address);
                stm.setBigDecimal(3, new BigDecimal(total));
                rs = stm.executeQuery();
                if (rs.next()) {
                    int orderID = rs.getInt("OrderID");
                    return orderID;
                }
            } //end if con connect success
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return -1;
    }
    
    public OrdersDTO getOrders(int orderID) 
        throws NamingException, SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select Name, Address, Date, Total "
                        + "From Orders "
                        + "Where orderID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, orderID);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String name = rs.getNString("Name");
                    String address = rs.getNString("Address");
                    Timestamp date = rs.getTimestamp("Date");
                    BigDecimal total = rs.getBigDecimal("Total");
                    
                    OrdersDTO dto = new 
                        OrdersDTO(orderID, name, address, date, total);
                    
                    return dto;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
}
