package emu.nebula.game.character;

import dev.morphia.annotations.Entity;
import emu.nebula.proto.Public.CharGem;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;

@Getter
@Entity(useDiscriminator = false)
public class CharacterGem {
    private boolean locked;
    private int[] attributes;
    private int[] overlock;
    private int[] alterAttributes;
    private int[] alterOverlock;
    
    @Deprecated // Morphia only
    public CharacterGem() {
        
    }
    
    public CharacterGem(IntList attributes) {
        this.attributes = attributes.toIntArray();
        this.overlock = new int[this.attributes.length];
        this.alterAttributes = new int[4];
        this.alterOverlock = new int[this.alterAttributes.length];
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public void setAttributes(IntList attributes) {
        this.attributes = attributes.toIntArray();
    }

    public void setNewAttributes(IntList attributes, IntSet locked) {
        this.alterAttributes = attributes.toIntArray();
        this.alterOverlock = new int[this.getOverlock().length];
        
        // Copy over locked attributes
        if (locked.size() > 0) {
            for (int i = 0; i < this.getOverlock().length; i++) {
                if (locked.contains(this.alterAttributes[i])) {
                    this.getAlterOverlock()[i] = this.getOverlock()[i];
                }
            }
        }
    }
    
    public boolean hasAlterAttributes() {
        for (int i = 0; i < this.alterAttributes.length; i++) {
            if (this.alterAttributes[i] <= 0) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean replaceAttributes() {
        // Make sure gem has alter attributes
        if (!this.hasAlterAttributes()) {
            return false;
        }
        
        // Replace attributes
        this.attributes = this.alterAttributes;
        this.overlock = this.alterOverlock;
        this.alterAttributes = new int[4];
        this.alterOverlock = new int[this.alterAttributes.length];
        
        // Success
        return true;
    }
    
    public int[] getOverlock() {
        if (this.overlock == null) {
            this.overlock = new int[this.attributes.length];
        }
        
        return this.overlock;
    }
    
    public int[] getAlterOverlock() {
        if (this.alterOverlock == null) {
            this.alterOverlock = new int[this.attributes.length];
        }
        
        return this.alterOverlock;
    }
    
    public int getOverlockCount() {
        int count = 0;
        
        for (int i = 0; i < this.getOverlock().length; i++) {
            count += this.getOverlock()[i];
        }
        
        return count;
    }
    
    // Proto
    
    public CharGem toProto() {
        var proto = CharGem.newInstance()
            .setLock(this.isLocked())
            .addAllAttributes(this.getAttributes())
            .addAllAlterAttributes(this.getAlterAttributes())
            .addAllOverlockCount(this.getOverlock())
            .addAllAlterOverlockCount(this.getAlterOverlock());
        
        return proto;
    }
}
