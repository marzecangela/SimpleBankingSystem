import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Jdbc {
    final SQLiteDataSource dataSource;

    Jdbc(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    void init() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {

//                statement.executeUpdate("DROP TABLE IF EXISTS card;");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewAccount(Account account) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement stm = con.createStatement()) {
                stm.executeUpdate("INSERT INTO card (number, pin, balance) " +
                        "VALUES(\'" + account.getNumber() + "\', \'" +
                        account.getPin() + "\', " + account.getBalance() + ");");
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {

        }
    }

    public Account findAccount(String number, String pin) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement stm = con.createStatement()) {
                ResultSet res = stm.executeQuery("SELECT * FROM card WHERE number = "
                        + number + " AND pin = " + pin + ";");
                if (res.next()) {
                    return new Account(res.getString("number"), res.getString("pin"), res.getInt("balance"));
                }
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public void updateAccountBalance(Account account) {
        String update = "UPDATE card SET balance = ? WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement stm = con.prepareStatement(update)) {
                stm.setInt(1, account.getBalance());
                stm.setString(2, account.getNumber());
                stm.executeUpdate();
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {
        }
    }

    public int getBalance(Account account) {
        String select = "SELECT balance FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement pstm = con.prepareStatement(select)) {
                pstm.setString(1, account.getNumber());
                ResultSet res = pstm.executeQuery();
                if (res.next()) {
                    return res.getInt("balance");
                }
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {

        }
        return 0;
    }

    public void closeAccount(Account account) {
        String delete = "DELETE FROM card WHERE number = ? AND pin = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement pstm = con.prepareStatement(delete)) {
                pstm.setString(1, account.getNumber());
                pstm.setString(2, account.getPin());
                pstm.executeUpdate();
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {

        }
    }

    public boolean findTransferAccount(String numberAccount) {
        String select = "SELECT * FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement pstm = con.prepareStatement(select)) {
                pstm.setString(1, numberAccount);
                ResultSet res = pstm.executeQuery();
                if (res.next()) {
                    return true;
                }
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean isMoneyEnough(int money, Account account) {
        String select = "SELECT balance FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement pstm = con.prepareStatement(select)) {
                pstm.setString(1, account.getNumber());
                ResultSet res = pstm.executeQuery();
                if (res.next()) {
                    if (res.getInt("balance") >= money) {
                        return true;
                    }
                }
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {
        }
        return false;
    }

    void transfer(Account account, String transferNumber, int money) {
        String update1 = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String update2 = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement add = con.prepareStatement(update2);
                 PreparedStatement subtract = con.prepareStatement(update1)) {

                add.setInt(1, money);
                add.setString(2, transferNumber);
                add.executeUpdate();

                subtract.setInt(1, money);
                subtract.setString(2, account.getNumber());
                subtract.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
        } catch (SQLException e) {

        }
    }
}