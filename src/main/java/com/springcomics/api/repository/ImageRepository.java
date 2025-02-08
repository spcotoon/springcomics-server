package com.springcomics.api.repository;

import com.springcomics.api.domain.comic.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {

    List<Image> findAllByComicBodyId(Long comicBodyId);
}
