package emu.nebula.game.jointdrill;

import emu.nebula.game.tower.StarTowerBuild;
import lombok.Getter;

@Getter
public class JointDrillBuild {
    private StarTowerBuild build;
    private int time;
    private int damage;
    
    public JointDrillBuild(StarTowerBuild build, int time, int damage) {
        this.build = build;
        this.time = time;
        this.damage = damage;
    }
}
