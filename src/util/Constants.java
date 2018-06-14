package util;

public class Constants {
	public static final int GRID_SIZE = 50;
	public static final int NUM_AGENTS = 10;
	public static final int MAX_PAINTING = 8;
	public static final int FRAME_SIZE = 600;
	public static final int MAX_STEPS = 10;
	public static final int MAX_SCOUT_AGENTS = 2;
	public static final int SPAWN_ZONE_INIT = 10; // La zone d'apparation s'étend sur SPAWN_ZONE_INIT lignes
	public static final int MAX_TIN_OF_PAINT = 15; // Nombre de pot de peinture à l'initialisation
	public static final int MAX_TUBE_OF_PAINT = 1;
	public static final int PERCEPTION_FOR_COLORING_AGENT = 1;
	public static final int PERCEPTION_FOR_SCOUT_AGENT = 5 * PERCEPTION_FOR_COLORING_AGENT;
	public static final int COLORATION_POWER_FOR_COLORING_AGENT = 1;
	public static final int THRESHOLD_FOR_PAINTING_A_ZONE = (9 * PERCEPTION_FOR_COLORING_AGENT)/2; // Nombre de cases � colorier sur une zone pour un agent
}
