package eodigatji.eodigatjiserver.post.repository;

import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // LOST, FOUND 타입별로 목록을 조회할 때 사용
    List<Post> findByType(PostType type);
    List<Post> findAllByUserId(Long userId);
    // 제목 또는 설명 키워드 검색
    List<Post> findByTitleContainingOrDescriptionContaining(String title, String description);

    // 카테고리 검색
    List<Post> findByCategory(PostCategory category);

    // 작성 날짜 범위 검색
    List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 장소 ID 목록으로 게시글 검색
    List<Post> findByLocationIdIn(List<Long> locationIds);
}