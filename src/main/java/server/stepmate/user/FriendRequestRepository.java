package server.stepmate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import server.stepmate.user.entity.FriendRequest;
import server.stepmate.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findFriendRequestByReceiver(User receiver);

    void deleteBySenderAndReceiver(User sender, User receiver);
}
