package no.nav.oebs.pdlhendelser.quartz;

import no.nav.oebs.pdlhendelser.quartz.HendelseRetryManager;
import org.springframework.stereotype.Component;

import no.nav.oebs.pdlhendelser.db.entity.Livshendelse;
import no.nav.oebs.pdlhendelser.db.repository.LivshendelseRepository;
import no.nav.oebs.pdlhendelser.service.LivshendelseRetryService;

/**
 * Klasse som administrerer rekjøring av livshendelser.
 */
@Component
public class LivshendelseRetryManager extends HendelseRetryManager<Livshendelse> {

	public LivshendelseRetryManager(LivshendelseRetryService retryService, LivshendelseRepository hendelseRepository) {
		super(retryService, hendelseRepository);
	}
}
