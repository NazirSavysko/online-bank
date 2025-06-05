package entity;

import entity.enums.Mortgage_Term;

public class Mortgage {
    private int amountOfMortgage;
    private String mortgageHolderPassportId;
    private int currentMortgageAmount;
    private Mortgage_Term mortgageTerm;

    public Mortgage(
            final Mortgage_Term mortgageTerm,
            final int currentMortgageAmount,
            final String mortgageHolderPassportId,
            final int mortgageAmount
    ) {
        this.mortgageTerm = mortgageTerm;
        this.currentMortgageAmount = currentMortgageAmount;
        this.mortgageHolderPassportId = mortgageHolderPassportId;
        this.amountOfMortgage = mortgageAmount;
    }

    public int getAmountOfMortgage() {
        return amountOfMortgage;
    }

    public void setAmountOfMortgage(final int amountOfMortgage) {
        this.amountOfMortgage = amountOfMortgage;
    }

    public Mortgage_Term getMortgageTerm() {
        return mortgageTerm;
    }

    public void setMortgageTerm(final Mortgage_Term mortgageTerm) {
        this.mortgageTerm = mortgageTerm;
    }

    public int getCurrentMortgageAmount() {
        return currentMortgageAmount;
    }

    public void setCurrentMortgageAmount(final int currentMortgageAmount) {
        this.currentMortgageAmount = currentMortgageAmount;
    }

    public String getMortgageHolderPassportId() {
        return mortgageHolderPassportId;
    }

    public void setMortgageHolderPassportId(final String mortgageHolderPassportId) {
        this.mortgageHolderPassportId = mortgageHolderPassportId;
    }
}
