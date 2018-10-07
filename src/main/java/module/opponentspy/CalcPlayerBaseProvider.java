package module.opponentspy;

import core.constants.player.PlayerSpeciality;
import core.model.player.ISpielerPosition;
//import module.opponentspy.OpponentTeam.PlayedPosition;

import java.util.HashMap;
import java.util.Iterator;

public class CalcPlayerBaseProvider {

	
	
	public CalcVariables getCalcPlayerBase(int age, int wage, int tsi,
			double form, double stamina, int spec, int role) {

		CalcVariables calcPlayer = new CalcVariables();

		calcPlayer.age = age;
		calcPlayer.wage = wage;
		calcPlayer.tsi = tsi;
		calcPlayer.form = form;
		calcPlayer.stamina = stamina;
		calcPlayer.specialty = spec;
		calcPlayer.role = role;
		
		setBaseSkills(calcPlayer);
		
		return calcPlayer;
	}

		
	public void setBaseSkills(CalcVariables calcPlayer) {
		
		switch (calcPlayer.role) {
			case ISpielerPosition.KEEPER:
				setAsGoalkeeper(calcPlayer, calcPlayer.specialty);
				break;
			// case ISpielerPosition.COUNTER_DEFENDER :
			// setAsCounterDefender (calcPlayer));
			// break; // It might be better to make a boolean in
			// setAsCentralDefender and setAsWingBack because COUNTER_DEFENDER is
			// not a position.
			case ISpielerPosition.CENTRAL_DEFENDER:
				setAsCentralDefender(calcPlayer);
				break;
			case ISpielerPosition.CENTRAL_DEFENDER_OFF:
				setAsCentralDefender(calcPlayer);
				break;
			case ISpielerPosition.CENTRAL_DEFENDER_TOWING:
				setAsWingBack(calcPlayer);
				break;
			case ISpielerPosition.BACK:
				setAsWingBack(calcPlayer);
				break;
			case ISpielerPosition.BACK_DEF:
				setAsWingBack(calcPlayer);
				break;
			case ISpielerPosition.BACK_OFF:
				setAsWingBack(calcPlayer);
				break;
			case ISpielerPosition.BACK_TOMID:
				setAsCentralDefender(calcPlayer);
				break;
			case ISpielerPosition.MIDFIELDER:
				setAsCentralMidfielder(calcPlayer);
				break;
			case ISpielerPosition.MIDFIELDER_DEF:
				setAsCentralMidfielder(calcPlayer);
				break;
			case ISpielerPosition.MIDFIELDER_OFF:
				setAsCentralMidfielder(calcPlayer);
				break;
			case ISpielerPosition.MIDFIELDER_TOWING:
				setAsWinger(calcPlayer);
				break;
			case ISpielerPosition.WINGER:
				setAsWinger(calcPlayer);
				break;
			case ISpielerPosition.WINGER_DEF:
				setAsWinger(calcPlayer);
				break;
			case ISpielerPosition.WINGER_OFF:
				setAsWinger(calcPlayer);
				break;
			case ISpielerPosition.WINGER_TOMID:
				setAsCentralMidfielder(calcPlayer);
				break;
			case ISpielerPosition.FORWARD:
				setAsForward(calcPlayer);
				break;
			case ISpielerPosition.FORWARD_DEF:
				setAsForwardDefensive(calcPlayer);
				break;
			case ISpielerPosition.FORWARD_TOWING:
				setAsForwardToWing(calcPlayer);
				break;
	
			default:
				setAsGoalkeeper(calcPlayer, calcPlayer.specialty);
				break;
		}
	}

	private void setAsGoalkeeper(CalcVariables calcPlayer, int spec) {

		if (!(spec == PlayerSpeciality.UNPREDICTABLE)) {
			if (calcPlayer.age < 22) {

				calcPlayer.setGoalkeeping(7, 1);
				calcPlayer.setDefending(6);
				calcPlayer.setSetPieces(5);
			} else if (calcPlayer.age >= 22) {
				calcPlayer.setGoalkeeping(15, 1);
				calcPlayer.setDefending(15);
				calcPlayer.setSetPieces(5);
			} else if (calcPlayer.age >= 25) {
				calcPlayer.setGoalkeeping(17, 1);
				calcPlayer.setDefending(15);
				calcPlayer.setSetPieces(20);
			} else if (calcPlayer.age >= 28) {
				calcPlayer.setGoalkeeping(20, 1);
				calcPlayer.setDefending(16);
				calcPlayer.setSetPieces(20);
			}
		} else {

			if (calcPlayer.age < 22) {
				calcPlayer.setGoalkeeping(7, 1);
				calcPlayer.setDefending(6);
				calcPlayer.setPassing(5);
				calcPlayer.setSetPieces(4);

			} else if (calcPlayer.age >= 22) {
				calcPlayer.setGoalkeeping(15, 1);
				calcPlayer.setDefending(15);
				calcPlayer.setPassing(7);
				calcPlayer.setSetPieces(5);

			} else if (calcPlayer.age >= 25) {
				calcPlayer.setGoalkeeping(17, 1);
				calcPlayer.setDefending(15);
				calcPlayer.setPassing(7);
				calcPlayer.setSetPieces(19);

			} else if (calcPlayer.age >= 28) {
				calcPlayer.setGoalkeeping(20, 1);
				calcPlayer.setDefending(15);
				calcPlayer.setPassing(7);
				calcPlayer.setSetPieces(20);
			}
		}
	}

