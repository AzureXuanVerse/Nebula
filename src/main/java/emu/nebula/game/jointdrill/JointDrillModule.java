package emu.nebula.game.jointdrill;

import java.util.ArrayList;
import java.util.List;

import emu.nebula.Nebula;
import emu.nebula.game.GameContext;
import emu.nebula.game.GameContextModule;
import emu.nebula.proto.JointDrillRank.JointDrillRankData;

@SuppressWarnings("unused")
public class JointDrillModule extends GameContextModule {
    private long lastUpdate;
    private long nextUpdate;
    private List<JointDrillRankData> ranking;
    
    public JointDrillModule(GameContext context) {
        super(context);
        this.nextUpdate = -1;
        this.ranking = new ArrayList<>();
    }
    
    private long getRefreshTime() {
        return Nebula.getConfig().getServerOptions().leaderboardRefreshTime * 1000;
    }
    
    public synchronized List<JointDrillRankData> getRanking(int activityId) {
        if (System.currentTimeMillis() > this.nextUpdate) {
            this.updateRanking(activityId);
        }
        
        return this.ranking;
    }

    // Cache ranking so we dont query the database too much
    private void updateRanking(int activityId) {
        // Clear
        this.ranking.clear();
        
        // Get from database
        var list = Nebula.getGameDatabase().getSortedObjects(JointDrillRankEntry.class, "activityId", activityId, "score", 50);
        
        for (int i = 0; i < list.size(); i++) {
            // Get rank entry and set proto
            var entry = list.get(i);
            entry.setRank(i + 1);
            
            // Add to ranking
            this.ranking.add(entry.toProto());
        }
        
        this.nextUpdate = System.currentTimeMillis() + this.getRefreshTime();
        this.lastUpdate = Nebula.getCurrentServerTime();
    }
}
