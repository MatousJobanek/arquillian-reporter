package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportBuilderImpl extends AbstractReportBuilder<TestClassReport, TestClassReportBuilder> implements
    TestClassReportBuilder {

    public TestClassReportBuilderImpl(TestClassReport sectionReport) {
        super(sectionReport);
    }

    public TestClassReportBuilderImpl stop() {
        getReport().setStop(ReporterUtils.getCurrentDate());
        return this;
    }
}