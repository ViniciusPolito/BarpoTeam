package rural_team.formations;

import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;

public class Formations {

	public Formations() {
	}

	// Posição Inicial
	public Vector2D initalHexaFormation(PlayerCommander commander) {

		double homeX;
		double homeY;
		int uniform = commander.getMyUniformNumber();

		if (uniform == 1) {
			// GOLEIRO
			homeX = -50;
			homeY = 0;
		} else if (uniform == 2) {
			// RIGHT DEFENSER
			homeX = -30;
			homeY = 8;
		} else if (uniform == 3) {
			// LEFT DEFENSER
			homeX = -30;
			homeY = -8;
		} else if (uniform == 4) {
			// RIGHT BACK
			homeX = -18;
			homeY = 16;
		} else if (uniform == 5) {
			// LEFT BACK
			homeX = -18;
			homeY = -16;
		} else if (uniform == 6) {
			// RIGHT STRIKER
			homeX = -6;
			homeY = 8;
		} else {
			// LEFT STRIKER
			homeX = -6;
			homeY = -8;
		}

		// se o time estiver no lado direito, faz o ajuste
		if (commander.getMyFieldSide() == EFieldSide.RIGHT) {
			homeX = -homeX;
			homeY = -homeY;
		}

		return new Vector2D(homeX, homeY);
	}

	// Posição Neutra
	public Vector2D neutralFormation(PlayerCommander commander) {

		double homeX;
		double homeY;
		int uniform = commander.getMyUniformNumber();

		if (uniform == 1) {
			// GOLEIRO
			homeX = -50;
			homeY = 0;
		} else if (uniform == 2) {
			// RIGHT DEFENSER
			homeX = -30;
			homeY = 8;
		} else if (uniform == 3) {
			// LEFT DEFENSER
			homeX = -30;
			homeY = -8;
		} else if (uniform == 4) {
			// RIGHT BACK
			homeX = -6;
			homeY = 8;
		} else if (uniform == 5) {
			// LEFT BACK
			homeX = -6;
			homeY = -8;
		} else if (uniform == 6) {
			// RIGHT STRIKER
			homeX = 18;
			homeY = 16;
		} else {
			// LEFT STRIKER
			homeX = 18;
			homeY = -16;
		}

		// se o time estiver no lado direito, faz o ajuste
		if (commander.getMyFieldSide() == EFieldSide.RIGHT) {
			homeX = -homeX;
			homeY = -homeY;
		}

		return new Vector2D(homeX, homeY);
	}

	// Posição Ataque
	public Vector2D attackingFormation(PlayerCommander commander) {

		double homeX;
		double homeY;
		int uniform = commander.getMyUniformNumber();

		if (uniform == 1) {
			// GOLEIRO
			homeX = -50;
			homeY = 0;
		} else if (uniform == 2) {
			// RIGHT DEFENSER
			homeX = -26;
			homeY = 0;
		} else if (uniform == 3) {
			// LEFT DEFENSER
			homeX = -6;
			homeY = -8;
		} else if (uniform == 4) {
			// RIGHT BACK
			homeX = -6;
			homeY = 8;
		} else if (uniform == 5) {
			// LEFT BACK
			homeX = 30;
			homeY = -10;
		} else if (uniform == 6) {
			// RIGHT STRIKER
			homeX = 30;
			homeY = 10;
		} else {
			// LEFT STRIKER
			homeX = 10;
			homeY = 0;
		}

		// se o time estiver no lado direito, faz o ajuste
		if (commander.getMyFieldSide() == EFieldSide.RIGHT) {
			homeX = -homeX;
			homeY = -homeY;
		}

		return new Vector2D(homeX, homeY);
	}

	// Posição Defesa
	public Vector2D defendingFormation(PlayerCommander commander) {

		double homeX;
		double homeY;
		int uniform = commander.getMyUniformNumber();

		if (uniform == 1) {
			// GOLEIRO
			homeX = -50;
			homeY = 0;
		} else if (uniform == 2) {
			// RIGHT DEFENSER
			homeX = -36;
			homeY = 0;
		} else if (uniform == 3) {
			// LEFT DEFENSER
			homeX = -26;
			homeY = -8;
		} else if (uniform == 4) {
			// RIGHT BACK
			homeX = -26;
			homeY = 8;
		} else if (uniform == 5) {
			// LEFT BACK
			homeX = -10;
			homeY = -10;
		} else if (uniform == 6) {
			// RIGHT STRIKER
			homeX = -10;
			homeY = 10;
		} else {
			// LEFT STRIKER
			homeX = 8;
			homeY = 0;
		}

		// se o time estiver no lado direito, faz o ajuste
		if (commander.getMyFieldSide() == EFieldSide.RIGHT) {
			homeX = -homeX;
			homeY = -homeY;
		}

		return new Vector2D(homeX, homeY);
	}
}
