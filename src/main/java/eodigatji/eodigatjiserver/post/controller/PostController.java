package eodigatji.eodigatjiserver.post.controller;

import eodigatji.eodigatjiserver.post.dto.PostCreateRequest;
import eodigatji.eodigatjiserver.post.dto.PostDetailResponse;
import eodigatji.eodigatjiserver.post.dto.PostListResponse;
import eodigatji.eodigatjiserver.post.dto.PostUpdateRequest;
import eodigatji.eodigatjiserver.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    //auth 연동
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody PostCreateRequest request,
            @RequestHeader("Authorization") String token) {

        // 헤더 값 검증 (Bearer가 없으면 에러 처리를 하거나 예외 발생)
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 토큰 형식입니다.");
        }

        //dumy에서 변경 완료
        String jwt = token.substring(7);
        Long userId = jwtTokenProvider.getUserIdFromAccessToken(jwt);

        Long postId = postService.createPost(request, userId);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable("postId") Long postId) {
        PostDetailResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostListResponse>> getPostList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostListResponse> response = postService.getPostList(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable("postId") Long postId,
            @RequestPart("request") PostUpdateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        postService.updatePost(postId, request, images);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestPart("image") MultipartFile image) {
        String imageUrl = postService.uploadImage(image);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}