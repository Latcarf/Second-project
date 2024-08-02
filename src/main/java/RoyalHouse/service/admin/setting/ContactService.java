package RoyalHouse.service.admin.setting;

import RoyalHouse.model.Contact;
import RoyalHouse.model.RequestEmail;
import RoyalHouse.repository.ContactRepository;
import RoyalHouse.repository.RequestEmailRepository;
import RoyalHouse.util.RegEx;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public void addRequestEmail(Contact contact, String email, BindingResult bindingResult) {
        validateEmail(email, bindingResult);
        if (bindingResult.hasErrors()) {
            return;
        }

        RequestEmail requestEmail = RequestEmail.builder()
                .email(email)
                .contact(contact)
                .build();
        requestEmailRepository.save(requestEmail);
    }

    @Transactional
    public void removeRequestEmail(Long contactId, String email) {
        RequestEmail requestEmail = requestEmailRepository.findByEmailAndContactId(email, contactId);
        if (requestEmail != null) {
            requestEmailRepository.delete(requestEmail);
            logger.info("RequestEmail with email {} removed", email);
        } else {
            logger.warn("RequestEmail with email {} not found", email);
        }
    }



    public Contact findContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    @Transactional
    public void createContact(Contact contact, List<String> requestEmails, String password, BindingResult bindingResult) {
        logger.info("Creating a contact with email: {}", contact.getEmail());

        validateContact(contact, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        contact.setPassword(passwordEncoder.encode(password));
        Contact newContact = contactRepository.save(contact);

        for (String email : requestEmails) {
            RequestEmail requestEmail = RequestEmail.builder()
                    .email(email)
                    .contact(newContact)
                    .build();
            requestEmailRepository.save(requestEmail);
        }

        logger.info("Contact successfully created with ID: {}", newContact.getId());
    }

    @Transactional
    public void updateContact(Contact contact, List<String> requestEmails, BindingResult bindingResult) {
        logger.info("Updating contact with ID: {}", contact.getId());

        Contact existingContact = findContactById(contact.getId());
        if (existingContact == null) {
            bindingResult.rejectValue("id", "contact.notfound", "Contact not found.");
            return;
        }

        existingContact.setEmail(contact.getEmail());
        existingContact.setPhone(contact.getPhone());
        existingContact.setTelegram(contact.getTelegram());
        existingContact.setViber(contact.getViber());
        existingContact.setInstagram(contact.getInstagram());
        existingContact.setFacebook(contact.getFacebook());
        existingContact.setAddress(contact.getAddress());

        validateContact(existingContact, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        contactRepository.save(existingContact);

        Set<String> existingEmails = new HashSet<>(getRequestEmails(contact.getId()));

        for (String email : requestEmails) {
            if (!existingEmails.contains(email)) {
                RequestEmail requestEmail = RequestEmail.builder()
                        .email(email)
                        .contact(existingContact)
                        .build();
                requestEmailRepository.save(requestEmail);
            }
            existingEmails.remove(email);
        }

        for (String email : existingEmails) {
            RequestEmail requestEmail = requestEmailRepository.findByEmailAndContactId(email, contact.getId());
            if (requestEmail != null) {
                requestEmailRepository.delete(requestEmail);
            }
        }

        logger.info("Contact successfully updated with ID: {}", existingContact.getId());
    }

    public void updatePassword(Contact contact, String currentPassword, String newPassword, String confirmPassword, BindingResult bindingResult) {
        logger.info("Changing password for contact ID: {}", contact.getId());
        logger.info("Current password (encoded): {}", contact.getPassword());

        if (Objects.isNull(currentPassword) || StringUtils.isBlank(currentPassword)) {
            bindingResult.rejectValue("currentPassword", "password.empty", "Current password cannot be blank.");
        } else if (!passwordEncoder.matches(currentPassword, contact.getPassword())) {
            bindingResult.rejectValue("currentPassword", "password.invalid", "Current password is incorrect.");
        } else if (Objects.isNull(newPassword) || StringUtils.isBlank(newPassword)) {
            bindingResult.rejectValue("newPassword", "password.empty", "New password cannot be blank.");
        } else if (!newPassword.equals(confirmPassword)) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "New password and confirm password do not match.");
        } else {
            validatePassword(newPassword, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return;
        }

        contact.setPassword(passwordEncoder.encode(newPassword));
        contactRepository.save(contact);
    }

    private void validateContact(Contact contact, BindingResult bindingResult) {
        validateEmail(contact.getEmail(), bindingResult);
        validatePhone(contact.getPhone(), bindingResult);
        validateTelegram(contact.getTelegram(), bindingResult);
        validateViber(contact.getViber(), bindingResult);
        validateInstagram(contact.getInstagram(), bindingResult);
        validateFacebook(contact.getFacebook(), bindingResult);
        validateAddress(contact.getAddress(), bindingResult);
    }

    private void validateEmail(String email, BindingResult bindingResult) {
        if (Objects.isNull(email) || StringUtils.isBlank(email)) {
            bindingResult.rejectValue("email", "email.empty", "Email cannot be blank.");
        } else if (!RegEx.EMAIL_REGEX.matcher(email).matches()) {
            bindingResult.rejectValue("email", "email.invalid", "Email must be in the format: example@example.com");
        }
    }

    private void validatePhone(String phone, BindingResult bindingResult) {
        if (Objects.isNull(phone) || StringUtils.isBlank(phone)) {
            bindingResult.rejectValue("phone", "phone.empty", "Phone number cannot be blank.");
        } else if (!RegEx.PHONE_REGEX.matcher(phone).matches()) {
            bindingResult.rejectValue("phone", "phone.invalid", "Phone number must be in the format: 380-XX-XXX-XX-XX");
        }
    }

    private void validatePassword(String password, BindingResult bindingResult) {
        if (Objects.isNull(password) || StringUtils.isBlank(password)) {
            bindingResult.rejectValue("password", "password.empty", "Password cannot be blank.");
        } else if (!RegEx.PASSWORD_REGEX.matcher(password).matches()) {
            bindingResult.rejectValue("password", "password.invalid", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
        }
    }

    private void validateTelegram(String telegram, BindingResult bindingResult) {
        if (Objects.isNull(telegram) || StringUtils.isBlank(telegram)) {
            bindingResult.rejectValue("telegram", "telegram.empty", "Telegram username cannot be blank.");
        } else if (!RegEx.TELEGRAM_REGEX.matcher(telegram).matches()) {
            bindingResult.rejectValue("telegram", "telegram.invalid", "Telegram username must be in the format: @username");
        }
    }

    private void validateViber(String viber, BindingResult bindingResult) {
        if (Objects.isNull(viber) || StringUtils.isBlank(viber)) {
            bindingResult.rejectValue("viber", "viber.empty", "Viber phone number cannot be blank.");
        } else if (!RegEx.PHONE_REGEX.matcher(viber).matches()) {
            bindingResult.rejectValue("viber", "viber.invalid", "Viber phone number must be in the format: 380-XX-XXX-XX-XX");
        }
    }

    private void validateInstagram(String instagram, BindingResult bindingResult) {
        if (Objects.isNull(instagram) || StringUtils.isBlank(instagram)) {
            bindingResult.rejectValue("instagram", "instagram.empty", "Instagram link cannot be blank.");
        } else if (!RegEx.INSTAGRAM_REGEX.matcher(instagram).matches()) {
            bindingResult.rejectValue("instagram", "instagram.invalid", "Instagram link must be in the format: https://www.instagram.com/username/");
        }
    }

    private void validateFacebook(String facebook, BindingResult bindingResult) {
        if (Objects.isNull(facebook) || StringUtils.isBlank(facebook)) {
            bindingResult.rejectValue("facebook", "facebook.empty", "Facebook link cannot be blank.");
        } else if (!RegEx.FACEBOOK_REGEX.matcher(facebook).matches()) {
            bindingResult.rejectValue("facebook", "facebook.invalid", "Facebook link must be in the format: https://www.facebook.com/username/");
        }
    }

    private void validateAddress(String address, BindingResult bindingResult) {
        if (Objects.isNull(address) || StringUtils.isBlank(address) || address.length() > 255) {
            bindingResult.rejectValue("address", "address.invalid", "Address length must be less than or equal to 255 characters.");
        }
    }
}
