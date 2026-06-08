package eodigatji.eodigatjiserver.post.service;

import eodigatji.eodigatjiserver.post.dto.PostCreateRequest;
import eodigatji.eodigatjiserver.post.dto.PostDetailResponse;
import eodigatji.eodigatjiserver.post.dto.PostListResponse;
import eodigatji.eodigatjiserver.post.dto.PostUpdateRequest;
import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String IMAGE_UPLOAD_DIR = "uploads/posts";

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostCreateRequest request, List<MultipartFile> images, Long userId) {
        List<String> imageUrls = uploadImages(images);

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .category(request.getCategory())
                .locationId(request.getLocationId())
                .userId(userId)
                .imageUrls(imageUrls)
                .build();

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        return PostDetailResponse.from(post);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(PostListResponse::from);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        List<String> imageUrls = null;

        if (images != null && !images.isEmpty()) {
            deleteImageFiles(post.getImageUrls());
            imageUrls = uploadImages(images);
        }

        post.update(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getLocationId(),
                request.getType(),
                imageUrls
        );
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        deleteImageFiles(post.getImageUrls());

        postRepository.delete(post);
    }

    public String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
        }

        String originalFilename = image.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("이미지 파일명이 올바르지 않습니다.");
        }

        String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String extension = getExtension(originalFilename);
        String savedFilename = UUID.randomUUID() + "_" + LocalDateTime.now().toString().replace(":", "-") + extension;

        try {
            Path uploadPath = Paths.get(IMAGE_UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path imagePath = uploadPath.resolve(savedFilename);
            image.transferTo(imagePath.toFile());

            return "/posts/" + savedFilename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();

        if (images == null || images.isEmpty()) {
            return imageUrls;
        }

        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                imageUrls.add(uploadImage(image));
            }
        }

        return imageUrls;
    }

    private void deleteImageFiles(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        for (String imageUrl : imageUrls) {
            deleteImageFile(imageUrl);
        }
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String filename = imageUrl.replace("/posts/", "");
        Path imagePath = Paths.get(IMAGE_UPLOAD_DIR, filename);

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 삭제 중 오류가 발생했습니다. imageUrl=" + imageUrl, e);
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");

        if (dotIndex == -1) {
            return "";
        }

        return filename.substring(dotIndex);
    }
}