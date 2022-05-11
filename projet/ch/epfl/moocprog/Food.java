package ch.epfl.moocprog;
import static java.lang.Math.*;
public final class Food extends Positionable {
    private double Quantity;
    public Food(ToricPosition position,double Quantity){
        super(position);
        if(Quantity<0){
            this.Quantity = 0.0;
        }else{
            this.Quantity = Quantity;
        }
    }
    public double getQuantity() {
        return this.Quantity;
    }

    public double takeQuantity(double prelev) throws IllegalArgumentException {
		if (prelev < 0.0){
            throw new IllegalArgumentException();
        }else{
			double r = min(this.Quantity,prelev);
			this.Quantity -= r;
			return r;
		}	
	}
    public String toString(){
        return super.toString()+"\n"+String.format("Quantity : %.2f", getQuantity());
    }
    
}
