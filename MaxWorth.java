import java.util.*;

public class MaxWorth {

    static class StringInfo {
        String value;
        int cost;
        int worth;

        public StringInfo(String value, int cost) {
            this.value = value;
            this.cost = cost;
            this.worth = calculateWorth(value);
        }

        private int calculateWorth(String s) {
            int worth = 0;
            for (char c : s.toCharArray()) {
                worth += (c - 'a' + 1);
            }
            return worth;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        sc.nextLine();

        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = sc.next();
        }

        int[] costs = new int[n];
        for (int i = 0; i < n; i++) {
            costs[i] = sc.nextInt();
        }

        Map<String, Set<String>> contradictions = new HashMap<>();
        for (int i = 0; i < m; i++) {
            String a = sc.next();
            String b = sc.next();
            contradictions.putIfAbsent(a, new HashSet<>());
            contradictions.putIfAbsent(b, new HashSet<>());
            contradictions.get(a).add(b);
            contradictions.get(b).add(a);
        }

        int budget = sc.nextInt();

        List<StringInfo> stringInfos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            stringInfos.add(new StringInfo(strings[i], costs[i]));
        }

        int maxWorth = getMaxWorth(stringInfos, contradictions, budget);

        System.out.print(maxWorth);
    }

    private static int getMaxWorth(List<StringInfo> stringInfos, Map<String, Set<String>> contradictions, int budget) {
        int n = stringInfos.size();
        int maxStates = 1 << n; 
        int maxWorth = 0;

        for (int state = 0; state < maxStates; state++) {
            int totalCost = 0;
            int totalWorth = 0;
            boolean valid = true;
            Set<String> selectedStrings = new HashSet<>();

            for (int i = 0; i < n; i++) {
                if ((state & (1 << i)) != 0) { 
                    StringInfo info = stringInfos.get(i);
                    totalCost += info.cost;
                    totalWorth += info.worth;
                    selectedStrings.add(info.value);

                    
                    if (contradictions.containsKey(info.value)) {
                        for (String contradicted : contradictions.get(info.value)) {
                            if (selectedStrings.contains(contradicted)) {
                                valid = false;
                                break;
                            }
                        }
                    }
                }
                if (!valid) break;
            }

            if (valid && totalCost <= budget) {
                maxWorth = Math.max(maxWorth, totalWorth);
            }
        }

        return maxWorth;
    }
}