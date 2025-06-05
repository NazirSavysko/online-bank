package entity;

import java.time.LocalDate;

public final class DebitCard {
    private String cardNumber;
    private String cardHolderPassportId;
    private LocalDate expirationDate;
    private LocalDate issueDate;
    private String cvv;
    private int balance;

    public DebitCard(
            final String cvv,
            final int balance,
            final String cardHolderPassportId,
            final String cardNumber
    ) {
        this.cvv = cvv;
        this.balance = balance;
        this.cardHolderPassportId = cardHolderPassportId;
        this.cardNumber = cardNumber;
        this.issueDate = LocalDate.now();
        this.expirationDate = issueDate.plusYears(3);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderPassportId() {
        return cardHolderPassportId;
    }

    public void setCardHolderPassportId(final String cardHolderPassportId) {
        this.cardHolderPassportId = cardHolderPassportId;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(final LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(final String cvv) {
        this.cvv = cvv;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(final int balance) {
        this.balance = balance;
    }
}
