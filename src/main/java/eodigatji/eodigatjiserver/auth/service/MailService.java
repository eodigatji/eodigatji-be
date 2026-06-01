package eodigatji.eodigatjiserver.auth.service;

import eodigatji.eodigatjiserver.auth.domain.EmailVerification;
import eodigatji.eodigatjiserver.auth.repository.EmailVerificationRepository;
import eodigatji.eodigatjiserver.user.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MailService {

    private static final String SCHOOL_EMAIL_DOMAIN = "kangnam.ac.kr";
    private static final int VERIFICATION_CODE_BOUND = 1_000_000;
    private static final int VERIFICATION_EXPIRATION_MINUTES = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    public MailService(
            JavaMailSender javaMailSender,
            EmailVerificationRepository emailVerificationRepository,
            UserRepository userRepository
    ) {
        this.javaMailSender = javaMailSender;
        this.emailVerificationRepository = emailVerificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendVerificationCode(String email) {
        String normalizedEmail = normalizeEmail(email);
        validateKangnamEmail(normalizedEmail);
        validateNotRegisteredEmail(normalizedEmail);

        String verificationCode = createVerificationCode();
        sendMail(normalizedEmail, verificationCode);

        EmailVerification emailVerification = new EmailVerification(
                normalizedEmail,
                verificationCode,
                LocalDateTime.now().plusMinutes(VERIFICATION_EXPIRATION_MINUTES)
        );
        emailVerificationRepository.save(emailVerification);
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        String normalizedEmail = normalizeEmail(email);
        EmailVerification emailVerification = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증정보가 없습니다."));

        if (!emailVerification.isCodeMatched(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다.");
        }

        if (emailVerification.isExpired(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다.");
        }

        emailVerification.verify();
    }

    public void validateKangnamEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        int atIndex = normalizedEmail.indexOf('@');

        if (atIndex <= 0 || atIndex != normalizedEmail.lastIndexOf('@')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "강남대학교 이메일만 사용할 수 있습니다.");
        }

        String domain = normalizedEmail.substring(atIndex + 1);
        if (!SCHOOL_EMAIL_DOMAIN.equals(domain)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "강남대학교 이메일만 사용할 수 있습니다.");
        }
    }

    public String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private void validateNotRegisteredEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
        }
    }

    private void sendMail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[어디갔지] 이메일 인증번호 안내");
        message.setText("인증번호는 " + verificationCode + "입니다. 인증번호는 5분간 유효합니다.");

        javaMailSender.send(message);
    }

    private String createVerificationCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(VERIFICATION_CODE_BOUND));
    }
}
