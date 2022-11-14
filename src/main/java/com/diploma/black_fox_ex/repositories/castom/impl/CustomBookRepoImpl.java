package com.diploma.black_fox_ex.repositories.castom.impl;

import com.diploma.black_fox_ex.dto.book.BookEditDTO;
import com.diploma.black_fox_ex.dto.book.BookLookDTO;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDTO;
import com.diploma.black_fox_ex.model.QBook;
import com.diploma.black_fox_ex.model.QUser;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.repositories.castom.CustomBookRepo;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class CustomBookRepoImpl implements CustomBookRepo {

    private final JPAQueryFactory queryFactory;

    private final QBook qBook = QBook.book;
    private final QUser qUser = QUser.user;
    private final Predicate bookIsNotDel = qBook.dateTimeDelete.isNull();

    public CustomBookRepoImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> getBooksIdByGenre(Genre genre, int skipRows, int sizeLimit) {
        return getBooksId(qBook.genre.eq(genre), skipRows, sizeLimit);
    }

    @Override
    public List<Long> getBooksIdByUser(User user, int skipRows, int sizeLimit) {
        return getBooksId(qBook.author.eq(user), skipRows, sizeLimit);
    }

    public List<Long> getFavoriteBooksId(User user, int skipRows, int sizeLimit) {
        return getBooksId(qBook.likes.contains(user), skipRows, sizeLimit);
    }

    @Override
    public List<Long> getBooksIdAllGenres(int skipRows, int sizeLimit) {
        return getBooksId(null, skipRows, sizeLimit);
    }

    @Override
    public Set<Long> getLikesIdByBookId(Long id) {
        return Optional.ofNullable(queryFactory
                        .select(qBook.likes)
                        .from(qBook)
                        .where(qBook.id.eq(id), qUser.dateTimeDelete.isNull())
                        .leftJoin(qBook.likes, qUser)
                        .fetchFirst()).orElse(new HashSet<>())
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public ReferenceBookDTO getReferenceBook(long id) {
        return queryFactory
                .select(Projections.constructor(
                        ReferenceBookDTO.class, qBook.id, qBook.title, qBook.genre,
                        qBook.filenameBg, qBook.bigText.substring(0, 100),
                        qBook.likes.size()))
                .from(qBook)
                .where(qBook.id.eq(id))
                .fetchFirst();
    }

    @Override
    public int getPageCountByBookId(Long bookId, int maxSymbols) {
        return queryFactory
                .select(qBook.bigText.length().divide(maxSymbols).ceil())
                .from(qBook)
                .where(qBook.id.eq(bookId), bookIsNotDel)
                .fetchFirst();
    }

    @Override
    public BookEditDTO getBookEditDTO(Long bookId) {
        return queryFactory
                .select(Projections.constructor(BookEditDTO.class,
                        qBook.title, qBook.genre, qBook.bigText, qBook.author))
                .from(qBook)
                .where(qBook.id.eq(bookId), bookIsNotDel)
                .fetchFirst();
    }

    @Override
    public BookLookDTO getSplitBookDTO(Long bookId, int fromSymbols, int toSymbols) {
        return queryFactory
                .select(Projections.constructor(
                        BookLookDTO.class, qBook.title, qBook.genre,
                        qBook.bigText.substring(fromSymbols, toSymbols)))
                .from(qBook)
                .where(qBook.id.eq(bookId), bookIsNotDel)
                .fetchFirst();
    }

    /**
     * This method gets all book ids in the given range
     * search was performed by sorted id
     *
     * @param predicate initial condition
     * @param skipRows  skip first searching elem
     * @param sizeLimit number of elements
     * @return list of book ids
     */
    private List<Long> getBooksId(Predicate predicate, int skipRows, int sizeLimit) {
        return queryFactory
                .select(qBook.id)
                .from(qBook)
                .where(predicate, bookIsNotDel)
                .orderBy(qBook.id.desc())
                .limit(sizeLimit)
                .offset(skipRows)
                .fetch();
    }
}