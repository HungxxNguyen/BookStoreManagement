
package hungng.orderdetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import hungng.product.ProductDTO;
import hungng.utils.DBHelpers;


public class OrderDetailsDAO implements Serializable {

    private List<OrderDetailsDTO> orderDetailsList;

    public List<OrderDetailsDTO> getOrderDetailsList() {
        return orderDetailsList;
    }

    public boolean createOrderDetails(int orderID, Map<ProductDTO, Integer> checkedItems)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                con.setAutoCommit(false); //*****
                String sql = "Insert Into OrderDetails"
                        + "(OrderID, SKU, Name, Price, Quantity, Total) "
                        + "Values (?, ?, ?, ?, ?, ?)";
                stm = con.prepareStatement(sql);
                BigDecimal price;
                BigDecimal total;
                int quantity;
//                int i = 0; //*****
                int affectedRows =  0;
                for (ProductDTO dto : checkedItems.keySet()) {
                    quantity = checkedItems.get(dto);
                    price = dto.getPrice();
                    total = price.multiply(new BigDecimal(quantity));
                    stm.setInt(1, orderID);
                    stm.setString(2, dto.getSKU());
                    stm.setString(3, dto.getName());
                    stm.setBigDecimal(4, price);
                    stm.setInt(5, quantity);
                    stm.setBigDecimal(6, total);
                    affectedRows += stm.executeUpdate(); //*****
//                    stm.addBatch(); //******
//                    i++;//******
                }
                
                con.commit(); //*****
                
                if (affectedRows == checkedItems.size()) { //*****
                    return true; 
                }
                
//                if (i % 100 == 0 || i == checkedItems.size()) {
//                    stm.executeBatch();
//                    return true;
//                }
            }
        } catch (SQLException ex) {
            if (con != null) {
                con.rollback();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDetailsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }
}
