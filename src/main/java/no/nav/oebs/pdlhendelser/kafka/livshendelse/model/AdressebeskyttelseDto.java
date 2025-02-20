package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.nav.person.pdl.leesah.adressebeskyttelse.Adressebeskyttelse;

/**
 * Klasse for intern representasjon av et Adressebeskyttelse-objekt i en livshendelse.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class AdressebeskyttelseDto {

	private String gradering;

	/**
	 * Mapper fra Avro- til Java-objekt.
	 */
	public static AdressebeskyttelseDto map(Adressebeskyttelse adressebeskyttelse) {
		if (adressebeskyttelse == null) {
			return null;
		}
		return AdressebeskyttelseDto.builder() //
				.gradering(adressebeskyttelse.getGradering().name()) //
				.build();
	}
}
