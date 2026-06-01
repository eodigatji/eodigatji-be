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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @RequestBody PostCreateRequest request,
            @RequestHeader("Authorization") String token) {

        // 3. "Bearer" 문자열 제거
        String jwt = token.substring(7);

        // 4. JwtTokenProvider를 통해 userId 추출
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
            // 최신 글이 맨 위로 오도록 내림차순(DESC) 정렬을 기본값으로 설정.
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostListResponse> response = postService.getPostList(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody PostUpdateRequest request) {

        postService.updatePost(postId, request);
        return ResponseEntity.ok().build(); // 데이터 반환 없이 성공(200) 상태코드만 보냄
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build(); // 데이터 반환 없이 성공(200) 상태코드만 보냄
    }
}