package server.stepmate.rank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.rank.entity.Rank;

public interface RankRepository extends JpaRepository<Rank,Long> {

    @Query("select r from Rank r order by r.ranking asc")
    Page<Rank> findPageRank(Pageable pageable);

    @Query("select r.ranking from Rank r where r.nickname=:nickname")
    Integer findRank(@Param("nickname") String nickname);
}
