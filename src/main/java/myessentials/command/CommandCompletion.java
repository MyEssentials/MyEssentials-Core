package myessentials.command;

import java.util.*;

public class CommandCompletion {

    /**
     * Map with the completion as key and a list of available completions as value
     */
    private static final Map<String, List<String>> completionMap = new HashMap<String, List<String>>();

    public static List<String> getCompletionList(String key) {
        List<String> completionList = completionMap.get(key);

        if(completionList == null) {
            completionList = new ArrayList<String>();
            completionMap.put(key, completionList);
        }

        return completionList;
    }

    public static void addCompletion(String key, String completion) {
        getCompletionList(key).add(completion);
    }

    public static void addCompletions(String key, Collection<String> completions) {
        getCompletionList(key).addAll(completions);
    }

    public static void removeCompletion(String key, String completion) {
        List<String> completionList = completionMap.get(key);

        if(completionList == null)
            return;

        for(Iterator<String> it = completionList.iterator(); it.hasNext(); ) {
            if(it.next().equals(completion)) {
                it.remove();
                return;
            }
        }
    }

}
