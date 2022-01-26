package com.wangzb.wiki.job;

import com.wangzb.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    /**
     * 自定义cron表达式跑批
     * 只有等上一次执行完成，下一次才会在下一个时间点执行，错过就错过
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档数据结束, 耗时：{}毫秒", System.currentTimeMillis() - start);

    }

}
