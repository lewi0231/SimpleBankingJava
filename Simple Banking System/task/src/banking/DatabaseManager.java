package banking;

import org.sqlite.*;

import java.sql.*;

public class DatabaseManager {
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();

    public static void initializeTable() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE card(id INTEGER, " +
                        "number TEXT, " +
                        "pin TEXT, " +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                System.out.println("Problem with statement");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAccount(Account account) {
        try (Connection con = dataSource.getConnection()) {
            String newRow = "INSERT INTO card VALUES(?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(newRow)) {
                statement.setInt(1, account.getId());
                statement.setString(2, account.getCardNumber());
                statement.setString(3, account.getPin());
                statement.setInt(4, account.getBalance());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFilename(String filename) {
        String url = "jdbc:sqlite:" + filename;
        dataSource.setUrl(url);
    }


    public static ResultSet queryDatabase(String query) {
        ResultSet result = null;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                result = statement.executeQuery(query);

            } catch (SQLException e) {
                System.out.println("Problem with statement");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void transferAmountTransaction(int amount, Account origin, Account destination) {
        String update = "UPDATE \"card\" SET \"balance\" = ? WHERE \"number\" = ?";
        Savepoint savepoint = null;
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement statement = con.prepareStatement(update)) {
                savepoint = con.setSavepoint();

                statement.setInt(1, origin.getBalance());
                statement.setString(2, origin.cardNumber);
                statement.executeUpdate();

                PreparedStatement statement2 = con.prepareStatement(update);

                statement2.setInt(1, destination.getBalance());
                statement2.setString(2, destination.cardNumber);
                statement2.executeUpdate();
                con.commit();

            } catch (SQLException e) {
                try {
                    con.rollback(savepoint);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Account getAccount(String otherCard) {
        String selectStatement = "SELECT * FROM card WHERE number = " + otherCard;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet result = statement.executeQuery(selectStatement);

                if (result.next()) {
                    int id = result.getInt("id");
                    String card = result.getString("number");
                    String pin = result.getString("pin");
                    int balance = result.getInt("balance");
                    return new Account(id, card, pin, balance);
                } else {
                    return null;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAccount(Account customerAccount) {
        String deleteStatement = "DELETE FROM card WHERE id = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(deleteStatement)) {
                statement.setInt(1, customerAccount.getId());
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateAccountBalance(Account customerAccount) {
        String update = "UPDATE \"card\" SET \"balance\" = ? WHERE \"number\" = ?";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(update)) {
                statement.setInt(1, customerAccount.getBalance());
                statement.setString(2, customerAccount.cardNumber);
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
