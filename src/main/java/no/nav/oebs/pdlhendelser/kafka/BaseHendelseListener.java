package no.nav.oebs.pdlhendelser.kafka;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import no.nav.oebs.pdlhendelser.logging.LoggingUtils;
import no.nav.oebs.pdlhendelser.mdc.MdcOperations;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.pdlhendelser.db.entity.PdlLogg;
import no.nav.oebs.pdlhendelser.db.repository.PdlLoggRepository;

/**
 * Kafka-basislistener som inneholder basisfunksjonalitet for mottak av hendelser. Kafka-listeneren som leser aktuelle topics
 * skal arve denne klassen.
 */
@Slf4j
public class BaseHendelseListener {

	public static final int STATUS_OK = 900;
	public static final int STATUS_ERROR = 909;

	private PdlLoggRepository pdlLoggRepository;

	public BaseHendelseListener(PdlLoggRepository pdlLoggRepository) {
		this.pdlLoggRepository = pdlLoggRepository;
	}

	/**
	 * Returnerer en epoch millisekund-verdi som en LocalDateTime-verdi.
	 */
	protected LocalDateTime epochToLocalDateTime(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}

	/**
	 * Genererer korrelasjonId-verdien og setter den på MDC-konteksten. Generert verdi returneres.
	 */
	protected String generateAndSetCorrelationId() {
		String correlationId = MdcOperations.generateCorrelationId();
		MdcOperations.put(MdcOperations.MDC_CORRELATION_ID, correlationId);

		return correlationId;
	}

	/**
	 * Logger Kafka-meldingen med tilleggsinformasjon til kalloggen.
	 */
	protected void logToKallLogg(String korrelasjonId, int status, long startTime, String message,
			ConsumerRecord<?, ?> consumerRecord, Exception exception) {

		long endTime = System.currentTimeMillis();

		PdlLogg pdlLoggEntry = PdlLogg.builder() //
				.korrelasjonId(korrelasjonId) //
				.tidspunkt(LocalDateTime.now()) //
				.type(PdlLogg.TYPE_KAFKA) //
				.kallRetning(PdlLogg.RETNING_INN) //
				.operation(consumerRecord.topic()) //
				.status(status) //
				.kalltid(endTime - startTime) //
				.kafkaPartition(consumerRecord.partition()) //
				.kafkaOffset(consumerRecord.offset()) //
				.kafkaTimestamp(epochToLocalDateTime(consumerRecord.timestamp())) //
				.kafkaTimestampType(consumerRecord.timestampType().name()) //
				.kafkaKey(getValueAsString(consumerRecord.key(), PdlLogg.MAX_KAFKA_KEY_LEN)) //
				.request(message) //
				.logginfo(LoggingUtils.formatExceptionAsString(exception)) //
				.build();

		log.debug(pdlLoggEntry.toString());

		saveKallLogg(pdlLoggEntry);
	}

	private String getValueAsString(Object value, int maxLength) {
		String result = null;
		if (value != null) {
			String stringValue = value.toString();
			result = stringValue.substring(0, Math.min(stringValue.length(), maxLength));
		}
		return result;
	}

	private void saveKallLogg(PdlLogg pdlLoggEntry) {
		try {
			pdlLoggRepository.save(pdlLoggEntry);
		} catch (Exception e) {
			log.error("Feil ved logging av kalloggdata til databasen; feilmelding=" + e.getMessage(), e);
		}
	}
}
