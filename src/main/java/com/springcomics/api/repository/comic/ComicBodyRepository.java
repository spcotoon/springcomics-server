package com.springcomics.api.repository.comic;

import com.springcomics.api.domain.comic.ComicBody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicBodyRepository extends JpaRepository<ComicBody, Long>, ComicBodyRepositoryCustom {

    List<ComicBody> findAllByComicHeadId(Long comicId);

}
