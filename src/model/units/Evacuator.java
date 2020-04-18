package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;

public class Evacuator extends PoliceUnit{
	
	public Evacuator(String id, Address location, int stepsPerCycle,WorldListener wl, int maxCapacity) {
		super(id,location,stepsPerCycle,wl,maxCapacity);
	}
	public void treat(){
		//super.treat();
		if(this.getLocation()==this.getTarget().getLocation()) {
			if(((ResidentialBuilding)this.getTarget()).getOccupants().size()==0) {
				this.jobsDone();
			}
			else {
				while(((ResidentialBuilding)this.getTarget()).getOccupants().size()>0 && getPassengers().size()<getMaxCapacity()) {
					this.getPassengers().add(((ResidentialBuilding)this.getTarget()).getOccupants().remove(0));
				}
			}
		}
		else {
			if(getDistanceToBase()==0) {
				while(getPassengers().size()>0){
					Citizen x = (getPassengers().remove(0));
					this.getWorldListener().assignAddress(x, 0, 0);
					x.setState(CitizenState.RESCUED);
				}
			}
		}
	}

}
