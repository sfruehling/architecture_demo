package de.andrena.architecturedemo.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {
    private final JavaClasses classes = new ClassFileImporter().importPackages("de.andrena.architecturedemo");

    @Test
    void core_should_not_access_rest_layer() {
        noClasses().that().resideInAPackage("..core..").should().accessClassesThat().resideInAPackage("..rest..")
                .check(classes);
    }

    @Test
    void core_should_not_access_persistence_layer() {
        noClasses().that().resideInAPackage("..core..").should().accessClassesThat().resideInAPackage("..persistence..")
                .check(classes);
    }

    @Test
    void rest_should_not_access_persistence_layer() {
        noClasses().that().resideInAPackage("..rest..").should().accessClassesThat().resideInAPackage("..persistence..")
                .check(classes);
    }

    @Test
    void persistence_should_not_access_rest_layer() {
        noClasses().that().resideInAPackage("..persistence..").should().accessClassesThat().resideInAPackage("..rest..")
                .check(classes);
    }

    @Test
    void core_should_not_access_config_layer() {
        noClasses().that().resideInAPackage("..core..").should().accessClassesThat().resideInAPackage("..config..")
                .check(classes);
    }

    @Test
    void persistence_should_not_access_config_layer() {
        noClasses().that().resideInAPackage("..persistence..").should().accessClassesThat()
                .resideInAPackage("..config..").check(classes);
    }

    @Test
    void rest_should_not_access_config_layer() {
        noClasses().that().resideInAPackage("..rest..").should().accessClassesThat().resideInAPackage("..config..")
                .check(classes);
    }

    @Test
    void config_should_not_access_rest_layer() {
        noClasses().that().resideInAPackage("..config..").should().accessClassesThat().resideInAPackage("..rest..")
                .check(classes);
    }

    @Test
    void config_should_not_access_persist_layer() {
        noClasses().that().resideInAPackage("..config..").should().accessClassesThat()
                .resideInAPackage("..persistence..").check(classes);
    }


}
