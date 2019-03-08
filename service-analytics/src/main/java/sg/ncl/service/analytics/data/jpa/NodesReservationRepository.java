package sg.ncl.service.analytics.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.analytics.data.jpa.NodesReservationEntity;

public interface NodesReservationRepository extends JpaRepository<NodesReservationEntity, Long> {
}
