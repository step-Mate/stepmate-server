package server.stepmate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.user.entity.Friendship;
import server.stepmate.user.entity.User;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    List<Friendship> findAllByUser(User user);
}
