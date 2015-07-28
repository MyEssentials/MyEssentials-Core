package myessentials.command;

import cpw.mods.fml.common.Loader;
import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.command.annotation.Command;
import myessentials.command.registrar.BukkitCommandRegistrar;
import myessentials.command.registrar.ForgeEssentialsCommandRegistrar;
import myessentials.command.registrar.ICommandRegistrar;
import myessentials.command.registrar.VanillaCommandRegistrar;
import myessentials.exception.CommandException;
import myessentials.utils.ClassUtils;
import net.minecraft.command.ICommandSender;

import java.lang.reflect.Method;
import java.util.*;

public class CommandManagerNew {

    /**
     * Registrar used to register any commands. Offers compatibility for Bukkit and ForgeEssentials
     */
    private static final ICommandRegistrar registrar = makeRegistrar();

    private static final List<CommandTree> commandTrees = new ArrayList<CommandTree>();

    public static final String ROOT_PERM_NODE = "ROOT";

    private CommandManagerNew() {
    }

    /**
     * It is enforced that the class has to contain ONE root command .
     */
    public static void registerCommands(Class clazz, String rootPerm, Localization local) {
        CommandTreeNode root = null;
        CommandTree commandTree = rootPerm == null ? null : getTree(rootPerm);

        Map<Command, Method> nodes = new HashMap<Command, Method>();

        for (final Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Command.class)) {
                if(isMethodValid(method)) {
                    Command command = method.getAnnotation(Command.class);
                    if (command.parentName().equals(ROOT_PERM_NODE)) {
                        if (commandTree == null) {
                            root = new CommandTreeNode(command, method);
                        } else {
                            throw new CommandException("Class " + clazz.getName() + " has more than one root command.");
                        }
                    } else {
                        nodes.put(command, method);
                    }
                } else {
                    MyEssentialsCore.instance.LOG.error("Method " + method.getName() + " from class " + clazz.getName() + " is not valid for command usage");
                }
            }
        }

        if(commandTree == null) {
            if (root == null) {
                throw new CommandException("Class " + clazz.getName() + " has no root command.");
            } else {
                commandTree = new CommandTree(root, local);
                commandTrees.add(commandTree);
            }
        }

        registrar.registerCommand(new CommandModel(commandTree.getRoot().getAnnotation(), commandTree.getRoot().getMethod()), commandTree.getRoot().getAnnotation().permission(), false);

        constructTree(commandTree.getRoot(), nodes);

        if(!nodes.isEmpty()) {
            for(Map.Entry<Command, Method> entry : nodes.entrySet()) {
                MyEssentialsCore.instance.LOG.error("Missing parent: " + entry.getKey().permission() + " |<-| " + entry.getKey().parentName());
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

    private static CommandTreeNode findNode(CommandTreeNode root, String perm) {
        if(root.getAnnotation().permission().equals(perm))
            return root;

        for(CommandTreeNode child : root.getChildren()) {
            CommandTreeNode foundNode = findNode(child, perm);
            if(foundNode != null)
                return foundNode;
        }
        return null;
    }

    private static void constructTree(CommandTreeNode root, Map<Command, Method> nodes) {
        int currentNodeNumber;
        do {
            currentNodeNumber = nodes.size();
            for (Iterator<Map.Entry<Command, Method>> it = nodes.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Command, Method> entry = it.next();

                CommandTreeNode parent = findNode(root, entry.getKey().parentName());

                if (parent != null) {
                    parent.addChild(new CommandTreeNode(parent, entry.getKey(), entry.getValue()));
                    if(!root.getLocal().hasLocalization(entry.getKey().permission() + ".help")) {
                        MyEssentialsCore.instance.LOG.error("Missing help: " + entry.getKey().permission() + ".help");
                    }
                    it.remove();
                }
            }
        } while(currentNodeNumber != nodes.size());
    }

    private static boolean isMethodValid(Method method) {
        if(!method.getReturnType().equals(CommandResponse.class))
            return false;

        if(method.getParameterCount() != 2)
            return false;

        if(!(method.getParameterTypes()[0].equals(ICommandSender.class) && method.getParameterTypes()[1].equals(List.class)))
            return false;

        return true;
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
