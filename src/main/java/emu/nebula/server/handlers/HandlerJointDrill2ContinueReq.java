package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.JointDrill2Continue.JointDrill2ContinueReq;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.JointDrillActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.joint_drill_2_continue_req)
public class HandlerJointDrill2ContinueReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Get joint drill activity
        var activity = session.getPlayer().getActivityManager().getFirstActivity(JointDrillActivity.class);
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.joint_drill_2_continue_failed_ack);
        }
        
        // Parse request
        var req = JointDrill2ContinueReq.parseFrom(message);
        
        // Continue
        var success = activity.continueDrill(req.getBuildId());
        
        // Check
        if (!success) {
            return session.encodeMsg(NetMsgId.joint_drill_2_continue_failed_ack);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.joint_drill_2_continue_succeed_ack);
    }

}
