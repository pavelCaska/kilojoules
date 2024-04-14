package com.pc.kilojoules.scheduler;

import com.pc.kilojoules.services.JournalMealService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanUpJob implements Job {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JournalMealService journalMealService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        journalMealService.deleteUnsavedJournalMeals();
    }
}
