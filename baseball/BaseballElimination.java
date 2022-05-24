/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int n;
    private final Map<String, Integer> map;
    private final String[] names;
    private final int[] wins, loss, left;
    private final int[][] games;
    private final boolean[] solved;
    private final List<List<String>> certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        map = new HashMap<>();
        In in = new In(filename);
        n = in.readInt();
        names = new String[n];
        wins = new int[n];
        loss = new int[n];
        left = new int[n];
        games = new int[n][n];
        solved = new boolean[n];
        certificates = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            certificates.add(new ArrayList<>());
            String s = in.readString();
            map.put(s, i);
            names[i] = s;
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            left[i] = in.readInt();
            for (int j = 0; j < n; j++)
                games[i][j] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return map.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validateString(team);
        return wins[map.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validateString(team);
        return loss[map.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateString(team);
        return left[map.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateString(team1);
        validateString(team2);
        return games[map.get(team1)][map.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return certificateOfElimination(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateString(team);
        int x = map.get(team), g = 1, t = (n - 1) * (n - 2) / 2 + n, v = t - n + 1;
        if (solved[x]) return certificates.get(x).isEmpty() ? null : certificates.get(x);

        // trivial elimination
        List<String> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (wins[x] + left[x] < wins[i]) {
                res.add(names[i]);
                solved[x] = true;
                certificates.set(x, res);
                return res;
            }
        }

        // nontrivial elimination
        FlowNetwork net = new FlowNetwork(1 + t);
        for (int i = 0; i < n; i++) {
            if (i == x) continue;
            int w1 = i < x ? i + v : i + v - 1;
            net.addEdge(new FlowEdge(w1, t, wins[x] + left[x] - wins[i]));
            for (int j = i + 1; j < n; j++) {
                if (j == x) continue;
                int w2 = j < x ? j + v : j + v - 1;
                net.addEdge(new FlowEdge(0, g, games[i][j]));
                net.addEdge(new FlowEdge(g, w1, Double.POSITIVE_INFINITY));
                net.addEdge(new FlowEdge(g++, w2, Double.POSITIVE_INFINITY));
            }
        }

        FordFulkerson ff = new FordFulkerson(net, 0, t);
        for (int i = 0; i < n; i++) {
            if (i == x) continue;
            int w = i < x ? i + v : i + v - 1;
            if (ff.inCut(w)) res.add(names[i]);
        }

        solved[x] = true;
        certificates.set(x, res);
        return res.isEmpty() ? null : res;
    }

    private void validateString(String s) {
        if (s == null) throw new IllegalArgumentException("null argument");
        if (!map.containsKey(s)) throw new IllegalArgumentException("invalid key");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                Iterable<String> res = division.certificateOfElimination(team);
                int sum = 0, sz = 0, best = division.wins(team) + division.remaining(team);
                for (String t : res) {
                    StdOut.print(t + " ");
                    sum += division.wins(t) + division.remaining(t);
                    sz++;
                }
                StdOut.println("}; a(R) = " + (double) sum / sz + " > " + best);
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
