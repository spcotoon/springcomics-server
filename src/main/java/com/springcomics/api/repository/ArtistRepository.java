package com.springcomics.api.repository;

import com.springcomics.api.domain.member.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArtistRepository extends CrudRepository<Artist, Long>, ArtistRepositoryCustom{

    Optional<Artist> findByEmailAndPassword(String email, String password);

    Optional<Artist> findByEmail(String email);

    Optional<Artist> findByPenName(String penName);

    Page<Artist> findAll(Pageable pageable);
}