	private void setAsCounterDefender(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(4);
			calcPlayer.setDefending(7, 1);
			calcPlayer.setWing(5);
			calcPlayer.setPassing(7);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setDefending(15, 1);
			calcPlayer.setWing(9);
			calcPlayer.setPassing(15);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setDefending(17, 1);
			calcPlayer.setWing(9);
			calcPlayer.setPassing(15);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(8);
			calcPlayer.setDefending(18, 1);
			calcPlayer.setWing(9);
			calcPlayer.setPassing(16);
			calcPlayer.setSetPieces(12);
		}

	}

	private void setAsCentralDefender(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setDefending(7, 1);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(15);
			calcPlayer.setDefending(15, 1);
			calcPlayer.setPassing(7);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(17);
			calcPlayer.setDefending(17, 1);
			calcPlayer.setPassing(7);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(17);
			calcPlayer.setDefending(18, 1);
			calcPlayer.setPassing(7);
			calcPlayer.setSetPieces(9);
		}
	}

	private void setAsWingBack(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(5);
			calcPlayer.setDefending(7, 1);
			calcPlayer.setWing(6);
			calcPlayer.setPassing(5);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(14);
			calcPlayer.setDefending(15, 1);
			calcPlayer.setWing(15);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(14);
			calcPlayer.setDefending(17, 1);
			calcPlayer.setWing(16);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(14);
			calcPlayer.setDefending(18, 1);
			calcPlayer.setWing(17);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(8);
		}
	}

	private void setAsCentralMidfielder(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(7, 1);
			calcPlayer.setDefending(5);
			calcPlayer.setScoring(4);
			calcPlayer.setPassing(7);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(15, 1);
			calcPlayer.setDefending(11);
			calcPlayer.setScoring(6);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(18, 1);
			calcPlayer.setDefending(11);
			calcPlayer.setScoring(6);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(19, 1);
			calcPlayer.setDefending(11);
			calcPlayer.setScoring(6);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(7);
		}

	}

	private void setAsWinger(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setDefending(5);
			calcPlayer.setWing(7, 1);
			calcPlayer.setPassing(5);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(13);
			calcPlayer.setDefending(11);
			calcPlayer.setWing(16, 1);
			calcPlayer.setPassing(9);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(16);
			calcPlayer.setDefending(11);
			calcPlayer.setWing(17, 1);
			calcPlayer.setPassing(9);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(18);
			calcPlayer.setDefending(11);
			calcPlayer.setWing(17, 1);
			calcPlayer.setPassing(9);
			calcPlayer.setSetPieces(8);
		}
	}

	private void setAsForward(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setWing(5);
			calcPlayer.setScoring(6, 1);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(12);
			calcPlayer.setWing(10);
			calcPlayer.setScoring(13, 1);
			calcPlayer.setPassing(11);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(14);
			calcPlayer.setWing(12);
			calcPlayer.setScoring(15, 1);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(14);
			calcPlayer.setWing(12);
			calcPlayer.setScoring(17, 1);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(9);
		}
	}

	private void setAsForwardDefensive(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(6, 1);
			calcPlayer.setWing(5);
			calcPlayer.setScoring(6);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(13, 1);
			calcPlayer.setWing(5);
			calcPlayer.setScoring(12);
			calcPlayer.setPassing(13);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(16, 1);
			calcPlayer.setWing(7);
			calcPlayer.setScoring(14);
			calcPlayer.setPassing(16);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(16, 1);
			calcPlayer.setWing(7);
			calcPlayer.setScoring(14);
			calcPlayer.setPassing(17);
			calcPlayer.setSetPieces(9);
		}
	}

	private void setAsForwardToWing(CalcVariables calcPlayer) {

		if (calcPlayer.age < 22) {
			calcPlayer.setPlaymaking(6);
			calcPlayer.setWing(5);
			calcPlayer.setScoring(6, 1);
			calcPlayer.setPassing(6);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 22) {
			calcPlayer.setPlaymaking(9);
			calcPlayer.setWing(13);
			calcPlayer.setScoring(13, 1);
			calcPlayer.setPassing(10);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 27) {
			calcPlayer.setPlaymaking(11);
			calcPlayer.setWing(16);
			calcPlayer.setScoring(15, 1);
			calcPlayer.setPassing(12);
			calcPlayer.setSetPieces(2);
		} else if (calcPlayer.age >= 29) {
			calcPlayer.setPlaymaking(11);
			calcPlayer.setWing(16);
			calcPlayer.setScoring(18, 1);
			calcPlayer.setPassing(12);
			calcPlayer.setSetPieces(9);
		}
	}
}