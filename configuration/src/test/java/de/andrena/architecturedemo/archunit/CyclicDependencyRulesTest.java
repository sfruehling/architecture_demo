package de.andrena.architecturedemo.archunit;


import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "de.andrena.architecturedemo..")
public class CyclicDependencyRulesTest {

    @ArchTest
    public static final ArchRule applicationShouldNotHaveAnyCycles = slices().matching("..(architecturedemo).(**)").should().beFreeOfCycles();

}


