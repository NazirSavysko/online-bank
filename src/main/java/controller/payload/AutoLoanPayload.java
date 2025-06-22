package controller.payload;


public record AutoLoanPayload(
        Integer amount,
        Integer termInMonths,
        Integer currentAmount,
        String holderPassportNumber
) {
}
