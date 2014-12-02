package engine;

public enum HappinessStatus {
	HAPPY,
	HUNGRY,
	AMMONIA,
	NITRITE,
	NITRATE,
	PHLOW,
	PHHIGH,
	TEMPLOW,
	TEMPHIGH,
	HARDLOW,
	HARDHIGH;

	public String stringify(){
		String readable = "Unhappy: ";
		switch(this){
		case HAPPY:
			readable = "Happy";
			break;
		case HUNGRY:
			readable += "Hungry!";
			break;
		case AMMONIA:
			readable += "Ammonia in the tank is too high. Check below to see what ammonia ranges this species can tolerate.";
			break;
		case NITRITE:
			readable += "Nitrite in the tank is too high. Check below to see what nitrite ranges this species can tolerate.";
			break;
		case NITRATE:
			readable += "Nitrate in the tank is too high. Check below to see what nitrate ranges this species can tolerate.";
			break;
		case PHLOW:
			readable += "pH in the tank is too low. Check below to see what pH ranges this species can tolerate.";
			break;
		case PHHIGH:
			readable += "pH in the tank is too high. Check below to see what pH ranges this species can tolerate.";
			break;
		case TEMPLOW:
			readable += "Temperature in the tank is too low. Check below to see what temperature ranges this species can tolerate.";
			break;
		case TEMPHIGH:
			readable += "Temperature in the tank is too high. Check below to see what temperature ranges this species can tolerate.";
			break;
		case HARDLOW:
			readable += "Hardness in the tank is too low. Check below to see what hardness ranges this species can tolerate.";
			break;
		case HARDHIGH:
			readable += "Hardness in the tank is too high. Check below to see what hardness ranges this species can tolerate.";
			break;
		}
		return readable;
	}
}
