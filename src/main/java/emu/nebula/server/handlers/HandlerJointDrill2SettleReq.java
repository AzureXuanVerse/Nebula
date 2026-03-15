package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.JointDrill2Settle.JointDrill2SettleReq;
import emu.nebula.proto.PublicJointDrill.JointDrillSettle;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.JointDrillActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.joint_drill_2_settle_req)
public class HandlerJointDrill2SettleReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Get joint drill activity
        var activity = session.getPlayer().getActivityManager().getFirstActivity(JointDrillActivity.class);
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_settle_failed_ack);
        }
        
        // Parse request
        var req = JointDrill2SettleReq.parseFrom(message);
        
        // Settle
        var score = activity.settle(req.getTime(), req.getDamage(), true);
        
        if (score == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_settle_failed_ack);
        }
        
        // Handle client events for achievements
        session.getPlayer().getAchievementManager().handleClientEvents(req.getEvents());
        
        // Build response
        var rsp = JointDrillSettle.newInstance()
                .setFightScore(score.getFight())
                .setDifficultyScore(score.getDifficulty())
                .setHpScore(score.getHp())
                .setChange(score.getChange().toProto());
        
        score.getRewards().toItemTemplateStream().forEach(rsp::addItems);
        
        // Encode and send
        return session.encodeMsg(NetMsgId.joint_drill_2_settle_succeed_ack, rsp);
    }

}
