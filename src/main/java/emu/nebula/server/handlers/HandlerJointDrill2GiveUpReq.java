package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.JointDrill2GiveUp.JointDrill2GiveUpReq;
import emu.nebula.proto.PublicJointDrill.JointDrillSettle;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.JointDrillActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.joint_drill_2_give_up_req)
public class HandlerJointDrill2GiveUpReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Get joint drill activity
        var activity = session.getPlayer().getActivityManager().getFirstActivity(JointDrillActivity.class);
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_give_up_failed_ack);
        }
        
        // Parse request
        var req = JointDrill2GiveUpReq.parseFrom(message);
        
        // Give up
        var score = activity.giveup(req.getTime(), req.getDamage());
        
        if (score == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_give_up_failed_ack);
        }
        
        // Build response
        var rsp = JointDrillSettle.newInstance()
                .setFightScore(score.getFight())
                .setDifficultyScore(score.getDifficulty())
                .setHpScore(score.getHp())
                .setChange(score.getChange().toProto());
        
        // Encode and send
        return session.encodeMsg(NetMsgId.joint_drill_2_give_up_succeed_ack, rsp);
    }

}
