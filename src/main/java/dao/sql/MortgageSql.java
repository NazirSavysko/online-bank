package dao.sql;

public final class MortgageSql {

    private MortgageSql() {
    }

    public static final String SELECT_ALL = """
                SELECT m.id,
                    m.mortgage_amount,
                    m.current_mortgage_amount,
                    m.mortgage_term,
                    u.passport_number
                FROM mortgage m
                JOIN bank.bank_user u ON m.user_id = u.id
            """;

    public static final String SELECT_BY_ID = """
                SELECT m.id,
                    m.mortgage_amount,
                    m.current_mortgage_amount,
                    m.mortgage_term,
                    u.passport_number
                FROM mortgage m
                JOIN bank.bank_user u ON m.user_id = u.id
                WHERE m.id = ?
            """;

    public static final String INSERT = """
                INSERT INTO mortgage (
                    user_id = ?,
                    mortgage_amount,
                    current_mortgage_amount,
                    mortgage_term
                ) VALUES (?, ?, ?, ?)
            """;

    public static final String UPDATE = """
                UPDATE mortgage
                SET user_id = ?,
                    mortgage_amount = ?,
                    current_mortgage_amount = ?,
                    mortgage_term = ?
                WHERE id = ?
            """;

    public static final String DELETE = """
                DELETE FROM mortgage
                WHERE id = ?
            """;
}
