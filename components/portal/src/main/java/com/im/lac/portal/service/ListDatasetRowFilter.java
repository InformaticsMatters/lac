package com.im.lac.portal.service;

import java.util.List;

public class ListDatasetRowFilter {

    private Long datasetid;
    private List<Long> rowIdList;

    public Long getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(Long datasetid) {
        this.datasetid = datasetid;
    }

    public List<Long> getRowIdList() {
        return rowIdList;
    }

    public void setRowIdList(List<Long> rowIdList) {
        this.rowIdList = rowIdList;
    }
}
