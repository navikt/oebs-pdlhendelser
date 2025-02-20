package no.nav.oebs.pdlhendelser.service;

import no.nav.oebs.pdlhendelser.logging.LoggingUtils;
import no.nav.oebs.pdlhendelser.mdc.MdcOperations;
import no.nav.oebs.pdlhendelser.db.entity.Livshendelse;
import no.nav.oebs.pdlhendelser.db.repository.LivshendelseRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Serviceklasse som rekjører livshendelser som har feilet.
 */
@Slf4j
@Service
public class LivshendelseRetryService extends HendelseServiceBase implements HendelseRetryService<Livshendelse> {

	private LivshendelseRepository hendelseRepository;

	public LivshendelseRetryService(ServiceConfig serviceConfig, LivshendelseRepository hendelseRepository) {
		super(serviceConfig);
		this.hendelseRepository = hendelseRepository;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Status som lagres i hendelse-tabellen:
	 * <ul>
	 * <li>BEHANDLET - vellykket retry av hendelsen.</li>
	 * <li>RETRY - dersom det fortsatt oppstår feil under behandling av hendelsen.</li>
	 * <li>FEILET - dersom det fortsatt feiler etter at alle retry-forsøk er oppbrukt.</li>
	 * <li>ERSTATTET - dersom det i mellomtiden har blitt mottatt en nyere hendelse med samme personidenter og
	 * opplysningstype.</li>
	 * </ul>
	 */
	@Override
	public void retryHendelse(Livshendelse livshendelse) {
		try {
			if (isRetryHendelseErstattet(livshendelse)) {
				livshendelse.setStatus(Livshendelse.STATUS_ERSTATTET);
			} else {
				livshendelse.setKorrelasjonId(MdcOperations.get(MdcOperations.MDC_CORRELATION_ID));
				livshendelse.decrementRetryTeller();

				// hendelseFacadeRepository.mottaLivshendelse(livshendelse.getHendelse());

				livshendelse.setStatus(Livshendelse.STATUS_BEHANDLET);
			}
		} catch (Exception e) {
			if (livshendelse.getRetryTeller() <= 0) {
				log.error(String.format(
						"Alle rekjøringsforsøk av livshendelse har feilet og status er endret til FEILET; id=%d, cause=%s",
						livshendelse.getId(), e.getMessage()), e);

				livshendelse.setStatus(Livshendelse.STATUS_FEILET);

				// logLivshendelseError(livshendelse);

			} else {
				livshendelse.setRetryTidspunkt(getNextRetryTidspunkt(livshendelse));

				log.warn(String.format("Feilet under rekjøring av livshendelse; id=%d, neste retrytidspunkt=%s, cause=%s",
						livshendelse.getId(), livshendelse.getRetryTidspunkt(), e.getMessage()), e);
			}

			livshendelse.appendFeilinformasjon(LoggingUtils.formatExceptionAsString(e));
		}
	}

	/**
	 * Sjekker om det finnes nyere hendelser (større ID-verdi) med samme personidenter og opplysningstype som erstatter en
	 * hendelse under retry.
	 */
	private boolean isRetryHendelseErstattet(Livshendelse livshendelse) {
		return !hendelseRepository
				.findByHendelsePersonidenterAndHendelseOpplysningstypeAndIdGreaterThan(livshendelse.getHendelsePersonidenter(),
						livshendelse.getHendelseOpplysningstype(), livshendelse.getId())
				.isEmpty();
	}
}
