package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.CharGemUseOverlock.CharGemOverlockReq;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.char_gem_overlock_req)
public class HandlerCharGemOverlockReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse req
        var req = CharGemOverlockReq.parseFrom(message);

        // Get character
        var character = session.getPlayer().getCharacters().getCharacterById(req.getCharId());
        
        if (character == null) {
            return session.encodeMsg(NetMsgId.char_gem_overlock_failed_ack);
        }
        
        // Overlock gem
        var change = character.overlockGem(req.getSlotId(), req.getGemIndex(), req.getAttrIndex());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.char_gem_overlock_failed_ack);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.char_gem_overlock_succeed_ack, change.toProto());
    }

}
