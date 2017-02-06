package org.arquillian.reporter.impl.section.nonexisting;

import java.util.List;

import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.dummy.DummyTestClass;
import org.arquillian.reporter.impl.utils.dummy.SecondDummyTestClass;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassNonExistingTreeSectionTest extends AbstractNonExistingTreeSectionTest {

    @Test
    public void testAddClassToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        TestClassSection testClassSection = createTestClassSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                      testClassSection,
                                                      executionReport.getTestSuiteReports(), 1, 3);
        verifyTestClassReportAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }

    @Test
    public void testAddTestClassToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassSection testClassSection = createTestClassSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                      testClassSection,
                                                      executionReport.getTestSuiteReports(),
                                                      EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                      TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);
        verifyTestClassReportAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }

    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         1, 4);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSectionAddedAndGetReport(suiteTree,
                                                                                testClassConfigSection,
                                                                                testClassReports,
                                                                                1, 3);

        verifyConfigAddedInNonExistingSection(classReport.getConfiguration());

    }

    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                         TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 3);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSectionAddedAndGetReport(suiteTree,
                                                                                testClassConfigSection,
                                                                                testClassReports,
                                                                                1, 3);

        verifyConfigAddedInNonExistingSection(classReport.getConfiguration());

    }

    private TestClassSection createTestClassSectionInNonExistingSection() throws Exception {
        return new TestClassSection(
            createReportInNonExistingSection(TestClassReport.class),
            DummyTestClass.class,
            NON_EXISTING_SECTION_NAME);
    }

    private TestClassConfigurationSection createTestClassConfigSectionInNonExistingSection() throws Exception {

        TestClassConfigurationSection testClassConfigurationSection = new TestClassConfigurationSection(
            createReportInNonExistingSection(ConfigurationReport.class),
            SecondDummyTestClass.class,
            SECTION_IN_NON_EXISTING_SECTION_NAME);
        testClassConfigurationSection.setTestSuiteId(NON_EXISTING_SECTION_NAME);

        return testClassConfigurationSection;
    }

    private void verifyTestClassReportAddedInNonExistingSection(List<TestClassReport> classReports) {
        assertThat(classReports).hasSize(1);
        TestClassReport testClassReport = classReports.get(0);

        assertThatReport(testClassReport)
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasGeneratedSubreportsAndEntriesWithDefaults();

        assertThatReport(testClassReport.getConfiguration()).hasNumberOfSubreportsAndEntries(0);
    }
}
