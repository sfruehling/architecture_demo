package de.andrena.architecturedemo.domain.privilege.in;

import de.andrena.architecturedemo.domain.privilege.Privilege;

import java.util.List;

public interface FindPrivilegesUsecase {
    List<Privilege> findAllPrivileges();
    Privilege findPrivilegeByDomainId(String domainId);
}
