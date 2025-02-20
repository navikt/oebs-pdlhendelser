package no.nav.oebs.pdlhendelser.db.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import no.nav.oebs.pdlhendelser.db.entity.PdlLogg;

/**
 * Implementasjonsklasse for {@link PdlLoggRepositoryCustom}.
 */
@Repository
public class PdlLoggRepositoryImpl implements PdlLoggRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void pingKallLogg() {
		entityManager.createQuery("SELECT k.id FROM PdlLogg k WHERE k.id = 0", PdlLogg.class) //
				.getResultList();
	}
}
