package ch.epfl.moocprog;
import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.*;
import ch.epfl.moocprog.utils.Vec2d;

public final class ToricPosition {
    
    private final Vec2d position;
    
    public ToricPosition(double x, double y) {
        this.position = clampedPosition(x, y);
    }
    
    public ToricPosition(Vec2d position) {
        this.position = clampedPosition(position.getX(), position.getY());
    }
    
    public ToricPosition() {
        this.position = new Vec2d(0.0, 0.0);
    }
    
    private static Vec2d clampedPosition(double x, double y) {
        
        double ww = getConfig().getInt(WORLD_WIDTH);
        double wh = getConfig().getInt(WORLD_HEIGHT);
        
        while(x<0) {
            x+=ww;
        }
        
        while(x>=ww) {
            x-=ww;
        }
        
        while(y<0) {
            y+=wh;
        }
        
        while(y>=wh) {
            y-=wh;
        }
        
        return new Vec2d(x,y);  
    }
    
    public Vec2d toVec2d() {
        return position;
    }

    public ToricPosition add(ToricPosition that) {
        return new ToricPosition(position.add(that.toVec2d()));
    }
    
    public ToricPosition add(Vec2d vec) {
        return new ToricPosition(position.add(vec));
    }
    
    public Vec2d toricVector(ToricPosition that) {
        
        double ww = getConfig().getInt(WORLD_WIDTH);
        double wh = getConfig().getInt(WORLD_HEIGHT);
        
        Vec2d[] position = new Vec2d[9];
        
        position[0] = that.toVec2d().add(new Vec2d(0,wh));
        position[1] = that.toVec2d().add(new Vec2d(0,-wh));
        position[2] = that.toVec2d().add(new Vec2d(ww,0));
        position[3] = that.toVec2d().add(new Vec2d(-ww,0));
        position[4] = that.toVec2d().add(new Vec2d(ww,wh));
        position[5] = that.toVec2d().add(new Vec2d(ww,-wh));
        position[6] = that.toVec2d().add(new Vec2d(-ww,wh));
        position[7] = that.toVec2d().add(new Vec2d(-ww,-wh));
        position[8] = that.toVec2d();
        
        Vec2d vmin = position[0];
        
        for(int k = 1; k<9; k++) {
            if (this.toVec2d().distance(position[k])<this.toVec2d().distance(vmin)) {
                vmin = position[k];
            }
        }
        return vmin.minus(this.position);       
    }
    
    public double toricDistance(ToricPosition that) {
        return this.toricVector(that).length();
    }

    
    public String toString() {
        return this.toVec2d().toString();
    }
    
}