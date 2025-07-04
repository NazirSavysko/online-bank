package utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.lang.System.err;

@Component
public final class MyOwnValidator implements Validator {

    @Override
    public boolean supports(final @NotNull Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final @NotNull Object target, final @NotNull Errors errors) {
        final Class<?> clazz = target.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            this.validateField(field, target, errors);
        }

    }

    private void validateField(final @NotNull Field field, final Object target, final Errors errors) {
        field.setAccessible(true);

        try {
            final Object value = field.get(target);
            final Annotation[] annotations = field.getAnnotations();

            for (Annotation annotation : annotations) {

                if (annotation instanceof NotBlank notBlank) {
                    final String message = notBlank.message();
                    if (value == null || (value instanceof String str && str.isBlank())) {
                        errors.rejectValue(field.getName(), "field.notBlank", message);
                    }

                } else if (annotation instanceof Pattern pattern) {
                    if (value instanceof String strValue && !strValue.isBlank()) {
                        final String regex = pattern.regexp();
                        final String message = pattern.message();
                        if (!strValue.matches(regex)) {
                            errors.rejectValue(field.getName(), "field.invalidFormat", message);
                        }
                    }

                } else if (annotation instanceof javax.validation.constraints.NotNull) {
                    if (value == null) {
                        errors.rejectValue(field.getName(), "field.notNull", field.getName() + " cannot be null");
                    }

                } else if (annotation instanceof Size size) {
                    if (value instanceof String strValue) {
                        final int len = strValue.length();
                        final int min = size.min();
                        final int max = size.max();
                        final String message = size.message();
                        if (len < min || len > max) {
                            errors.rejectValue(field.getName(), "field.size", message);
                        }
                    }

                } else if (annotation.annotationType().getSimpleName().equals("CurrentAmount")) {
                    if (value instanceof BigDecimal currentAmount) {
                        final Field amountField = target.getClass().getDeclaredField("amount");
                        amountField.setAccessible(true);
                        final BigDecimal amount = (BigDecimal) amountField.get(target);
                        if (amount != null && currentAmount.compareTo(amount) > 0) {
                            errors.rejectValue(field.getName(), "field.currentAmount", "Current amount cannot be greater than the total amount");
                        }
                    }

                } else if (annotation.annotationType().getSimpleName().equals("MinAgeUser")) {
                    if (value instanceof LocalDate birthDate) {
                        final int age = LocalDate.now().getYear() - birthDate.getYear();
                        if (age < 18) {
                            errors.rejectValue(field.getName(), "field.minAge", "UserDTO must be at least 18 years old");
                        }
                    }

                } else if (annotation.annotationType().getSimpleName().equals("ExpirationDate")) {
                    if (value instanceof LocalDate expirationDate) {
                        final Field issueDateField = target.getClass().getDeclaredField("issueDate");
                        issueDateField.setAccessible(true);
                        final LocalDate issueDate = (LocalDate) issueDateField.get(target);
                        if (issueDate != null && expirationDate.isBefore(issueDate)) {
                            errors.rejectValue(field.getName(), "field.expirationDate", "Expiration date cannot be before the issue date");
                        }
                    }
                }
            }

        } catch (Exception e) {
            err.println("Cannot validate field: " + field.getName());
            err.println("Error: " + e.getMessage());
        }
    }
}
