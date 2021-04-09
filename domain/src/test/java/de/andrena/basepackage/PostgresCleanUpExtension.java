package de.andrena.basepackage;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class PostgresCleanUpExtension implements BeforeEachCallback, AfterEachCallback {
    
    @Override
    public void afterEach(ExtensionContext extensionContext) {
        deleteRepositoryContent(extensionContext);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        deleteRepositoryContent(extensionContext);
    }

    private void deleteRepositoryContent(ExtensionContext extensionContext) {
        UserRepository userRepository = getBean(extensionContext, UserRepository.class);
        userRepository.deleteAll();

        RoleRepository roleRepository = getBean(extensionContext, RoleRepository.class);
        roleRepository.deleteAll();

        PrivilegeRepository privilegeRepository = getBean(extensionContext, PrivilegeRepository.class);
        privilegeRepository.deleteAll();
    }

    private <T> T getBean(ExtensionContext extensionContext, Class<T> var1) {
        return SpringExtension.getApplicationContext(extensionContext).getBean(var1);
    }
}
