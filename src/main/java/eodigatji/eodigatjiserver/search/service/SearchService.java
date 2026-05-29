package eodigatji.eodigatjiserver.search.service;
import eodigatji.eodigatjiserver.search.dto.SearchResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

    public List<SearchResponseDto> searchByKeyword(String keyword) {
        //PostRepository merge 후 구현
        // title, description에 keyword가 포함 된 게시글 검색
        return List.of();
    }

    public List<SearchResponseDto> searchByCategory(String category){
        //PostRepository merge 후 구현
        //String category를 PostCategory enum으로 변환한 뒤 카테고리 기준 검색
        return List.of();
    }

    public List<SearchResponseDto> searchByDate(String date){
        //PostRepository merge 후 구현
        //date 문자열을 LocalDate로 변환
        //해당 날짜의 createdAt 기준 검색
        return List.of();
    }

    public List<SearchResponseDto> searchByPlace(String place) {
        //PostRepository, LocationRepository merge 후 구현
        //LocationEntity의 name/detail/number에서 place 검색
        //검색된 LocationEntity들의 id 목록 추출
        //PostRepository에서 locationId가 해당 id 목록에 포함된 게시글 검색
        return List.of();
    }

}
