package com.springcomics.api.controller.user;

import com.springcomics.api.response.ComicBodyResponse;
import com.springcomics.api.response.ComicHeadResponse;
import com.springcomics.api.response.OneComicBodyResponse;
import com.springcomics.api.service.user.UserComicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserComicController {

    private final UserComicService userComicService;

    @GetMapping("/springcomics/comics")
    public ResponseEntity<Slice<ComicHeadResponse>> allComics(
            @RequestParam(name = "search", required = false) String searchWord,
            @RequestParam(name = "genre", required = false) String genre,
            Pageable pageable
    ) {
        Slice<ComicHeadResponse> comicList = userComicService.getAllComics(searchWord, genre, pageable);

        return ResponseEntity.ok(comicList);
    }

    @GetMapping("/springcomics/comics/artist/{artistName}")
    public ResponseEntity<List<ComicHeadResponse>> allComicsByArtist(
            @PathVariable(name = "artistName", required = false) String penName
    ) {
        return ResponseEntity.ok(userComicService.getAllComicsByArtist(penName));
    }


    @GetMapping("/springcomics/comics/{comicHeadId}")
    public ResponseEntity<PagedModel<ComicBodyResponse>> oneComicList(@PathVariable("comicHeadId") Long comicHeadId, Pageable pageable) {

        return ResponseEntity.ok().body(new PagedModel<>(userComicService.getOneComicList(comicHeadId, pageable)));
    }

    @GetMapping("/springcomics/get-one/{comicHeadId}/comics/{no}")
    public ResponseEntity<OneComicBodyResponse> oneComic2(
            @PathVariable("comicHeadId") Long comicHeadId,
            @PathVariable("no") Long no
    ) {

        return ResponseEntity.ok().body(userComicService.getOneComic(comicHeadId, no));
    }

}
