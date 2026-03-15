package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.JointDrillRank.JointDrillRankInfo;
import emu.nebula.net.HandlerId;
import emu.nebula.Nebula;
import emu.nebula.game.activity.type.JointDrillActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.joint_drill_rank_req)
public class HandlerJointDrillRankReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Get joint drill activity
        var activity = session.getPlayer().getActivityManager().getFirstActivity(JointDrillActivity.class);
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.joint_drill_rank_failed_ack);
        }
        
        // Build response
        var rsp = JointDrillRankInfo.newInstance()
                .setLastRefreshTime(Nebula.getCurrentServerTime());
        
        // Get self
        var self = activity.getRankEntry();
        
        if (self != null) {
            rsp.setSelf(self.toProto());
        }
        
        // Get ranking
        var ranking = Nebula.getGameContext().getJointDrillModule().getRanking(activity.getId());
        
        for (var entry : ranking) {
            // Check self
            if (self != null && self.getPlayerUid() == entry.getId()) {
                rsp.getMutableSelf().setRank(entry.getRank());
            }
            
            // Add to ranking
            rsp.addRank(entry);
        }
        
        // Set total
        rsp.setTotal(ranking.size());
        
        // Encode and send
        return session.encodeMsg(NetMsgId.joint_drill_rank_succeed_ack, rsp);
    }

}
