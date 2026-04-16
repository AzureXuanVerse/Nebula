package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.CharGemUseOverlockRevert.CharGemOverlockRevertReq;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.char_gem_overlock_revert_req)
public class HandlerCharGemOverlockRevertReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse req
        var req = CharGemOverlockRevertReq.parseFrom(message);
        
        // Get character
        var character = session.getPlayer().getCharacters().getCharacterById(req.getCharId());
        
        if (character == null) {
            return session.encodeMsg(NetMsgId.char_gem_overlock_revert_failed_ack);
        }
        
        // Overlock gem
        var change = character.revertOverlockGem(req.getSlotId(), req.getGemIndex(), req.getAttrIndex());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.char_gem_overlock_revert_failed_ack);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.char_gem_overlock_revert_succeed_ack, change.toProto());
    }

}
