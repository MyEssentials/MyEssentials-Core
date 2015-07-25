package myessentials.command;

import cpw.mods.fml.common.Loader;
import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.command.annotation.CommandNode;
import myessentials.command.registrar.BukkitCommandRegistrar;
import myessentials.command.registrar.ForgeEssentialsCommandRegistrar;
import myessentials.command.registrar.ICommandRegistrar;
import myessentials.command.registrar.VanillaCommandRegistrar;
import myessentials.exception.CommandException;
import myessentials.utils.ClassUtils;
import net.minecraft.command.ICommand;

import java.lang.reflect.Method;
import java.util.*;

public class CommandManagerNew {

    /**
     * Registrar used to register any commands. Offers compatibility for Bukkit and ForgeEssentials
     */
    public static final ICommandRegistrar registrar = makeRegistrar();

    /**
     * Map with the completion as key and a list of available completions as value
     */
    public static final Map<String, List<String>> completionMap = new HashMap<String, List<String>>();

    private static final List<CommandTree> commandTrees = new ArrayList<CommandTree>();

    private static final String ROOT_PERM_NODE = "ROOT";

    private CommandManagerNew() {
    }

    /**
     * It is enforced that the class has to contain ONE root command .
     */
    public static void registerCommands(Class clazz, String rootPerm, Localization localization) {
        CommandTreeNode root = null;
        CommandTree commandTree = rootPerm == null ? null : getTree(rootPerm);

        Map<CommandNode, Method> nodes = new HashMap<CommandNode, Method>();

        for (final Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(CommandNode.class)) {
                CommandNode commandNode = method.getAnnotation(CommandNode.class);
                if(commandNode.parentName().equals(ROOT_PERM_NODE)) {
                    if(commandTree == null) {
                        root = new CommandTreeNode(commandNode, method);
                    } else {
                        throw new CommandException("Class " + clazz.getName() + " has more than one root command.");
                    }
                } else {
                    nodes.put(commandNode, method);
                }
            }
        }

        if(commandTree == null) {
            if (root == null) {
                throw new CommandException("Class " + clazz.getName() + " has no root command.");
            } else {
                commandTree = new CommandTree(root, localization);
                commandTrees.add(commandTree);
            }
        }

        registerCommand(new CommandModel(commandTree.getRoot().getAnnotation(), commandTree.getRoot().getMethod()), commandTree.getRoot().getAnnotation().permission());
        constructTree(commandTree.getRoot(), nodes);

        if(!nodes.isEmpty()) {
            for(Map.Entry<CommandNode, Method> entry : nodes.entrySet()) {
                MyEssentialsCore.instance.LOG.error("Node " + entry.getKey().permission() + " has an invalid parent " + entry.getKey().parentName());
            }
        }
    }

    public static CommandTree getTree(String basePerm) {
        for(CommandTree tree : commandTrees) {
            if(tree.getRoot().getAnnotation().permission().equals(basePerm))
                return tree;
        }
        return null;
    }

    public static void registerCommand(ICommand command, String permNode) {
        if (command == null)
            return;
        registrar.registerCommand(command, permNode, false);
    }

    public static void addCompletionKey(String key, String completion) {
        List<String> completionList = completionMap.get(key);

        if(completionList == null) {
            completionList = new ArrayList<String>();
            completionMap.put(key, completionList);
        }

        completionList.add(completion);
    }

    public static List<String> getCompletionList(String key) {
        return completionMap.get(key);
    }


    public static void removeCompletionKey(String key, String completion) {
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


    private static void constructTree(CommandTreeNode treeNode, Map<CommandNode, Method> nodes) {
        for(Iterator<Map.Entry<CommandNode, Method>> it = nodes.entrySet().iterator(); it.hasNext();) {
            Map.Entry<CommandNode, Method> entry = it.next();
            if(entry.getKey().parentName().equals(treeNode.getAnnotation().permission())) {
                treeNode.addChild(new CommandTreeNode(entry.getKey(), entry.getValue()));
                it.remove();
            }
        }

        for(CommandTreeNode child : treeNode.getChildren()) {
            constructTree(child, nodes);
        }
    }

    private static ICommandRegistrar makeRegistrar() {
        if (ClassUtils.isBukkitLoaded()) { // Bukkit Compat takes precedence
            return new BukkitCommandRegistrar();
        } else if (Loader.isModLoaded("ForgeEssentials")) { // Then Forge Essentials
            return new ForgeEssentialsCommandRegistrar();
        } else { // Finally revert to Vanilla (Ew, Vanilla Minecraft)
            return new VanillaCommandRegistrar();
        }
    }
}
