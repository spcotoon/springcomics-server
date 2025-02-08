package com.springcomics.api.service.user;

import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.domain.comic.ComicHead;
import com.springcomics.api.exception.ComicNotFound;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.repository.comic.ComicHeadRepository;
import com.springcomics.api.response.ComicBodyResponse;
import com.springcomics.api.response.ComicHeadResponse;
import com.springcomics.api.response.OneComicBodyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserComicService {

    private final ComicHeadRepository comicHeadRepository;
    private final ComicBodyRepository comicBodyRepository;


    public Slice<ComicHeadResponse> getAllComics(String searchWord, String genre, Pageable pageable) {

        int pageSize = 40;
        Pageable requestPageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        Slice<ComicHead> comicHeads = comicHeadRepository.getComicHeadList(searchWord, genre, requestPageable);

        return comicHeads.map(comic -> ComicHeadResponse.builder()
                .id(comic.getId())
                .title(comic.getTitle())
                .thumbnailUrl(comic.getThumbnailUrl())
                .artist(comic.getArtist().getPenName())
                .build());

    }

    public List<ComicHeadResponse> getAllComicsByArtist(String penName) {

        try {
            List<ComicHead> comicHeads = comicHeadRepository.findAllByPenName(penName);


            return comicHeads.stream()
                    .map(comic -> ComicHeadResponse.builder()
                            .id(comic.getId())
                            .title(comic.getTitle())
                            .thumbnailUrl(comic.getThumbnailUrl())
                            .artist(comic.getArtist().getPenName())
                            .build()
                    ).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new ComicNotFound();
        }

    }

    public Page<ComicBodyResponse> getOneComicList(Long comicHeadId, Pageable pageable) {

        int pageSize = 10;
        Pageable requestPageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        Page<ComicBody> comicBodies = comicBodyRepository.findOneComicListByComicHeadId(comicHeadId, requestPageable);

        return comicBodies
                .map(comicBody -> ComicBodyResponse.builder()
                        .headId(comicBody.getComicHead().getId())
                        .artist(comicBody.getComicHead().getArtist().getPenName())
                        .headTitle(comicBody.getComicHead().getTitle())
                        .headSynopsis(comicBody.getComicHead().getSynopsis())
                        .genre(comicBody.getComicHead().getGenre())
                        .headThumbnail(comicBody.getComicHead().getThumbnailUrl())
                        .id(comicBody.getId())
                        .title(comicBody.getTitle())
                        .authorComment(comicBody.getAuthorComment())
                        .thumbnail(comicBody.getThumbnail())
                        .uploadDate(comicBody.getCreatedDate())
                        .no(comicBody.getNo())
                        .build());
    }


    public OneComicBodyResponse getOneComic(Long comicHeadId, Long no) {
        return comicBodyRepository.findOneComicByComicHeadIdAndNo(comicHeadId, no);
    }
}
