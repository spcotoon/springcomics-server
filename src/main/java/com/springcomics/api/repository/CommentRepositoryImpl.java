package com.springcomics.api.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcomics.api.admin.adminDto.CommentDto;
import com.springcomics.api.admin.adminDto.QCommentDto;
import com.springcomics.api.domain.comic.QComicBody;
import com.springcomics.api.domain.comic.QComicComment;
import com.springcomics.api.response.CommentResponse;
import com.springcomics.api.response.QCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final QComicComment qcomicComment = QComicComment.comicComment;

    private final QComicBody qcomicBody = QComicBody.comicBody;

    @Override
    public Slice<CommentResponse> findSliceByComicBodyId(Long comicBodyId, Pageable pageable) {

        List<CommentResponse> content = jpaQueryFactory
                .select(new QCommentResponse(
                        qcomicComment.comment,
                        qcomicComment.createdDate,
                        qcomicComment.userNicknameOfComment,
                        qcomicComment.createdBy
                ))
                .from(qcomicComment)
                .where(qcomicBody.id.eq(comicBodyId))
                .leftJoin(qcomicComment.comicBody, qcomicBody)
                .orderBy(qcomicComment.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();

        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Page<CommentDto> findCommentDtosByComicBodyId(Pageable pageable, Long comicBodyId) {
        List<CommentDto> content = jpaQueryFactory.select(new QCommentDto(
                        qcomicComment.id,
                        qcomicComment.userNicknameOfComment,
                        qcomicComment.comment,
                        qcomicComment.createdDate
                ))
                .from(qcomicComment)
                .where(qcomicComment.comicBody.id.eq(comicBodyId))
                .leftJoin(qcomicComment.comicBody, qcomicBody)
                .orderBy(qcomicComment.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(qcomicComment.count())
                .from(qcomicComment)
                .where(qcomicComment.comicBody.id.eq(comicBodyId));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);


    }


}
