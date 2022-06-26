package com.Bytepad.server.Utilities;

import com.Bytepad.server.exceptions.InvalidField;
import com.Bytepad.server.models.Admin;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Validators {


    /**
     * used for validation of email
     * @param email
     * @throws InvalidField
     */
    public void emailValidator(String email) throws InvalidField {
        String email_regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidField();
        }
    }

    /**
     * validates password
     *     a password must have:
     *     6 characters at least
     *     15 characters at max
     *     atleast 1 special character
     *     atleast 1 upper case letter
     *     atleast 1 lower case letter
     *     and atleast 1 number
     * @param password
     * @throws InvalidField
     */
    public void passwordValidator(String password) throws InvalidField {
        String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{6,15}$";
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            throw new InvalidField();
        }
    }


    /**
     * function to verify password
     * @param admin
     * @param password
     * @return
     */
    public boolean verifyPassword(Admin admin, String password){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(password, admin.getPassword());
    }


}
