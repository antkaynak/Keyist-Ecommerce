package com.commerce.backend.validator;

import com.commerce.backend.dto.PasswordForgotDTO;
import com.commerce.backend.dto.PasswordResetDTO;
import com.commerce.backend.dto.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof UserDTO) {
            UserDTO user = (UserDTO) obj;
            return user.getPassword().equals(user.getPasswordRepeat());
        } else if (obj instanceof PasswordResetDTO) {
            PasswordResetDTO passwordResetDTO = (PasswordResetDTO) obj;
            return passwordResetDTO.getNewPassword().equals(passwordResetDTO.getNewPasswordConfirm());
        } else if (obj instanceof PasswordForgotDTO) {
            PasswordForgotDTO passwordForgotDTO = (PasswordForgotDTO) obj;
            return passwordForgotDTO.getNewPassword().equals(passwordForgotDTO.getNewPasswordConfirm());
        }

        return false;

    }
}