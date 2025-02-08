package com.springcomics.api.repository.comic;

import com.springcomics.api.domain.comic.ComicHead;
import com.springcomics.api.domain.comic.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ComicHeadRepository extends JpaRepository<ComicHead, Long>, ComicHeadRepositoryCustom {

    List<ComicHead> findAllByCreatedBy(String email);

    List<ComicHead> findAllByGenre(Genre genre);
}
