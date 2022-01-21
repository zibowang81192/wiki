package com.wangzb.wiki.req;

public class DocQueryReq extends PageReq {

    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    public Long getId() {
        return id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public Long getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Integer getSort() {
        return sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "DocQueryReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                '}';
    }
}