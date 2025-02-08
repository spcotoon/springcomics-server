package com.springcomics.api.admin.manageController;

import com.springcomics.api.admin.adminDto.*;
import com.springcomics.api.crypto.PasswordEncryptor;
import com.springcomics.api.domain.member.Artist;
import com.springcomics.api.exception.AlreadyExistsEmailException;
import com.springcomics.api.repository.ArtistRepository;
import com.springcomics.api.repository.CommentRepository;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.repository.comic.ComicHeadRepository;
import com.springcomics.api.request.ArtistSignup;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AdminArtistController {

    private final ArtistRepository artistRepository;
    private final ComicHeadRepository comicHeadRepository;
    private final ComicBodyRepository comicBodyRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncryptor passwordEncryptor;

    @PostMapping("/admin/manage/artist/signup")
    public ResponseEntity<String> signup(@RequestBody ArtistSignup artistSignup) {

        Optional<Artist> existingUser = artistRepository.findByEmail(artistSignup.getEmail() + "@spring.comics.artist");
        if (existingUser.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        Artist artist = Artist.builder()
                .email(artistSignup.getEmail() + "@spring.comics.artist")
                .password(passwordEncryptor.encode(artistSignup.getPassword()))
                .penName(artistSignup.getPenName())
                .selfIntroduction(artistSignup.getSelfIntroduction())
                .build();

        artistRepository.save(artist);

        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin/manage/artist")
    public ResponseEntity<PagedModel<ArtistDto>> getAllArtist(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<ArtistDto> artistPage = artistRepository.findArtistAndSeriesAmount(pageable);
        return ResponseEntity.ok().body(new PagedModel<>(artistPage));
    }

    @DeleteMapping("/admin/manage/artist/{id}/delete")
    public ResponseEntity<String> deleteArtist(@PathVariable("id") Long artistId) {
        try {
            artistRepository.deleteById(artistId);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("해당 작가의 작품을 먼저 삭제해주세요.");
        }
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin/manage/comicHead/list")
    public ResponseEntity<PagedModel<ComicHeadDto>> comicHeadList(@RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<ComicHeadDto> comicHeadPage = comicHeadRepository.findComicHeadListByAdminPage(pageable);
        return ResponseEntity.ok().body(new PagedModel<>(comicHeadPage));
    }

    @DeleteMapping("/admin/manage/comicHead/{id}/delete")
    public ResponseEntity<String> deleteComic(@PathVariable("id") Long comicHeadId) {
        comicHeadRepository.deleteById(comicHeadId);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/admin/manage/comment/comic-list/{id}")
    public ResponseEntity<PagedModel<ComicBodyDto>> commentComicBodyList(@RequestParam(value = "page", defaultValue = "0") int page, @PathVariable("id") Long comicHeadId) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<ComicBodyDto> comicBodyPage = comicBodyRepository.findComicBodyWithCommentByComicHeadId(pageable, comicHeadId);
        return ResponseEntity.ok().body(new PagedModel<>(comicBodyPage));
    }

    @GetMapping("/admin/manage/comment/comment-list/{id}")
    public ResponseEntity<PagedModel<CommentDto>> getCommentListByComicBodyId(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @PathVariable("id") Long comicBodyId) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<CommentDto> commentPage = commentRepository.findCommentDtosByComicBodyId(pageable, comicBodyId);
        return ResponseEntity.ok().body(new PagedModel<>(commentPage));
    }

    @DeleteMapping("/admin/manage/comment/{id}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long commentId) {
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok().body("success");
    }
}


