package com.wangzb.wiki.mapper;

import com.wangzb.wiki.domain.Test;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface DocMapperCust {

    public void increaseViewCount(@Param("id") Long id);
    public void increaseVoteCount(@Param("id") Long id);
    public void updateEbookInfo();

}
