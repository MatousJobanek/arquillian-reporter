package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.BasicReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BasicReport extends AbstractReport<BasicReport, BasicReportBuilder> {

    public BasicReport() {
    }

    public BasicReport(StringKey name) {
        super(name);
    }

    public BasicReport(String name) {
        super(new UnknownStringKey(name));
    }

    public BasicReport merge(BasicReport newBasicReport) {
        defaultMerge(newBasicReport);
        return this;
    }

    @Override
    public Report addNewReport(Report newReport) {
        getSubReports().add(newReport);
        return newReport;
    }

    public Class<BasicReportBuilder> getReportBuilderClass() {
        return BasicReportBuilder.class;
    }
}
