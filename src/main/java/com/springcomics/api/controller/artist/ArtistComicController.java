package com.springcomics.api.controller.artist;

import com.springcomics.api.config.util.JwtUtil;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.request.ComicApproval;
import com.springcomics.api.request.ComicUpload;
import com.springcomics.api.response.ComicBodyResponse;
import com.springcomics.api.response.ComicHeadResponse;
import com.springcomics.api.service.artist.ArtistComicService;
import com.springcomics.api.service.S3Service;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArtistComicController {

    private final ArtistComicService artistComicService;
    private final S3Service s3Service;
    private final ComicBodyRepository comicBodyRepository;

    @PostMapping("/artist/comic-body/thumbnail/presigned-url")
    public ResponseEntity<String> uploadThumbnail(@RequestBody ComicUpload request) {

        String fileExtension = getFileExtension(request.getThumbnail());
        String uniqueFileName = generateUniqueFileName(request.getThumbnail(), fileExtension);

        String s3Path = "test/" + uniqueFileName;

        String presignedUrl = s3Service.createPresignedUrl(s3Path);

        return ResponseEntity.ok(presignedUrl);
    }

    @PostMapping("/artist/comic-body/presigned-url")
    public ResponseEntity<List<String>> uploadRequest(@Validated @RequestBody ComicUpload request) {

        List<String> presignedUrls = new ArrayList<>();

        for (String fileName : request.getImageUrls()) {

            String fileExtension = getFileExtension(fileName);
            String uniqueFileName = generateUniqueFileName(fileName, fileExtension);

            String s3Path = "test/" + uniqueFileName;

            String presignedUrl = s3Service.createPresignedUrl(s3Path);
            presignedUrls.add(presignedUrl);
        }

        return ResponseEntity.ok(presignedUrls);
    }

    @PostMapping("/artist/comic-head/presigned-url")
    public ResponseEntity<String> approvalRequest(@Validated @RequestBody ComicApproval request) {

        String fileExtension = getFileExtension(request.getThumbnailUrl());
        String uniqueFileName = generateUniqueFileName(request.getThumbnailUrl(), fileExtension);

        String s3Path = "test/" + uniqueFileName;

        String presignedUrl = s3Service.createPresignedUrl(s3Path);


        return ResponseEntity.ok(presignedUrl);
    }

    @PostMapping("/artist/comic/{comicHeadId}/upload")
    public ResponseEntity<String> comicUpload(@PathVariable("comicHeadId") Long comicHeadId, @Validated @RequestBody ComicUpload request) {

        artistComicService.upload(comicHeadId, request);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/artist/approval")
    public ResponseEntity<String> comicApproval(@Validated @RequestBody ComicApproval request) {

        artistComicService.comicApproval(request);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/artist/comic-list")
    public ResponseEntity<List<ComicHeadResponse>> comicSeriesList(HttpServletRequest request) {

        String token = JwtUtil.getJwtFromCookies(request, "accessToken");
        Claims claims = JwtUtil.extractToken(token);
        String email = claims.get("email", String.class);

        List<ComicHeadResponse> comicList = artistComicService.getSeriesList(email);

        return ResponseEntity.ok().body(comicList);
    }

    @GetMapping("/artist/comic/{comicHeadId}")
    public ResponseEntity<List<ComicBodyResponse>> comicList(@PathVariable("comicHeadId") String comicId) {

        Long comicHeadId = Long.parseLong(comicId);

        return ResponseEntity.ok().body(artistComicService.getComicList(comicHeadId));
    }

    @GetMapping("artist/comic/content/{comicBodyId}")
    public ResponseEntity<ComicBodyResponse> comicDetail(@PathVariable("comicBodyId") String comicId) {

        Long comicBodyId = Long.parseLong(comicId);

        return ResponseEntity.ok().body(artistComicService.getOneComic(comicBodyId));
    }

    private String generateUniqueFileName(String originalFileName, String fileExtension) {

        String baseName = getBaseName(originalFileName);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        long nanoTime = System.nanoTime();

        return baseName + "_" + timestamp + "_" + nanoTime + fileExtension;
    }

    // 원본 파일명에서 확장자 제외한 이름만 추출
    private String getBaseName(String url) {
        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return url.substring(0, lastDotIndex);
        }
        return url;
    }

    // 파일 확장자 추출
    private String getFileExtension(String url) {
        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return url.substring(lastDotIndex);
        }
        return "";
    }
}
