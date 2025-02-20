package no.nav.oebs.pdlhendelser.quartz;

import no.nav.oebs.pdlhendelser.quartz.LivshendelseRetryJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Konfigurasjonsklasse for Quartz-jobb for rekjøring av livshendelser som har feilet.
 */
@Component
public class LivshendelseRetryJobConfig {

	public static final String JOB_NAME = "LivshendelseRetryJob";
	public static final String TRIGGER_NAME = "LivshendelseRetryTrigger";

	@Bean
	public JobDetail livshendelseJobDetail() {
		return JobBuilder.newJob() //
				.ofType(LivshendelseRetryJob.class) //
				.storeDurably() //
				.withIdentity(JobKey.jobKey(JOB_NAME)) //
				.build();
	}

	@Bean
	public Trigger livshendelseTrigger(@Qualifier("livshendelseJobDetail") JobDetail job,
			@Value("${app.quartz.job-interval-secs}") int jobIntervalSecs) {
		return TriggerBuilder.newTrigger() //
				.forJob(job) //
				.withIdentity(TriggerKey.triggerKey(TRIGGER_NAME)) //
				.withSchedule(SimpleScheduleBuilder.simpleSchedule() //
						.withIntervalInSeconds(jobIntervalSecs) //
						.withMisfireHandlingInstructionNextWithRemainingCount() //
						.repeatForever()) //
				.build();
	}
}
