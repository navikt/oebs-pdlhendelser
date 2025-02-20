package no.nav.oebs.pdlhendelser.kafka.livshendelse.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.nav.person.pdl.leesah.utflytting.UtflyttingFraNorge;

/**
 * Klasse for intern representasjon av et UtflyttingFraNorge-objekt i en livshendelse.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class UtflyttingFraNorgeDto {

	private String tilflyttingsland;

	private String tilflyttingsstedIUtlandet;

	private LocalDate utflyttingsdato;

	/**
	 * Mapper fra Avro- til Java-objekt.
	 */
	public static UtflyttingFraNorgeDto map(UtflyttingFraNorge utflyttingFraNorge) {
		if (utflyttingFraNorge == null) {
			return null;
		}
		return UtflyttingFraNorgeDto.builder() //
				.tilflyttingsland(ModelUtils.getAsString(utflyttingFraNorge.getTilflyttingsland())) //
				.tilflyttingsstedIUtlandet(ModelUtils.getAsString(utflyttingFraNorge.getTilflyttingsstedIUtlandet())) //
				.utflyttingsdato(utflyttingFraNorge.getUtflyttingsdato()) //
				.build();
	}
}
