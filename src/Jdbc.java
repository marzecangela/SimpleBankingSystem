import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc {
    final SQLiteDataSource dataSource;

    Jdbc(SQLiteDataSource dataSource){
        this.dataSource = dataSource;
    }
    void init(){
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {

//                statement.executeUpdate("DROP TABLE IF EXISTS card;"); //need to pass stage 3 (several tests)
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewAccount(Account account){
        try(Connection con = dataSource.getConnection()){
            try(Statement stm = con.createStatement()){
                stm.executeUpdate("INSERT INTO card (number, pin, balance) " +
                        "VALUES(\"" + account.getNumber() + "\", \"" +
                        account.getPin() + "\", " + account.getBalance()  +");");
            }
        }catch(SQLException e){
        }
    }

    public Account findAccount(String number, String pin){
        try(Connection con = dataSource.getConnection()){
            try(Statement stm = con.createStatement()){
                ResultSet res = stm.executeQuery("SELECT * FROM card WHERE number = "
                        + number + " AND pin = " + pin + ";");
                if(res.next()){
                    return new Account(res.getString("number"), res.getString("pin"), res.getInt("balance"));
                }
            }
        }catch (SQLException e){
        }
        return null;
    }
}