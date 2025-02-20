package no.nav.oebs.pdlhendelser.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import no.nav.oebs.pdlhendelser.db.entity.PdlLogg;

/**
 * Grensesnitt for repository som håndterer dataaksess mot PdlLogg.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface PdlLoggRepository extends JpaRepository<PdlLogg, Integer>, PdlLoggRepositoryCustom {

}
