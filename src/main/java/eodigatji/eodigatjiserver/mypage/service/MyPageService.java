package eodigatji.eodigatjiserver.mypage.service;

import eodigatji.eodigatjiserver.auth.security.AuthenticatedUser;
import eodigatji.eodigatjiserver.comment.repository.CommentRepository;
import eodigatji.eodigatjiserver.mypage.dto.MyPageCommentResponseDto;
import eodigatji.eodigatjiserver.mypage.dto.MyPageResponseDto;
import eodigatji.eodigatjiserver.user.domain.User;
import eodigatji.eodigatjiserver.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import eodigatji.eodigatjiserver.mypage.dto.MyPageTemperatureResponseDto;

import java.util.List;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public MyPageService(
            UserRepository userRepository,
            CommentRepository commentRepository
    ) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public MyPageResponseDto getMyPage(AuthenticatedUser authenticatedUser) {

        User user = userRepository.findById(authenticatedUser.userId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "사용자를 찾을 수 없습니다."
                        ));

        return new MyPageResponseDto(
                user.getEmail(),
                user.getNickname(),
                user.getStudentNumber(),
                user.getTemperature()
        );
    }

    public List<MyPageCommentResponseDto> getMyComments(
            AuthenticatedUser authenticatedUser
    ) {

        return commentRepository
                .findAllByUserId(authenticatedUser.userId())
                .stream()
                .map(comment -> new MyPageCommentResponseDto(
                        comment.getId(),
                        comment.getContent()
                ))
                .toList();
    }

    public MyPageTemperatureResponseDto getMyTemperature(
            AuthenticatedUser authenticatedUser
    ) {

        User user = userRepository.findById(authenticatedUser.userId())
                     .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "사용자를 찾을 수 없습니다."
                        )
                );

        return new MyPageTemperatureResponseDto(
                user.getTemperature()
        );
    }
}