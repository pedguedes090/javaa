package Ex03;

import utils.DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Types;

public class Main {
    //P1:Phải dùng registerOutParameter
//DECIMAL → Types.DECIMAL
    static void main() {
        Scanner sc = new Scanner(System.in);
        String sql = "{CALL proc_GET_SURGERY_FEE (?, ?)}";

        try(Connection con= DB.getConnection();
            CallableStatement cstm = con.prepareCall(sql);
        ){
            cstm.setInt(1, sc.nextInt());
            cstm.registerOutParameter(2, Types.DECIMAL);
            cstm.execute();
            double cost = cstm.getDouble(2);
            System.out.println("Chi phí phẫu thuật: " + cost);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
