package core.training;

import core.db.DBManager;
import core.db.JDBCAdapter;
import core.model.HOVerwaltung;
import core.model.enums.DBDataSource;
import core.util.HOLogger;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * Class that create a list of TrainingPerWeek objects between 2 dates
 *
 */
public class TrainingWeekManager {

	private static final Instant m_NextTrainingDate = HOVerwaltung.instance().getModel().getXtraDaten().getTrainingDate().toInstant();

    private List<TrainingPerWeek> m_Trainings;
    private Instant m_StartDate;
	private Boolean m_IncludeMatches;
	private Boolean m_IncludeUpcomingMatches;

	/**
	 * Construct a list of TrainingPerWeek since provided initial training Date
	 * @param startDate initial training Date
	 * @param includeMatches whether or not the TrainingPerWeek objects will contain match information
	 * @param includeUpcomingMatches whether or not the TrainingPerWeek objects will contain upcoming match information
	 */
	public TrainingWeekManager(Instant startDate, boolean includeMatches, boolean includeUpcomingMatches) {
		m_StartDate = startDate;
		m_IncludeMatches = includeMatches;
		m_IncludeUpcomingMatches = includeUpcomingMatches;
		m_Trainings = computeTrainingList();
	}

	/**
	 * Construct a list of TrainingPerWeek of requested size
	 * @param nbEntries requested vector size
	 * @param includeMatches whether or not the TrainingPerWeek objects will contain match information
	 * @param includeUpcomingMatches whether or not the TrainingPerWeek objects will contain upcoming match information
	 */
	public TrainingWeekManager(int nbEntries, boolean includeMatches, boolean includeUpcomingMatches) {
		m_StartDate = findStartDate(nbEntries);
		m_IncludeMatches = includeMatches;
		m_IncludeUpcomingMatches = includeUpcomingMatches;
		m_Trainings = computeTrainingList();
	}

	/**
	 * Determine first training date from requested number of entries
	 * @param nbEntries requested size
	 * @return
	 */
	private Instant findStartDate(int nbEntries){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.from(ZoneOffset.UTC));
			String startDate = formatter.format(Instant.now().minus(1, ChronoUnit.YEARS));
			String sql = String.format("""
					SELECT TRAININGDATE	FROM XTRADATA WHERE XTRADATA.TRAININGDATE >= '%s' 
					ORDER BY TRAININGDATE DESC LIMIT %s""",startDate, nbEntries);

			Instant trainingDate = null;

			try {

				final JDBCAdapter ijdbca = DBManager.instance().getAdapter();
				final ResultSet rs = ijdbca.executeQuery(sql);
				rs.beforeFirst();

				while (rs.next()) {
					trainingDate = rs.getTimestamp("TRAININGDATE").toInstant();
				}
			}
			catch (Exception e) {
				HOLogger.instance().error(this.getClass(), "Error while performing findDefaultStartDate():  " + e);
			}

			return trainingDate;
	}


	// TODO: the logic of this function does not yet respect the specs
	/**
	 * Create the list of trainings in 3 steps:
	 *     1. information from Training table is loaded
	 *     2. missing weeks are constructed from other database tables
	 *     3. missing weeks are created from previous entry
	 */
	private List<TrainingPerWeek> computeTrainingList(){

		List<TrainingPerWeek>  trainings = new ArrayList<>();
		HashMap<Instant, TrainingPerWeek>  trainingsInDB = createTPWfromDBentries();
		int trainingsSize;

		if (m_StartDate.isAfter(m_NextTrainingDate)){
			HOLogger.instance().error(this.getClass(), "It was assumed that start date will always be before next training date in database");
			return trainings;
		}

		long nbWeeks = ChronoUnit.DAYS.between(m_StartDate, m_NextTrainingDate) / 7;

		Instant currDate = m_NextTrainingDate.minus(nbWeeks * 7, ChronoUnit.DAYS);

		while((currDate.isBefore(m_NextTrainingDate) || currDate.equals(m_NextTrainingDate))){
			if (trainingsInDB.containsKey(currDate)){
				trainings.add(trainingsInDB.get(currDate));
			}
			else{
				trainingsSize = trainings.size();
				if(trainingsSize != 0)
				{
					var previousTraining = trainings.get(trainingsSize - 1);
					previousTraining.setSource(DBDataSource.GUESS);
					trainings.add(previousTraining);
				}
				else{
					var tpw = new TrainingPerWeek(currDate, -1, 0, 0, 0, 0,
							m_IncludeMatches, m_IncludeUpcomingMatches, DBDataSource.GUESS);
					trainings.add(tpw);
				}
			}

			currDate = currDate.plus(7, ChronoUnit.DAYS);
		}

		return trainings;

	}


	/**
	 * Fetch trainings information from database (excl. Training table)
	 * This excludes manual entries set per user
	 */
	private HashMap<Instant, TrainingPerWeek> createTPWfromDBentries() {

		HashMap<Instant, TrainingPerWeek> output = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.from(ZoneOffset.UTC));
		String startDate = formatter.format(m_StartDate);
		String sql = String.format("""
					SELECT TRAININGDATE, TRAININGSART, TRAININGSINTENSITAET, STAMINATRAININGPART, COTRAINER
					FROM XTRADATA
					INNER JOIN TEAM on XTRADATA.HRF_ID = TEAM.HRF_ID
					INNER JOIN VEREIN on XTRADATA.HRF_ID = VEREIN.HRF_ID
					INNER JOIN (
					     SELECT TRAININGDATE, max(HRF_ID) MAX_HR_ID FROM XTRADATA GROUP BY TRAININGDATE
					) IJ1 ON XTRADATA.HRF_ID = IJ1.MAX_HR_ID
					WHERE XTRADATA.TRAININGDATE >= '%s'""",startDate);


		int trainType, trainIntensity, trainStaminaPart, coachLevel, trainingAssistantLevel;
		Instant trainingDate;

		try {

			final JDBCAdapter ijdbca = DBManager.instance().getAdapter();
			final ResultSet rs = ijdbca.executeQuery(sql);
			rs.beforeFirst();

			while (rs.next()) {
				trainType = rs.getInt("TRAININGSART");
				trainIntensity = rs.getInt("TRAININGSINTENSITAET");
				trainStaminaPart = rs.getInt("STAMINATRAININGPART");
				trainingDate = rs.getTimestamp("TRAININGDATE").toInstant();
				coachLevel = -1;  //TODO: fix this when #905 is implemented
				trainingAssistantLevel = rs.getInt("COTRAINER");
				TrainingPerWeek tpw = new TrainingPerWeek(trainingDate, trainType, trainIntensity, trainStaminaPart, trainingAssistantLevel,
						coachLevel, m_IncludeMatches, m_IncludeUpcomingMatches, DBDataSource.HRF);
				output.put(trainingDate, tpw);
			}
		}
		catch (Exception e) {
			HOLogger.instance().error(this.getClass(), "Error while performing fetchTrainingListFromDB():  " + e);
		}

		return output;
	}


    public List<TrainingPerWeek> getTrainingList() {
    	return m_Trainings;
    }



    

    




}
