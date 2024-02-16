package server.stepmate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.user.entity.DailyStep;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStepRepository extends JpaRepository<DailyStep,Long> {

    @Query("select d from DailyStep d where d.user.id=:id order by d.date asc")
    List<DailyStep> findUserDailyStep(@Param("id") Long id);

    @Query("select d from DailyStep d where d.user.id=:id and d.date=:date")
    Optional<DailyStep> findDailyStepByDate(Long id, LocalDate date);
}
