package eodigatji.eodigatjiserver.comment.controller;

import eodigatji.eodigatjiserver.comment.dto.CommentRequestDto;
import eodigatji.eodigatjiserver.comment.dto.CommentResponseDto;
import eodigatji.eodigatjiserver.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public List<CommentResponseDto> getComments(
            @PathVariable Long postId
    ) {
        return commentService.getComments(postId);
    }

    @PostMapping("/{postId}/comments")
    public void createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto request
    ) {
        commentService.createComment(postId, request);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(postId, commentId);
    }
}