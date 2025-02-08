package com.springcomics.api.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcomics.api.admin.adminDto.ArtistDto;
import com.springcomics.api.admin.adminDto.QArtistDto;
import com.springcomics.api.domain.comic.QComicHead;
import com.springcomics.api.domain.member.QArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArtistDto> findArtistAndSeriesAmount(Pageable pageable) {
        QArtist artist = QArtist.artist;
        QComicHead comicHead = QComicHead.comicHead;

        // 데이터 조회 쿼리
        List<ArtistDto> artistDtos = jpaQueryFactory
                .select(new QArtistDto(
                        artist.id,
                        artist.email,
                        artist.penName,
                        artist.createdDate,
                        comicHead.countDistinct()
                ))
                .from(artist)
                .leftJoin(comicHead).on(artist.id.eq(comicHead.artist.id))
                .groupBy(artist.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(artist.count())
                .from(artist);


        return PageableExecutionUtils.getPage(artistDtos, pageable, countQuery::fetchOne);
    }
}
