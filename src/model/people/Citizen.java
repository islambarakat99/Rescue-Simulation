package model.people;

import model.disasters.Disaster;
import model.events.SOSListener;
import model.events.WorldListener;
import model.units.UnitState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class Citizen implements Simulatable,Rescuable{
	
	private CitizenState state;
	private Disaster disaster;
	private	Address location;
	private String nationalID;
	private String name;
	private int age;
	private	int hp;
	private int bloodLoss;
	private int toxicity;
	private SOSListener emergencyService;
	private WorldListener worldListener;
		
		
	public Citizen(Address location, String nationalID, String name, int age,WorldListener wl){
		state = CitizenState.SAFE;
		worldListener=wl;
		this.location = location;
		this.nationalID= nationalID;
		this.name=name;
		this.age=age;
		hp=100;
		bloodLoss=0;
		toxicity=0;
	}

	public CitizenState getState() {
		return state;
	}

	public void setState(CitizenState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if(hp<=100&&hp>=0)
			this.hp=hp;
		else if(hp>100)
			this.hp=100;
		else
			this.hp=0;
		if (this.hp==0)
			this.state=CitizenState.DECEASED;
	}

	public int getBloodLoss() {
		return bloodLoss;
	}

	public void setBloodLoss(int bloodLoss) {
		if(bloodLoss<=100&&bloodLoss>=0) {
			this.bloodLoss=bloodLoss;
		}
		else {
			if(bloodLoss>100) {
				this.bloodLoss=100;
			}
		
		else
			this.bloodLoss=0;
		}
		if(this.bloodLoss==100) {
			this.setHp(0);
		}
	}

	public int getToxicity() {
		return toxicity;
	}

	public void setToxicity(int toxicity) {
		if(toxicity<=100&&toxicity>=0)
			this.toxicity=toxicity;
		else if(toxicity>100)
			this.toxicity=100;
		else
			this.toxicity=0;
		if(toxicity==100)
			this.hp=0;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public String getNationalID() {
		return nationalID;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public void struckBy(Disaster d) {
		this.disaster=d;this.state=CitizenState.IN_TROUBLE;
		emergencyService.receiveSOSCall(this);
	}

	public void cycleStep() {
		if((toxicity<30&&toxicity>0))
			hp-=5;
		else if(toxicity<70&&toxicity>=30)
			hp-=10;
		else if(toxicity>=70)
			hp-=15;
		
		if((bloodLoss<30&&bloodLoss>0))
			hp-=5;
		else if(bloodLoss<70&&bloodLoss>=30)
			hp-=10;
		else if(bloodLoss>=70)
			hp-=15;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setEmergencyService(SOSListener emergencyService) {
		this.emergencyService = emergencyService;
	}
	
	
}
