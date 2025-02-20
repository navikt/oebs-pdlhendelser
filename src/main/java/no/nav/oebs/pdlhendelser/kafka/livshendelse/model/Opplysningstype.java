package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

/**
 * Opplysningstype-verdier i livshendelser. Kun subsettet av verdier som brukes av applikasjonen er definert.
 */
public class Opplysningstype {

	private Opplysningstype() {
	}

	public static final String ADRESSEBESKYTTELSE = "ADRESSEBESKYTTELSE_V1";
	public static final String BOSTEDSADRESSE = "BOSTEDSADRESSE_V1";
	public static final String DOEDSFALL = "DOEDSFALL_V1";
	public static final String FOEDSELSDATO = "FOEDSELSDATO_V1";
	public static final String FOLKEREGISTERIDENTIFIKATOR = "FOLKEREGISTERIDENTIFIKATOR_V1";
	public static final String INNFLYTTING_TIL_NORGE = "INNFLYTTING_TIL_NORGE";
	public static final String KONTAKTADRESSE = "KONTAKTADRESSE_V1";
	public static final String NAVN = "NAVN_V1";
	public static final String UTFLYTTING_FRA_NORGE = "UTFLYTTING_FRA_NORGE";
	public static final String OPPHOLDSADRESSE = "OPPHOLDSADRESSE_V1";
}
