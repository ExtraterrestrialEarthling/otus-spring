package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Book;

@Service
@RequiredArgsConstructor
public class AclServiceImpl implements AclService {

    private final MutableAclService mutableAclService;

    public MutableAcl setUpAclFor(Book book) {
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
