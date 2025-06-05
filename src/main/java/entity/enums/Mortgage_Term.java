package entity.enums;

public enum Mortgage_Term {
    TEN_YEARS(10),
    FIFTEEN_YEARS(15),
    TWENTY_YEARS(20);

    private final int term;

    Mortgage_Term(final int term) {
        this.term = term;
    }

    public int getTerm() {
        return term;
    }
}
