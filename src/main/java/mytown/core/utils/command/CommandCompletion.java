package mytown.core.utils.command;

import mytown.core.MyTownCore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AfterWind on 9/10/2014.
 * Command completion class
 */
public class CommandCompletion {
    public static Map<String, List<String>> completionMap = new HashMap<String, List<String>>();

    public static List<String> getCommandCompletion(String key) {
        MyTownCore.Instance.log.info("Completion only was called!");
        return completionMap.get(key);
    }
}
