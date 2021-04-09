package de.andrena.basepackage;

import java.util.List;

public interface FindPrivilegesUsecase {
    List<Privilege> findAllPrivileges();
    Privilege findPrivilegeByDomainId(String domainId);
}
