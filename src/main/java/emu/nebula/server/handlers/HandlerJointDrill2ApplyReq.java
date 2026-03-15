package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.JointDrill2Apply.JointDrill2ApplyReq;
import emu.nebula.proto.JointDrill2Apply.JointDrill2ApplyResp;
import emu.nebula.net.HandlerId;
import emu.nebula.Nebula;
import emu.nebula.game.activity.type.JointDrillActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.joint_drill_2_apply_req)
public class HandlerJointDrill2ApplyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Get joint drill activity
        var activity = session.getPlayer().getActivityManager().getFirstActivity(JointDrillActivity.class);
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_apply_failed_ack);
        }
        
        // Parse request
        var req = JointDrill2ApplyReq.parseFrom(message);
        
        // Apply for joint drill stage
        var change = activity.apply(req.getLevelId(), req.getBuildId(), req.getSimulate());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_apply_failed_ack);
        }
        
        // Create response packet
        var rsp = JointDrill2ApplyResp.newInstance()
                .setStarTime(Nebula.getCurrentServerTime() + activity.getLevel().getBattleTime())
                .setChange(change.toProto());
        
        // Encode and send
        return session.encodeMsg(NetMsgId.joint_drill_2_apply_succeed_ack, rsp);
    }

}
