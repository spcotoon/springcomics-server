package com.springcomics.api.repository;

import com.springcomics.api.admin.adminDto.ArtistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistRepositoryCustom {

    Page<ArtistDto> findArtistAndSeriesAmount(Pageable pageable);

}
