package dao.sql;

public final class UserSql {
    private UserSql() {

    }

    public static final String SELECT_ALL = "SELECT * FROM bank.bank_user";

    public static final String SELECT_BY_ID = "SELECT * FROM bank.bank_user WHERE id = ?";

    public static final String SELECT_BY_PASSPORT_NUMBER = "SELECT * FROM bank.bank_user WHERE passport_number = ?";

    public static final String COUNT_BY_PASSPORT_NUMBER = "SELECT COUNT(*) FROM bank.bank_user WHERE passport_number = ?";

    public static final String INSERT = """
        INSERT INTO bank.bank_user (passport_number, username, birthdate, gender)
        VALUES (?, ?, ?, ?)
    """;

    public static final String UPDATE = """
        UPDATE bank.bank_user
        SET username = ?, birthdate = ?, gender = ?, passport_number = ?
        WHERE id = ?
    """;

    public static final String DELETE = "DELETE FROM bank.bank_user WHERE id = ?";
}
