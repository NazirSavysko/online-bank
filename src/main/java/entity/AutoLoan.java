package entity;

public final class AutoLoan {
    private int amountOfCredit;
    private int currentCreditAmount;
    private int creditTermInMonths;
    private String creditHolderPassportId;

    public AutoLoan(
            final int amountOfCredit,
            final int currentCreditAmount,
            final int creditTermInMonths,
            final String creditHolderPassportId
    ) {
        this.amountOfCredit = amountOfCredit;
        this.currentCreditAmount = currentCreditAmount;
        this.creditTermInMonths = creditTermInMonths;
        this.creditHolderPassportId = creditHolderPassportId;
    }

    public int getAmountOfCredit() {
        return amountOfCredit;
    }

    public void setAmountOfCredit(final int amountOfCredit) {
        this.amountOfCredit = amountOfCredit;
    }

    public int getCurrentCreditAmount() {
        return currentCreditAmount;
    }

    public void setCurrentCreditAmount(final int currentCreditAmount) {
        this.currentCreditAmount = currentCreditAmount;
    }

    public int getCreditTermInMonths() {
        return creditTermInMonths;
    }

    public void setCreditTermInMonths(final int creditTermInMonths) {
        this.creditTermInMonths = creditTermInMonths;
    }

    public String getCreditHolderPassportId() {
        return creditHolderPassportId;
    }

    public void setCreditHolderPassportId(final String creditHolderPassportId) {
        this.creditHolderPassportId = creditHolderPassportId;
    }
}
