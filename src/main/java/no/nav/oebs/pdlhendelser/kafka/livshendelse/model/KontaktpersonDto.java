package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.nav.person.pdl.leesah.sikkerhetstiltak.Kontaktperson;

/**
 * Klasse for intern representasjon av et Kontaktperson-objekt i en livshendelse.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class KontaktpersonDto {

	private String personident;

	private String norgEnhet;

	/**
	 * Mapper fra Avro- til Java-objekt.
	 */
	public static KontaktpersonDto map(Kontaktperson kontaktperson) {
		if (kontaktperson == null) {
			return null;
		}
		return KontaktpersonDto.builder() //
				.personident(ModelUtils.getAsString(kontaktperson.getPersonident())) //
				.norgEnhet(ModelUtils.getAsString(kontaktperson.getNorgEnhet())) //
				.build();
	}
}
