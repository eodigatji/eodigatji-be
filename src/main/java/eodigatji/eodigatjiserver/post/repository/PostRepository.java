package eodigatji.eodigatjiserver.post.repository;

import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.entity.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // LOST(분실물), FOUND(습득물) 타입별로 목록을 조회할 때 사용
    List<Post> findByType(PostType type);
}