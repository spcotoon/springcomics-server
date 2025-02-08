package com.springcomics.api.admin.manageController;

import com.springcomics.api.admin.adminDto.ComicGenreDto;
import com.springcomics.api.domain.comic.ComicHead;
import com.springcomics.api.domain.comic.Genre;
import com.springcomics.api.exception.ComicNotFound;
import com.springcomics.api.repository.comic.ComicHeadRepository;
import com.springcomics.api.response.ComicHeadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdminComicController {



    private final ComicHeadRepository comicHeadRepository;

    @GetMapping("/springcomics/pf-comics")
    public ResponseEntity<List<ComicHeadResponse>> getPfComicHeadList() {
        List<ComicHead> pfComicList = comicHeadRepository.findAllByGenre(Genre.portfolio);

        List<ComicHeadResponse> responseList = pfComicList.stream().map(comic ->
                ComicHeadResponse.builder()
                        .id(comic.getId())
                        .title(comic.getTitle())
                        .synopsis(comic.getSynopsis())
                        .thumbnailUrl(comic.getThumbnailUrl())
                        .artist(comic.getArtist().getPenName())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseList);
    }


    @PutMapping("/admin/manage/comic/set-genre")
    public ResponseEntity<String> setGenre(@RequestBody ComicGenreDto comicGenreDto) {
        ComicHead comic = comicHeadRepository.findById(comicGenreDto.getComicHeadId()).orElseThrow(ComicNotFound::new);

        comic.changeGenre(Genre.valueOf(comicGenreDto.getGenre()));

        comicHeadRepository.save(comic);

        return ResponseEntity.ok().body("success");
    }
}
