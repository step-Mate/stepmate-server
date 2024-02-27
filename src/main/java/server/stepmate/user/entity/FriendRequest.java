package server.stepmate.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.stepmate.user.dto.FriendRequestDto;
import server.stepmate.user.entity.enumtypes.RequestStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public FriendRequestDto getFriendRequestDto() {
        return new FriendRequestDto(sender.getNickname());
    }
}
