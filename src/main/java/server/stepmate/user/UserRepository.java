package server.stepmate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.stepmate.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    boolean existsByUserId(String userId);

    @Query("select i from User i order by i.monthStep desc")
    List<User> findTop100ByMonthStep();

    @Query("update User u set u.monthStep=0")
    @Modifying(clearAutomatically = true)
    void resetAllUserMonthStep();

    @Query("update User u set u.todayStep=0")
    @Modifying
    void resetAllUserTodayStep();

    @Query("select u from User u order by u.monthStep DESC, u.level DESC, u.nickname asc ")
    Page<User> findAllOrderByMonthStepLevelNickname(Pageable pageable);

    @Query("select u from User u order by u.monthStep DESC, u.level DESC, u.nickname asc ")
    List<User> findAllByOrderByMonthStepDesc();
}