package ru.otus.hw.services;

import org.springframework.security.acls.model.MutableAcl;
import ru.otus.hw.models.Book;

public interface AclService {

    MutableAcl setUpAclFor(Book book);
}
