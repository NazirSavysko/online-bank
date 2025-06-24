package dao.sql;

public final class DebitCardSql {
    private DebitCardSql() {

    }


    public static final String SELECT_ALL = """
                SELECT d.id,
                       d.card_number,
                       d.expiration_date,
                       d.issue_date,
                       d.cvv,
                       d.balance,
                       u.passport_number
                FROM debit_card d
                JOIN bank_user u ON d.user_id = u.id
            """;

    public static final String CHECK_CARD_NUMBER = """
                SELECT COUNT(*) FROM bank.debit_card WHERE card_number = ?
            """;

    public static final String INSERT = """
                INSERT INTO bank.debit_card (
                    card_number,
                    user_id,
                    expiration_date,
                    issue_date,
                    cvv,
                    balance
                ) VALUES (?, ?, ?, ?, ?, ?)
            """;

    public static final String UPDATE = """
                UPDATE bank.debit_card
                SET card_number = ?,
                    expiration_date = ?,
                    issue_date = ?,
                    cvv = ?,
                    balance = ?,
                    user_id = ?
                WHERE id = ?
            """;

    public static final String DELETE = """
                DELETE FROM bank.debit_card WHERE id = ?
            """;

    public  static final String SELECT_BY_ID = """
                SELECT d.id,
                       d.card_number,
                       d.expiration_date,
                       d.issue_date,
                       d.cvv,
                       d.balance,
                       u.passport_number
                FROM debit_card d
                JOIN bank_user u ON d.user_id = u.id
                WHERE d.id = ?
    """;
}
