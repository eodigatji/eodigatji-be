package eodigatji.eodigatjiserver.search.service;
import eodigatji.eodigatjiserver.location.entity.LocationEntity;
import eodigatji.eodigatjiserver.location.repository.LocationRepository;
import eodigatji.eodigatjiserver.post.entity.Post;
import eodigatji.eodigatjiserver.post.entity.enums.PostCategory;
import eodigatji.eodigatjiserver.post.repository.PostRepository;
import eodigatji.eodigatjiserver.search.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final PostRepository postRepository;
    private final LocationRepository locationRepository;

    // 키워드 검색
    public List<SearchResponseDto> searchByKeyword(String keyword) {
        String value = validate(keyword, "검색어");

        List<Post> posts = postRepository.findByTitleContainingOrDescriptionContaining(
                value,
                value
        );
        return convert(posts);
    }
    // 카테고리 검색
    public List<SearchResponseDto> searchByCategory(String category) {
        String value = validate(category, "카테고리");

        PostCategory postCategory;
        try {
             postCategory = PostCategory.valueOf
                     (value.toUpperCase(Locale.ROOT)
             );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }

        return convert(postRepository.findByCategory(postCategory));
    }

    // 날짜 검색
    public List<SearchResponseDto> searchByDate(String date){
        String value = validate(date, "날짜");

        try {
            LocalDate searchDate = LocalDate.parse(value);

            return convert(postRepository.findByCreatedAtBetween(
                    searchDate.atStartOfDay(),
                    searchDate.atTime(LocalTime.MAX)
            ));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "날짜는 yyyy-MM-dd 형식이어야 합니다."
            );
        }
    }
    // 장소명 검색

    public List<SearchResponseDto> searchByPlace(String place) {
       String value = validate(place, "장소명");

       List<Long> locationIds = locationRepository.findByNameContaining(value)
               .stream()
               .map(LocationEntity::getId)
               .toList();

       if (locationIds.isEmpty()) {
           return List.of();
       }

       return convert(postRepository.findByLocationIdIn(locationIds));
    }

    // Post 목록을 검색 응답으로 변환
    private List<SearchResponseDto> convert(List<Post> posts) {
        return posts.stream()
                .map(SearchResponseDto::from)
                .toList();
    }

    // 검색값 검증 및 앞뒤 공백 제거
    private String validate(String value , String fieldName) {
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + "는 비어 있을 수 없습니다.");
        }
        return  value.trim();
    }

}
