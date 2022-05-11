package ch.epfl.moocprog;

public class Positionable {
    private ToricPosition toric;
    public Positionable(){
        toric = new ToricPosition();
    }
    public Positionable(double x,double y){
        toric = new ToricPosition(x, y);
    }
    public Positionable(ToricPosition toric) {
        this.toric = new ToricPosition(toric.toVec2d().getX(),toric.toVec2d().getY());
    }
    public ToricPosition getPosition(){
        return this.toric;
    }
    protected final void setPosition(ToricPosition position){
        this.toric = position;
    }
    public String toString(){
        return toric.toString();
    }
}
