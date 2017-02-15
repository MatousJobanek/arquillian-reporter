package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportBuilderImpl<T extends AbstractReport<T,ReportBuilder>> extends
    AbstractReportBuilder<T, ReportBuilderImpl<T>> {

    public ReportBuilderImpl(T sectionReport) {
        super(sectionReport);
    }

}
