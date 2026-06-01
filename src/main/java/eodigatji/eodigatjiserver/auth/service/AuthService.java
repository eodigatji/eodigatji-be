package eodigatji.eodigatjiserver.auth.service;

import eodigatji.eodigatjiserver.auth.domain.EmailVerification;
import eodigatji.eodigatjiserver.auth.domain.RefreshToken;
import eodigatji.eodigatjiserver.auth.dto.LoginRequest;
import eodigatji.eodigatjiserver.auth.dto.ReissueRequest;
import eodigatji.eodigatjiserver.auth.dto.SignupRequest;
import eodigatji.eodigatjiserver.auth.dto.TokenResponse;
import eodigatji.eodigatjiserver.auth.jwt.JwtTokenProvider;
import eodigatji.eodigatjiserver.auth.repository.EmailVerificationRepository;
import eodigatji.eodigatjiserver.auth.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            EmailVerificationRepository emailVerificationRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @Transactional
    public TokenResponse login(LoginRequest request) {
        String email = mailService.normalizeEmail(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        String requestRefreshToken = request.refreshToken().trim();
        if (requestRefreshToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token이 없습니다.");
        }

        if (!jwtTokenProvider.isValidRefreshToken(requestRefreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }

        RefreshToken savedRefreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "저장되지 않은 Refresh Token입니다."));

        if (savedRefreshToken.isExpired(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료된 Refresh Token입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(requestRefreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 정보가 없습니다."));

        return issueTokens(user);
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
        LocalDateTime refreshTokenExpiresAt = jwtTokenProvider.getRefreshTokenExpiresAt(refreshToken);

        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new RefreshToken(user.getId(), refreshToken, refreshTokenExpiresAt));
        savedRefreshToken.update(refreshToken, refreshTokenExpiresAt);
        refreshTokenRepository.save(savedRefreshToken);

        return TokenResponse.bearer(accessToken, refreshToken);
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
