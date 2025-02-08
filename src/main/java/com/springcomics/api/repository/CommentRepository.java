package com.springcomics.api.repository;

import com.springcomics.api.domain.comic.ComicComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<ComicComment, Long>, CommentRepositoryCustom {

}
