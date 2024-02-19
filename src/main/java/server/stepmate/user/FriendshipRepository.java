package server.stepmate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.user.entity.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
}
