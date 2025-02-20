package no.nav.oebs.pdlhendelser.db.repository;

/**
 * Egendefinerte metoder for PdlLogg-repository.
 */
public interface PdlLoggRepositoryCustom {

	/**
	 * Kjører en select mot tabellen PdlLogg, uten å finne noen rader. Vil feile hvis databasen ikke er tilgjengelig.
	 */
	void pingKallLogg();
}
