package com.sparta.newsfeed.service;
import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.repository.NewsfeedRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsfeedService {
    private final NewsfeedRepository newsfeedRepository;
    public NewsfeedService(NewsfeedRepository newsfeedRepository) {
        this.newsfeedRepository = newsfeedRepository;
    }

    public NewsfeedResponseDto createNewsfeed(NewsfeedRequestDto requestDto) {

        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setTitle(requestDto.getTitle());
        newsfeed.setContent(requestDto.getContent());
        Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
        NewsfeedResponseDto responseDto = new NewsfeedResponseDto(savedNewsfeed);
        return responseDto;
    }

    public NewsfeedResponseDto getNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("조회된 뉴스피드가 없습니다. id=" + id));
        return new NewsfeedResponseDto(newsfeed);
    }

    public List<NewsfeedResponseDto> getAllNewsfeeds() {
        List<Newsfeed> newsfeedList = newsfeedRepository.findAll();
        return newsfeedList.stream()
                .map(NewsfeedResponseDto::new)
                .collect(Collectors.toList());
    }


    public NewsfeedResponseDto updateNewsfeed(Long id, NewsfeedRequestDto requestDto) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스피드를 찾을 수 없습니다. id=" + id));

        newsfeed.setTitle(requestDto.getTitle());
        newsfeed.setContent(requestDto.getContent());

        Newsfeed updatedNewsfeed = newsfeedRepository.save(newsfeed);

        return new NewsfeedResponseDto(updatedNewsfeed);
    }

    public String deleteNewsfeed(Long id) {
        if (!newsfeedRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 뉴스피드가 없습니다. id=" + id);
        }
        newsfeedRepository.deleteById(id);
        return "뉴스피드가 삭제되었습니다. id=" + id;
    }
}