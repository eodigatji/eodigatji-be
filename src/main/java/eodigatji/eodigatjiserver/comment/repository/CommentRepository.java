package eodigatji.eodigatjiserver.comment.repository;

import eodigatji.eodigatjiserver.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // TODO: Post 엔티티 연관관계 매핑 후 수정 예정
    List<Comment> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}