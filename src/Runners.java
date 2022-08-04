import java.util.Date;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.TreeSet;

public class Runners {
    private final Runner[] runners;

    public Runners(int nbRunners) {
        runners = new Runner[nbRunners];
    }

    public void addRunner(Runner runner) {
        if (isFull())
            throw new IllegalArgumentException("Max runner amount achieved !");
        for (int i = 0; i < runners.length; ++i)
            if (runners[i] == null) {
                runners[i] = runner;
                break;
            }
    }

    public boolean isEmpty() {
        return runners[0] == null;
    }

    public boolean hasAtLeastTwoRunners() {
        return runners[0] != null && runners[1] != null;
    }

    public boolean isFull() {
        return runners[runners.length - 1] != null;
    }

    public Runner getRunnerByDossard(int dossardNum) {
        for (Runner runner : runners) {
            if (runner != null)
                if (runner.getNumDossard() == dossardNum)
                    return runner;
        }
        return null;
    }

    public boolean hasDossardNum(int dossardNum) {
        for (Runner runner : runners) {
            if (runner != null)
                if (runner.getNumDossard() == dossardNum)
                    return true;
        }
        return false;
    }

    public Date getTimeBefore(Runner runner) {
        Set<Runner> runnersSorted = getRunnersFinishedSorted();
        if (!runnersSorted.contains(runner))
            throw new IllegalArgumentException("This runner hasn't finished yet.");
        Date date = new Date();
        for (int i = 0; i < runnersSorted.size(); ++i)
            if (runnersSorted.toArray()[i].equals(runner)) {
                if (i != 0) {
                    for(int j = i ; j >= 0 ; --j){
                        Runner beforeRunner = (Runner) runnersSorted.toArray()[j];
                        if(beforeRunner.getTemps().getHours() != 0 || beforeRunner.getTemps().getMinutes() != 0 || beforeRunner.getTemps().getSeconds() != 0)
                            return beforeRunner.getTemps();
                    }
                }
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                return date;
            }
        return date;
    }

    public Date getTimeAfter(Runner runner) {
        Set<Runner> runnersSorted = getRunnersFinishedSorted();
        if (!runnersSorted.contains(runner))
            throw new IllegalArgumentException("This runner hasn't finished yet.");
        Date date = new Date();
        for (int i = 0; i < runnersSorted.size(); ++i)
            if (runnersSorted.toArray()[i].equals(runner)) {
                if (i != runnersSorted.size() - 1) {
                    for(int j = i ; j < runnersSorted.size() ; ++j){
                        Runner afterRunner = (Runner) runnersSorted.toArray()[j];
                        if(afterRunner.getTemps().getHours() != 0 || afterRunner.getTemps().getMinutes() != 0 || afterRunner.getTemps().getSeconds() != 0)
                            return afterRunner.getTemps();
                    }
                }
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                return date;
            }
        return date;
    }

    private Set<Runner> getRunnersFinishedSorted() {
        Set<Runner> treeRunners = new TreeSet<>();
        for (Runner r : runners)
            if (r != null)
                if (r.getStatus() == RunnerStatus.FINISHED)
                    treeRunners.add(r);
        if (treeRunners.isEmpty())
            throw new EmptyStackException();
        return treeRunners;
    }

    public String classementsToString() {
        StringBuilder result = new StringBuilder("");
        try {
            Set<Runner> treeRunners = getRunnersFinishedSorted();
            for (Runner runner : treeRunners)
                if (runner != null)
                    result.append(runner.getClassement())
                            .append(" : ").append(runner.getPrenom()).append(" ")
                            .append(runner.getNom()).append(" (n°").append(runner.getNumDossard()).append(")\n");

            return result.toString();
        } catch (EmptyStackException e) {
            result.append("No runners have finished the race yet !\n");
            return result.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        for (Runner runner : runners)
            if (runner != null)
                result.append(runner.toString()).append("\n");
        return result.toString();
    }
}