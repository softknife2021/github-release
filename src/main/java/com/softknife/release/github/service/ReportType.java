package com.softknife.release.github.service;

/**
 * @author amatsaylo on 2019-07-22
 * @project release-notes
 */
public enum ReportType {

    CSV("csv-table"),
    MARKUP_TBL("markup-table"),
    HTML("html-table");

    private final String name;

    /**
     * @param name
     */
    ReportType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
