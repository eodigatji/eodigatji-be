package eodigatji.eodigatjiserver.mypage.controller;

import eodigatji.eodigatjiserver.auth.security.AuthenticatedUser;
import eodigatji.eodigatjiserver.mypage.dto.MyPageResponseDto;
import eodigatji.eodigatjiserver.mypage.service.MyPageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import eodigatji.eodigatjiserver.mypage.dto.MyPageCommentResponseDto;
import eodigatji.eodigatjiserver.mypage.dto.MyPageTemperatureResponseDto;

import java.util.List;

@RestController
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/v1/mypage")
    public MyPageResponseDto getMyPage(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return myPageService.getMyPage(authenticatedUser);
    }

    @GetMapping("/v1/mypage/comments")
    public List<MyPageCommentResponseDto> getMyComments(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return myPageService.getMyComments(authenticatedUser);
    }

    @GetMapping("/v1/mypage/temperature")
    public MyPageTemperatureResponseDto getMyTemperature(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        return myPageService.getMyTemperature(authenticatedUser);
    }

}