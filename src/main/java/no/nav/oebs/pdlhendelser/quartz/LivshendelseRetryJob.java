package no.nav.oebs.pdlhendelser.quartz;

import org.quartz.DisallowConcurrentExecution;
import no.nav.oebs.pdlhendelser.db.entity.Livshendelse;

/**
 * Quartz-jobb for rekjøring av livshendelser som har feilet.
 * <p>
 * Jobben er annotert til <u>ikke</u> å tillate samtidig kjøring i tilfelle eksekveringstiden strekker seg inn i neste
 * kjøreperiode.
 */
@DisallowConcurrentExecution
public class LivshendelseRetryJob extends HendelseRetryJob<Livshendelse> {

	public LivshendelseRetryJob(LivshendelseRetryManager retryManager) {
		super(retryManager);
	}
}
