package no.nav.oebs.pdlhendelser.kafka;

import java.time.Duration;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import no.nav.person.pdl.leesah.Personhendelse;

/**
 * Konfigurasjonsklasse for Kafka.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

	// Max antall forsøk på retry når feil kastes tilbake til Spring Kafka fra applikasjonens Kafka-listener.
	@Value("${app.kafka.retry-max-attempts}")
	private int retryMaxAttempts;

	// Antall millisekunder mellom hver retry.
	@Value("${app.kafka.retry-backoff-period-ms}")
	private long retryBackoffPeriod;

	// Antall sekunder mellom hver gang Spring gjør retry ved AuthorizationException fra Kafka.
	@Value("${app.kafka.authorization-exception-retry-interval-secs}")
	private long authorizationExceptionRetryIntervalSecs;

	/**
	 * Kafka listener container factory for livshendelser med følgende egenskaper:
	 * <ul>
	 * <li>ConsumerFactory-instansen er konfigurert med ErrorHandlingDeserializer for håndtering av såkalte poison-meldinger.
	 * Dette er meldinger på ugyldig format som i utgangspunktet vil rulles tilbake til topicen for så å bli lest på nytt og på
	 * nytt og på nytt. Dette blokkerer videre lesing av topicen. Med ErrorHandlingDeserializer unngås dette og meldinger på
	 * ugyldig format vil bli forkastet fortløpende med en enkelt feilmelding (ERROR).</li>
	 * <li>Dersom applikasjonens Kafka-listener kaster exception tilbake til Spring Kafka (evt. feilen oppstår i Spring Kafka),
	 * vil Spring forsøke retryMaxAttempts ganger med retryBackoffPeriod pause mellom hvert forsøk. (Som konfigurert i
	 * SeekToCurrentErrorHandler.) ContainerFactory'en er konfigurert til å bruke stateful retry som betyr at feilen går tilbake
	 * til Kafka-brokeren noe som holder konsumenten i live. Ingen nye hendelser leses fra topicen under retry. Dersom det
	 * fortsatt er feil når retryMaxAttempts er forsøkt, vil hendelsen bli committet og Spring går videre og leser neste
	 * hendelse.</li>
	 * <li>Dersom Kafka-brokeren kaster en AuthorizationException (typisk TopicAuthorizationException), vil Spring utføre retry
	 * med authorizationExceptionRetryIntervalSecs pause mellom hvert forsøk inntil autorisasjonen er reetablert.</li>
	 * </ul>
	 *
	 */

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Personhendelse> kafkaLivshendelseListenerContainerFactory(
			KafkaProperties properties) {

		ConcurrentKafkaListenerContainerFactory<String, Personhendelse> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(kafkaLivshendelseConsumerFactory(properties));
		factory.getContainerProperties()
				.setAuthExceptionRetryInterval(Duration.ofSeconds(authorizationExceptionRetryIntervalSecs));
		factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(retryBackoffPeriod, retryMaxAttempts)));

		return factory;
	}

	private ConsumerFactory<String, Object> kafkaLivshendelseConsumerFactory(KafkaProperties properties) {
		Map<String, Object> consumerProperties = properties.buildConsumerProperties();
		consumerProperties.put("specific.avro.reader", "true");
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}
}
