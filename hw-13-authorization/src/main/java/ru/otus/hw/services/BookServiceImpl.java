package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final MutableAclService mutableAclService;

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d is not present".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        Book book = save(0, title, authorId, genresIds, adultOnly);
        setUpAclFor(book);
        return book;
    }

    @Override
    @Transactional
    public Book update(long id, String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book for update with id %d is not present".formatted(id));
        }
        Book updatedBook = save(id, title, authorId, genresIds, adultOnly);
        setUpAclFor(updatedBook);
        return updatedBook;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException("Book for deletion with id %d is not present".formatted(id));
        }
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds, boolean adultOnly) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres, adultOnly);
        return bookRepository.save(book);
    }

    private MutableAcl setUpAclFor(Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(book);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        acl.setOwner(owner);

        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");
        final Sid user = new GrantedAuthoritySid("ROLE_USER");
        final Sid adult = new GrantedAuthoritySid("ROLE_ADULT_USER");

        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);
        removeAce(acl, BasePermission.READ, user);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, user, !book.isAdultOnly());
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, adult, true);
        return acl;
    }

    private void removeAce(MutableAcl acl, Permission permission, Sid sid) {
        for (int i = 0; i < acl.getEntries().size(); i++) {
            AccessControlEntry ace = acl.getEntries().get(i);
            if (ace.getSid().equals(sid) && ace.getPermission().equals(permission)) {
                acl.deleteAce(i);
                break;
            }
        }
    }
}
