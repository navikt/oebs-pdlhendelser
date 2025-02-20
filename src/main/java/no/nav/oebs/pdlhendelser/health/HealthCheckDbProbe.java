package no.nav.oebs.pdlhendelser.health;

import org.springframework.stereotype.Component;

import no.nav.oebs.pdlhendelser.db.repository.PdlLoggRepository;

/**
 * Helsesjekk som brukes for å sjekke at databasen er tilgjengelig for applikasjonen.
 */
@Component
public class HealthCheckDbProbe {

	private PdlLoggRepository pdlLoggRepository;

	HealthCheckDbProbe(PdlLoggRepository pdlLoggRepository) {
		this.pdlLoggRepository = pdlLoggRepository;
	}

	/**
	 * Pinger databasen ved å forsøke en spørring mot kall-loggen, men henter ingen data.
	 */
	public void pingDatabase() {
		pdlLoggRepository.pingKallLogg();
	}
}
