package server.stepmate.rank;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.rank.entity.Rank;

public interface RankRepository extends JpaRepository<Rank,Long> {
}
