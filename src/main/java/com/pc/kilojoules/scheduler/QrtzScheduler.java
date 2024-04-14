package com.pc.kilojoules.scheduler;


import com.pc.kilojoules.config.AutoWiringSpringBeanJobFactory;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
@ConditionalOnExpression("'${using.spring.schedulerFactory}'=='false'")

public class QrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Quartz...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public Scheduler scheduler1(Trigger trigger1, JobDetail jobDetail1, SchedulerFactoryBean factory) throws SchedulerException {
        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.scheduleJob(jobDetail1, trigger1);
        scheduler.start();
        return scheduler;
    }

    @Bean
    public Scheduler scheduler2(Trigger trigger2, JobDetail jobDetail2, SchedulerFactoryBean factory) throws SchedulerException {
        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.scheduleJob(jobDetail2, trigger2);
        scheduler.start();
        return scheduler;
    }

    @Bean
    public JobDetail jobDetail1() {

        return newJob().ofType(CleanUpJob.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey("OneTime_CleanUp_Job_Detail"))
                .withDescription("Invoke CleanUp Job service...")
                .build();
    }
    @Bean
    public JobDetail jobDetail2() {

        return newJob().ofType(CleanUpJob.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey("CleanUp_Job_Detail"))
                .withDescription("Invoke CleanUp Job service...")
                .build();
    }

    @Bean
    public Trigger trigger1() {

        return newTrigger()
                .forJob(jobDetail1())
                .withIdentity(TriggerKey.triggerKey("SimpleTrigger"))
                .withDescription("SimpleTrigger for CleanUp Unsaved JournalMeals")
                .startAt(DateUtils.addSeconds(new Date(), 30))
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(30)
//                        .withIntervalInHours(12)
//                        .repeatForever())
                        .withRepeatCount(0))
                .build();
    }

    @Bean
    public Trigger trigger2() {

    return newTrigger()
            .forJob(jobDetail2())
            .withIdentity(TriggerKey.triggerKey("CronTriggerCleanUp"))
            .withDescription("CronTrigger for CleanUp Unsaved JournalMeals")
            .startNow()
            .withSchedule(dailyAtHourAndMinute(0, 15)
                .withMisfireHandlingInstructionFireAndProceed())
            .build();
    }
}
