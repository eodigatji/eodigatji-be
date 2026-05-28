package eodigatji.eodigatjiserver.auth.service;

import eodigatji.eodigatjiserver.auth.domain.EmailVerification;
import eodigatji.eodigatjiserver.auth.dto.SignupRequest;
import eodigatji.eodigatjiserver.auth.repository.EmailVerificationRepository;
import eodigatji.eodigatjiserver.user.domain.User;
import eodigatji.eodigatjiserver.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final int INITIAL_TEMPERATURE = 36;

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AuthService(
            UserRepository userRepository,
            EmailVerificationRepository emailVerificationRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public void signup(SignupRequest request) {
        String email = mailService.normalizeEmail(request.email());
        mailService.validateKangnamEmail(email);
        validateNotRegisteredEmail(email);
        validateVerifiedEmail(email);

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(
                email,
                encodedPassword,
                request.studentNumber(),
                request.nickname(),
                INITIAL_TEMPERATURE
        );

        userRepository.save(user);
    }

    private void validateNotRegisteredEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
        }
    }

    private void validateVerifiedEmail(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증정보가 없습니다."));

        if (!emailVerification.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다.");
        }

        if (emailVerification.isExpired(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다.");
        }
    }
}
