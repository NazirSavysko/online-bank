package utils;

import controller.payload.AutoLoanPayload;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public final class AutoLoanValidator implements Validator {

    private static final String PASSPORT_PATTERN = "^[A-Z]{2}[0-9]{6}$";

    @Override
    public boolean supports(@NotNull final Class<?> clazz) {
        return AutoLoanPayload.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NotNull final Object target, @NotNull final Errors errors) {
        final AutoLoanPayload autoLoan = (AutoLoanPayload) target;

        if (autoLoan.amount() == null) {
            errors.rejectValue("amount", "amount.empty", "Loan amount cannot be empty");
        } else {
            if (autoLoan.amount() <= 0) {
                errors.rejectValue("amount", "amount.negativeOrZero", "Loan amount must be greater than zero");
            }
        }
        if (autoLoan.currentAmount() == null) {
            errors.rejectValue("currentAmount", "currentAmount.empty", "Current loan amount cannot be empty");
        } else {
            if (autoLoan.currentAmount() <= 0) {
                errors.rejectValue("currentAmount", "currentAmount.negativeOrZero", "Current loan amount must be greater than zero");
            }
        }
        if (autoLoan.termInMonths() == null || autoLoan.termInMonths() <= 0) {
            errors.rejectValue("termInMonths", "termInMonths.negativeOrZero", "Term in months must be greater than zero");
        }
        if (autoLoan.holderPassportNumber() == null || autoLoan.holderPassportNumber().isBlank()) {
            errors.rejectValue("holderPassportNumber", "holderPassportNumber.empty", "Holder passport number cannot be blank");
        } else if (!autoLoan.holderPassportNumber().matches(PASSPORT_PATTERN)) {
            errors.rejectValue("holderPassportNumber", "holderPassportNumber.invalidFormat", "Holder passport number must be in the format XX123456");
        }

    }
}
