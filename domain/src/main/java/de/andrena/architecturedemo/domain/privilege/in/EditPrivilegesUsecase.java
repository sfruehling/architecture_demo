package de.andrena.architecturedemo.domain.privilege.in;

import de.andrena.architecturedemo.domain.privilege.Privilege;

public interface EditPrivilegesUsecase {
    Privilege saveNewPrivilege(Privilege privilege);

    Privilege updatePrivilege(String domainId, Privilege updatedPrivilege);

    void deletePrivilegeByDomainId(String domainId);
}
