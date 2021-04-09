package de.andrena.basepackage;

public interface EditPrivilegesUsecase {
    Privilege saveNewPrivilege(Privilege privilege);

    Privilege updatePrivilege(String domainId, Privilege updatedPrivilege);

    void deletePrivilegeByDomainId(String domainId);
}
