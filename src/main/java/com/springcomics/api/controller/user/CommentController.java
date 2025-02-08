package com.springcomics.api.controller.user;

import com.springcomics.api.request.CommentPost;
import com.springcomics.api.response.CommentResponse;
import com.springcomics.api.service.user.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{comicBodyId}")
    public ResponseEntity<String> postComment(
            @PathVariable("comicBodyId") Long comicBodyId,
            @RequestBody CommentPost commentPost
    ) {
        commentService.postComment(comicBodyId, commentPost);

        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/comment/{comicBodyId}")
    public ResponseEntity<Slice<CommentResponse>> getComment(
            @PathVariable("comicBodyId") Long comicBodyId,
            Pageable pageable
    ) {
        Slice<CommentResponse> comment = commentService.getComment(comicBodyId, pageable);

        return ResponseEntity.ok().body(comment);
    }
}
