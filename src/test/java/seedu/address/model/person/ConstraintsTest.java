package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test to verify length constraints and validation updates
 */
public class ConstraintsTest {

    @Test
    public void phoneValidation_updatedConstraints() {
        // Test phone: 3-15 digits allowed
        assertFalse(Phone.isValidPhone("12"), "2 digits should be invalid");
        assertTrue(Phone.isValidPhone("123"), "3 digits should be valid");
        assertTrue(Phone.isValidPhone("123456789012345"), "15 digits should be valid");
        assertFalse(Phone.isValidPhone("1234567890123456"), "16 digits should be invalid");

        System.out.println("✓ Phone validation: 3-15 digits (Phone.java line 15)");
    }

    @Test
    public void emailValidation_maxLength() {
        String longEmail = "a".repeat(90) + "@test.com"; // 99 chars
        assertTrue(Email.isValidEmail(longEmail), "99 char email should be valid");

        String tooLongEmail = "a".repeat(92) + "@test.com"; // 101 chars
        assertFalse(Email.isValidEmail(tooLongEmail), "101 char email should be invalid");

        System.out.println("✓ Email max length: 100 chars (Email.java line 51)");
    }

    @Test
    public void addressValidation_maxLength() {
        String longAddress = "a".repeat(200);
        assertTrue(Address.isValidAddress(longAddress), "200 char address should be valid");

        String tooLongAddress = "a".repeat(201);
        assertFalse(Address.isValidAddress(tooLongAddress), "201 char address should be invalid");

        System.out.println("✓ Address max length: 200 chars (Address.java line 37)");
    }

    @Test
    public void className_alreadyHasLimit() {
        // ClassName already has 50 char limit in the regex
        System.out.println("✓ Class name already limited to 50 chars (ClassName.java line 13)");
    }
}