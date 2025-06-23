package dao.sql;

public final class AutoLoanSql {
    private AutoLoanSql() {

    }

    public static final String INSERT = """
            INSERT INTO bank.auto_loan (user_id, loan_amount, current_loan_amount, loan_term)
            VALUES (?, ?, ?, ?)
            """;

    public static final String SELECT_ALL = """
            SELECT a.id,
                   a.loan_amount,
                   a.current_loan_amount,
                   a.loan_term,
                   s.passport_number
            FROM bank.auto_loan a
            JOIN bank.bank_user s ON a.user_id = s.id
            """;

    public static final String SELECT_BY_ID = """
            SELECT a.id,
                   a.loan_amount,
                   a.current_loan_amount,
                   a.loan_term,
                   u.passport_number
            FROM bank.auto_loan a
            JOIN bank.bank_user u ON a.user_id = u.id
            WHERE a.id = ?
            """;

    public static final String UPDATE = """
            UPDATE bank.auto_loan
            SET user_id = ?, loan_amount = ?, current_loan_amount = ?, loan_term = ?
            WHERE id = ?
            """;

    public static final String DELETE = "DELETE FROM bank.auto_loan WHERE id = ?";
}
