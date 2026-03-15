package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;

import lombok.Getter;

@Getter
@ResourceType(name = "Activity.json")
public class ActivityDef extends BaseDef {
    private int Id;
    private int ActivityType;
    //private String StartTime;
    //private String EndTime;
    
    private transient emu.nebula.game.activity.ActivityType type;
    //private transient long startTimeSec;
    //private transient long endTimeSec;
    
    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void onLoad() {
        // Cache activity type
        this.type = emu.nebula.game.activity.ActivityType.getByValue(this.ActivityType);
        
        // Parse start/end times
        /*
        if (this.StartTime != null) {
            var start = OffsetDateTime.parse(this.StartTime);
            this.startTimeSec = start.toEpochSecond();
        }
        
        if (this.EndTime != null) {
            var end = OffsetDateTime.parse(this.EndTime);
            this.endTimeSec = end.toEpochSecond();
        }
        */
    }
}
