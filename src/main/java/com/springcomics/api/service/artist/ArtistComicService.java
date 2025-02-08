package com.springcomics.api.service.artist;

import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.domain.comic.ComicHead;
import com.springcomics.api.domain.comic.Genre;
import com.springcomics.api.domain.comic.Image;
import com.springcomics.api.domain.member.Artist;
import com.springcomics.api.exception.ComicNotFound;
import com.springcomics.api.exception.InvalidRequest;
import com.springcomics.api.exception.UserNotFound;
import com.springcomics.api.repository.ArtistRepository;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.repository.comic.ComicHeadRepository;
import com.springcomics.api.repository.ImageRepository;
import com.springcomics.api.request.ComicApproval;
import com.springcomics.api.request.ComicUpload;
import com.springcomics.api.response.ComicBodyResponse;
import com.springcomics.api.response.ComicHeadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistComicService {

    private final ArtistRepository artistRepository;
    private final ComicBodyRepository comicBodyRepository;
    private final ComicHeadRepository comicHeadRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void comicApproval(ComicApproval request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Artist artist = artistRepository.findByEmail(email).orElseThrow(UserNotFound::new);

        ComicHead comicHead = ComicHead.builder()
                .title(request.getTitle())
                .synopsis(request.getSynopsis())
                .genre(Genre.valueOf(request.getGenre()))
                .thumbnailUrl(request.getThumbnailUrl())
                .artist(artist)
                .build();

        comicHeadRepository.save(comicHead);
    }

    @Transactional
    public void upload(Long comicHeadId, ComicUpload comicUpload) {

        List<Image> images = new ArrayList<>();

        for (String imageUrl : comicUpload.getImageUrls()) {
            Image image = Image.builder().imageUrl(imageUrl).build();

            images.add(image);
        }
        ComicHead comicHead = comicHeadRepository.findById(comicHeadId).orElseThrow(InvalidRequest::new);

        Long maxNo = comicBodyRepository.findMaxNoByComicHeadId(comicHeadId);

        Long nextNo = (maxNo == null) ? 1 : (maxNo + 1);

        ComicBody comicBody = ComicBody.builder()
                .title(comicUpload.getTitle())
                .thumbnail(comicUpload.getThumbnail())
                .authorComment(comicUpload.getAuthorComment())
                .images(images)
                .comicHead(comicHead)
                .no(nextNo)
                .build();

        comicBodyRepository.save(comicBody);

    }

    public List<ComicHeadResponse> getSeriesList(String email) {
        List<ComicHead> comicHeads = comicHeadRepository.findAllByCreatedBy(email);

        return comicHeads.stream().map(comicHead ->
                ComicHeadResponse.builder()
                        .id(comicHead.getId())
                        .title(comicHead.getTitle())
                        .genre(comicHead.getGenre())
                        .synopsis(comicHead.getSynopsis())
                        .thumbnailUrl(comicHead.getThumbnailUrl())
                        .artist(comicHead.getArtist().getPenName())
                        .build()).collect(Collectors.toList());
    }

    public List<ComicBodyResponse> getComicList(Long comicId) {

        List<ComicBody> comicBodies = comicBodyRepository.findAllByComicHeadId(comicId);

        ComicHead comicHead = comicHeadRepository.findById(comicId).orElseThrow(ComicNotFound::new);

        return comicBodies.stream()
                .map(comicBody -> ComicBodyResponse.builder()
                        .headId(comicHead.getId())
                        .artist(comicHead.getArtist().getPenName())
                        .headTitle(comicHead.getTitle())
                        .headSynopsis(comicHead.getSynopsis())
                        .genre(comicHead.getGenre())
                        .headThumbnail(comicHead.getThumbnailUrl())
                        .id(comicBody.getId())
                        .title(comicBody.getTitle())
                        .authorComment(comicBody.getAuthorComment())
                        .thumbnail(comicBody.getThumbnail())
                        .uploadDate(comicBody.getCreatedDate())
                        .no(comicBody.getNo())
                        .build())
                .collect(Collectors.toList());
    }

    public ComicBodyResponse getOneComic(Long comicBodyId) {

        ComicBody comic = comicBodyRepository.findById(comicBodyId).orElseThrow(ComicNotFound::new);

        List<String> imageUrls = imageRepository.findAllByComicBodyId(comicBodyId)
                .stream()
                .map(Image::getImageUrl)
                .toList();

        return ComicBodyResponse.builder()
                .id(comic.getId())
                .title(comic.getTitle())
                .authorComment(comic.getAuthorComment())
                .thumbnail(comic.getThumbnail())
                .imageUrls(imageUrls)
                .uploadDate(comic.getCreatedDate())
                .no(comic.getNo())
                .build();
    }
}
