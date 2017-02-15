package org.arquillian.reporter.api.builder.report;

import java.util.Map;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface ReportBuilder<REPORTTYPE extends Report<REPORTTYPE, ? extends ReportBuilder>, BUILDERTYPE extends ReportBuilder>
    extends Builder {

    REPORTTYPE build();

    BUILDERTYPE addEntry(Entry entry);

    BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap);

    BUILDERTYPE addReport(BUILDERTYPE reportBuilder);

    BUILDERTYPE addReport(REPORTTYPE report);

    BUILDERTYPE addKeyValueEntry(StringKey key, Entry value);

    BUILDERTYPE addKeyValueEntry(StringKey key, String value);

    BUILDERTYPE addKeyValueEntry(String key, String value);

    BUILDERTYPE addKeyValueEntry(StringKey key, int value);

    BUILDERTYPE addKeyValueEntry(StringKey key, boolean runAsClient);

    <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> inSection(
        SECTIONTYPE event);
}
