package emu.nebula.game.jointdrill;

import emu.nebula.data.resources.JointDrill2LevelDef;
import emu.nebula.game.inventory.ItemParamMap;
import emu.nebula.game.player.PlayerChangeInfo;
import lombok.Getter;

@Getter
public class JointDrillScore {
    // Scores
    private int fight;
    private int hp;
    private int difficulty;
    private int total;
    
    // Items
    private PlayerChangeInfo change;
    private ItemParamMap rewards;
    
    public JointDrillScore() {
        this.change = new PlayerChangeInfo();
        this.rewards = new ItemParamMap();
    }
    
    public void calculateScore(JointDrill2LevelDef level, int time) {
        // Clamp time
        time = Math.max(time, 0);
        
        // Get time penalty
        int timePenalty = (int) (level.getScorePerSec() * (time / 1000D));
        
        // Calculate scores
        this.difficulty = level.getLevelScore();
        this.hp = level.getBaseHpScore();
        this.fight = Math.max(level.getTimeScore() - timePenalty, 0);
        
        // Calculate total score
        this.total = this.difficulty + this.hp + this.fight;
    }
}
