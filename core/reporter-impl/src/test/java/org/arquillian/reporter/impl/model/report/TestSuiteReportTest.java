package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_CLASS_NAME;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_SUITE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportTest {

    @Test
    public void testAddNewReportToTestSuiteReport() throws Exception {
        TestSuiteReport testSuiteReport =
            ReportGeneratorUtils.prepareReport(TestSuiteReport.class, TEST_SUITE_NAME, 1, 5);

        // add test class report - should be added into List of test class reports
        TestClassReport testClassReportToAdd =
            ReportGeneratorUtils.prepareReport(TestClassReport.class, TEST_CLASS_NAME, 3, 8);
        testSuiteReport.addNewReport(testClassReportToAdd);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "test suite config", 1, 5);
        testSuiteReport.addNewReport(configurationReportToAdd);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        testSuiteReport.addNewReport(basicReport);

        // add another test class report - should be added into List of test class reports
        TestClassReport secondTestClassReportToAdd =
            ReportGeneratorUtils.prepareReport(TestClassReport.class, "second test class", 3, 8);
        testSuiteReport.addNewReport(secondTestClassReportToAdd);

        // add first test class report for the second time - should be added into List of test class reports
        testSuiteReport.addNewReport(testClassReportToAdd);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testSuiteReport)
                .hasName(TEST_SUITE_NAME)
                .hasSubReportsWithout(testClassReportToAdd, configurationReportToAdd, secondTestClassReportToAdd)
                .hasSubReportsEndingWith(basicReport)
                .hasGeneratedSubreportsAndEntries(1, 5)
                .hasNumberOfSubreports(5)
                .hasNumberOfEntries(4);

            assertThat(testSuiteReport.getConfiguration().getSubReports()).containsExactly(configurationReportToAdd);
            assertThat(testSuiteReport.getTestClassReports())
                .containsExactly(testClassReportToAdd, secondTestClassReportToAdd, testClassReportToAdd);
        });
    }

    @Test
    public void testNewTestSuiteReportShouldContainStartDate() throws ParseException {
        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return new TestSuiteReport().getStart();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testStopDateUsingTestSuiteBuilder() throws ParseException {
        BuilderLoader.load();
        TestSuiteReport testSuiteReport = new TestSuiteReport();
        assertThat(testSuiteReport.getStop()).isNull();

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return Reporter.createReport(testSuiteReport).stop().build().getStop();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testMergeReports() throws Exception {
        // prepare main
        TestSuiteReport mainTestSuiteReport = ReportGeneratorUtils
            .prepareReport(TestSuiteReport.class, "main test suite", 1, 5);
        // add test classes
        List<TestClassReport> firstTestClasses =
            ReportGeneratorUtils.prepareSetOfReports(TestClassReport.class, 5, "first classes", 1, 5);
        mainTestSuiteReport.getTestClassReports().addAll(firstTestClasses);
        // and config
        List<ConfigurationReport> firstConfigs =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestSuiteReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestSuiteReport testSuiteReportToMerge = ReportGeneratorUtils
            .prepareReport(TestSuiteReport.class, "to merge", 5, 10);
        // add test classes
        List<TestClassReport> classesToMerge =
            ReportGeneratorUtils.prepareSetOfReports(TestClassReport.class, 5, "second classes", 5, 10);
        testSuiteReportToMerge.getTestClassReports().addAll(classesToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testSuiteReportToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestSuiteReport.merge(testSuiteReportToMerge);

        // the report that has been merged is still same
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testSuiteReportToMerge)
                .hasName("to merge")
                .hasGeneratedSubreportsAndEntries(5, 10)
                .hasNumberOfSubreportsAndEntries(5);

            assertThat(testSuiteReportToMerge.getTestClassReports()).isEqualTo(classesToMerge);
            assertThat(testSuiteReportToMerge.getConfiguration().getSubReports()).isEqualTo(configsToMerge);
        });

        // the main report should contain all information
        firstTestClasses.addAll(classesToMerge);
        firstConfigs.addAll(configsToMerge);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(mainTestSuiteReport)
                .hasName("main test suite")
                .hasGeneratedSubreportsAndEntries(1, 10)
                .hasNumberOfSubreportsAndEntries(9);

            assertThat(mainTestSuiteReport.getTestClassReports()).isEqualTo(firstTestClasses);
            assertThat(mainTestSuiteReport.getConfiguration().getSubReports()).isEqualTo(firstConfigs);
        });
    }
}
