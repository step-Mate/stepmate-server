package server.stepmate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.stepmate.user.entity.Friendship;
import server.stepmate.user.entity.User;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    @Query("select f from Friendship f where f.user.id=:id")
    List<Friendship> findAllByUser(@Param("id") Long id);

    boolean existsByUserAndFriend(User user, User friend);

    void deleteByUserAndFriend(User user, User friend);
}
