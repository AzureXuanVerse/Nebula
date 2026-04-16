package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.Public.UI32;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.char_favorite_set_req)
public class HandlerCharFavoriteSetReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = UI32.parseFrom(message);
        
        // Get character
        var character = session.getPlayer().getCharacters().getCharacterById(req.getValue());
        
        if (character == null) {
            return session.encodeMsg(NetMsgId.char_favorite_set_failed_ack);
        }
        
        // Toggle favorite status
        character.setFavorite(!character.isFavorite());
        character.save();
        
        // Encode and send
        return session.encodeMsg(NetMsgId.char_favorite_set_succeed_ack);
    }

}
