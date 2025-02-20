package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.nav.person.pdl.leesah.navn.OriginaltNavn;

/**
 * Klasse for intern representasjon av et OriginaltNavn-objekt i en livshendelse.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class OriginaltNavnDto {

	private String fornavn;

	private String mellomnavn;

	private String etternavn;

	/**
	 * Mapper fra Avro- til Java-objekt.
	 */
	public static OriginaltNavnDto map(OriginaltNavn originaltNavn) {
		if (originaltNavn == null) {
			return null;
		}
		return OriginaltNavnDto.builder() //
				.fornavn(ModelUtils.getAsString(originaltNavn.getFornavn())) //
				.mellomnavn(ModelUtils.getAsString(originaltNavn.getMellomnavn())) //
				.etternavn(ModelUtils.getAsString(originaltNavn.getEtternavn())) //
				.build();
	}
}
