package com.econovation.teamponyo.domains.user.events;

import com.econovation.teamponyo.domains.follow.events.Followed;
import com.econovation.teamponyo.domains.follow.events.Unfollowed;
import com.econovation.teamponyo.domains.user.query.port.out.UserSearchPort;
import com.econovation.teamponyo.domains.user.query.model.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class UserEventHandler {
    private final UserSearchPort userSearchPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserCreated event){
        userSearchPort.save(UserDocument.newDoc(event.getUser()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserEdited event){
        UserDocument userDoc = userSearchPort.getById(event.getUser().getUserId());
        userSearchPort.save(UserDocument.of(event.getUser(), userDoc));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TeamJoined event){
        userSearchPort.addTeamId(event.getUserId(), event.getTeamId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TeamWithdrawn event){
        userSearchPort.removeTeamId(event.getUserId(), event.getTeamId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(Followed event){
        UserDocument followeeDoc = userSearchPort.getById(event.getFolloweeId());
        followeeDoc.addFollower(event.getFollowerId());
        UserDocument followerDoc = userSearchPort.getById(event.getFollowerId());
        followerDoc.addFollowing(event.getFolloweeId());
        userSearchPort.save(followeeDoc);
        userSearchPort.save(followerDoc);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(Unfollowed event){
        UserDocument followeeDoc = userSearchPort.getById(event.getFolloweeId());
        followeeDoc.removeFollower(event.getFollowerId());
        UserDocument followerDoc = userSearchPort.getById(event.getFollowerId());
        followerDoc.removeFollowing(event.getFolloweeId());
        userSearchPort.save(followeeDoc);
        userSearchPort.save(followerDoc);
    }
}
