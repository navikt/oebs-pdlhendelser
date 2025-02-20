package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.nav.person.pdl.leesah.folkeregisteridentifikator.Folkeregisteridentifikator;

/**
 * Klasse for intern representasjon av et Folkeregisteridentifikator-objekt i en livshendelse.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class FolkeregisteridentifikatorDto {

	private String identifikasjonsnummer;

	private String type;

	private String status;

	/**
	 * Mapper fra Avro- til Java-objekt.
	 */
	public static FolkeregisteridentifikatorDto map(Folkeregisteridentifikator folkeregisteridentifikator) {
		if (folkeregisteridentifikator == null) {
			return null;
		}
		return FolkeregisteridentifikatorDto.builder() //
				.identifikasjonsnummer(ModelUtils.getAsString(folkeregisteridentifikator.getIdentifikasjonsnummer())) //
				.type(ModelUtils.getAsString(folkeregisteridentifikator.getType())) //
				.status(ModelUtils.getAsString(folkeregisteridentifikator.getStatus())) //
				.build();
	}
}
