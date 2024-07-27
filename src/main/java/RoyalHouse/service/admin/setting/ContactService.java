package RoyalHouse.service.admin.setting;

import RoyalHouse.model.Contact;
import RoyalHouse.model.RequestEmail;
import RoyalHouse.repository.ContactRepository;
import RoyalHouse.repository.RequestEmailRepository;
import RoyalHouse.util.RegEx;
import RoyalHouse.util.Exception.*;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final PasswordEncoder passwordEncoder;

    private final ContactRepository contactRepository;
    private final RequestEmailRepository requestEmailRepository;

    public ContactService(ContactRepository contactRepository, RequestEmailRepository requestEmailRepository, PasswordEncoder passwordEncoder) {
        this.contactRepository = contactRepository;
        this.requestEmailRepository = requestEmailRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Contact getContact() {
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.isEmpty()) {
            return null;
        }

        return contacts.get(0);
    }

    public List<String> getRequestEmails(Long contactId) {
        return requestEmailRepository.findByContactId(contactId)
                .stream()
                .map(RequestEmail::getEmail)
                .collect(Collectors.toList());
    }

    public void saveRequestEmails(Contact contact, List<String> requestEmails) {
        requestEmailRepository.deleteByContact(contact);
        requestEmails.forEach(email -> {
            RequestEmail requestEmail = RequestEmail.builder()
                    .email(email)
                    .contact(contact)
                    .build();
            requestEmailRepository.save(requestEmail);
        });
    }

    public void deleteRequestEmails(Contact contact) {
        requestEmailRepository.deleteByContact(contact);
    }

    public void createContact(Contact contact, List<String> requestEmails) {
        logger.info("Creating a contact with email: {}", contact.getEmail());

        validateContact(contact);

        Contact newContact = Contact.builder()
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .telegram(contact.getTelegram())
                .viber(contact.getViber())
                .instagram(contact.getInstagram())
                .facebook(contact.getFacebook())
                .address(contact.getAddress())
                .password(passwordEncoder.encode(contact.getPassword()))
                .build();

        Contact savedContact = contactRepository.save(newContact);
        saveRequestEmails(savedContact, requestEmails);

        logger.info("Contact successfully created with ID: {}", savedContact.getId());
    }

    public void updateContact(Contact contact, List<String> requestEmails) {
        logger.info("Updating contact with ID: {}", contact.getId());

        validateContact(contact);

        contactRepository.save(contact);
        saveRequestEmails(contact, requestEmails);

        logger.info("Contact successfully updated with ID: {}", contact.getId());
    }

        public void changePassword(Contact contact, String currentPassword, String newPassword, String confirmPassword) {
        if (Objects.isNull(currentPassword) || StringUtils.isBlank(currentPassword)) {
            throw new InvalidPasswordException("Current password cannot be null or blank.");
        }

        if (!passwordEncoder.matches(currentPassword, contact.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect.");
        }

        if (Objects.isNull(newPassword) || StringUtils.isBlank(newPassword)) {
            throw new InvalidPasswordException("New password cannot be null or blank.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("New password and confirm password do not match.");
        }

        validatePassword(newPassword);

        contact.setPassword(passwordEncoder.encode(newPassword));
        contactRepository.save(contact);
    }

    private void validateContact(Contact contact) {
        validateEmail(contact.getEmail());
        validatePhone(contact.getPhone());
        validatePassword(contact.getPassword());
        validateTelegram(contact.getTelegram());
        validateViber(contact.getViber());
        validateInstagram(contact.getInstagram());
        validateFacebook(contact.getFacebook());
        validateAddress(contact.getAddress());
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || StringUtils.isBlank(email)) {
            throw new InvalidEmailException("Email cannot be null or blank.");
        }
        if (!RegEx.EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidEmailException("Invalid email format.");
        }
    }

    private void validatePhone(String phone) {
        if (Objects.isNull(phone) || StringUtils.isBlank(phone)) {
            throw new InvalidPhoneNumberException("Phone number cannot be null or blank.");
        }
        if (!RegEx.PHONE_REGEX.matcher(phone).matches()) {
            throw new InvalidPhoneNumberException("Invalid phone number format.");
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || StringUtils.isBlank(password)) {
            throw new InvalidPasswordException("Password cannot be null or blank.");
        }
        if (!RegEx.PASSWORD_REGEX.matcher(password).matches()) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
        }
    }

    private void validateTelegram(String telegram) {
        if (Objects.isNull(telegram) || StringUtils.isBlank(telegram)) {
            throw new InvalidTelegramException("Telegram username cannot be null or blank.");
        }
        if (!RegEx.TELEGRAM_REGEX.matcher(telegram).matches()) {
            throw new InvalidTelegramException("Invalid Telegram username format.");
        }
    }

    private void validateViber(String viber) {
        if (Objects.isNull(viber) || StringUtils.isBlank(viber)) {
            throw new InvalidViberException("Viber phone number cannot be null or blank.");
        }
        if (!RegEx.PHONE_REGEX.matcher(viber).matches()) {
            throw new InvalidViberException("Invalid Viber phone number format.");
        }
    }

    private void validateInstagram(String instagram) {
        if (Objects.isNull(instagram) || StringUtils.isBlank(instagram)) {
            throw new InvalidInstagramException("Instagram link cannot be null or blank.");
        }
        if (!RegEx.INSTAGRAM_REGEX.matcher(instagram).matches()) {
            throw new InvalidInstagramException("Invalid Instagram link format.");
        }
    }

    private void validateFacebook(String facebook) {
        if (Objects.isNull(facebook) || StringUtils.isBlank(facebook)) {
            throw new InvalidFacebookException("Facebook link cannot be null or blank.");
        }
        if (!RegEx.FACEBOOK_REGEX.matcher(facebook).matches()) {
            throw new InvalidFacebookException("Invalid Facebook link format.");
        }
    }

    private void validateAddress(String address) {
        if (Objects.isNull(address) || address.length() > 255) {
            throw new InvalidAddressException("Address length must be less than or equal to 255 characters.");
        }
    }
}
