package eodigatji.eodigatjiserver.search.controller;

import eodigatji.eodigatjiserver.search.dto.SearchResponseDto;
import eodigatji.eodigatjiserver.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class SearchController {

    private final SearchService searchService;

    //17번 키워드 검색
    @GetMapping("/search")
    public List<SearchResponseDto> searchByKeyword(
            @RequestParam String keyword
            ){
        return searchService.searchByKeyword(keyword);
    }
    //18번 카테고리 검색
    @GetMapping("/categories/{category}")
    public List<SearchResponseDto> searchByCategory(
            @PathVariable String category
    ){
        return searchService.searchByCategory(category);
    }
    //19번 날짜 검색
    @GetMapping("/search/date")
    public List<SearchResponseDto> searchByDate(
            @RequestParam String date
    ){
        return searchService.searchByDate(date);
    }
    //32번 장소 검색
    @GetMapping("/search/place")
    public List<SearchResponseDto> searchByPlace(
            @RequestParam String place
    ) {
        return searchService.searchByPlace(place);
    }

}
