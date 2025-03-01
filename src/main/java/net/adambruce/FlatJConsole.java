package net.adambruce;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import sun.tools.jconsole.JConsole;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlatJConsole {

    private static final String ARG_NAME = "-theme";
    private static final String ENV_NAME = "JCONSOLE_THEME";

    private static final String FLAT_LIGHT_LAF_THEME_NAME = "FlatLightLaf";
    private static final String FLAT_DARK_LAF_THEME_NAME = "FlatDarkLaf";
    private static final String FLAT_MAC_LIGHT_LAF_THEME_NAME = "FlatMacLightLaf";
    private static final String FLAT_MAC_DARK_LAF_THEME_NAME = "FlatMacDarkLaf";
    private static final String FLAT_INTELLIJ_LAF_THEME_NAME = "FlatIntelliJLaf";
    private static final String FLAT_DARCULA_LAF_THEME_NAME = "FlatDarculaLaf";


    public static void main(String[] args) {
        // Prevent flatlaf native library unpacking / loading
        System.setProperty("flatlaf.useNativeLibrary", "false");

        List<String> argsList = Arrays.asList(args);
        if (argsList.contains("-help") || argsList.contains("-h") || argsList.contains("-?")) {
            printThemeHelp();
        }

        initTheme(argsList);
        JConsole.main(stripInvalidArgs(argsList).toArray(String[]::new));
    }

    private static List<String> stripInvalidArgs(List<String> args) {
        return args.stream()
                .filter(arg -> !arg.startsWith(ARG_NAME))
                .collect(Collectors.toList());
    }

    private static void initTheme(List<String> args) {
        String envTheme = System.getenv().getOrDefault(ENV_NAME, null);

        Optional<String> argTuple = args.stream().filter(arg -> arg.startsWith(ARG_NAME)).findFirst();

        Optional<String> theme = argTuple.map(tup -> tup.split("=").length > 1 ? tup.split("=")[1] : null);

        // If they've used -theme but the value is bad, print help
        if (argTuple.isPresent() && theme.isEmpty()) {
            printThemeHelp();
            System.exit(0);
        }

        String themeName = theme.orElse(envTheme);
        if (themeName == null) {
            return;
        }

        // Ensure that JConsole doesn't cause any funny business (see JConsole.java:59)
        System.setProperty("swing.defaultlaf", "something");

        switch (themeName) {
            case FLAT_LIGHT_LAF_THEME_NAME:
                FlatLightLaf.setup();
                break;
            case FLAT_DARK_LAF_THEME_NAME:
                FlatDarkLaf.setup();
                break;
            case FLAT_MAC_LIGHT_LAF_THEME_NAME:
                FlatMacLightLaf.setup();
                break;
            case FLAT_MAC_DARK_LAF_THEME_NAME:
                FlatMacDarkLaf.setup();
                break;
            case FLAT_INTELLIJ_LAF_THEME_NAME:
                FlatIntelliJLaf.setup();
                break;
            case FLAT_DARCULA_LAF_THEME_NAME:
                FlatDarculaLaf.setup();
                break;
            default:
                printThemeHelp();
                System.exit(0);
        }
    }

    private static void printThemeHelp() {
        System.out.println("To set the theme using commandline arguments:");
        System.out.println("    java -jar flat-jconsole.jar -theme=<THEME> [ARGS]\n");
        System.out.println("To set the theme using environment variables:");
        System.out.println("    JCONSOLE_THEME=<THEME> java -jar flat-jconsole.jar [ARGS]\n");
        System.out.println("Available themes:");
        System.out.println("    " + FLAT_LIGHT_LAF_THEME_NAME);
        System.out.println("    " + FLAT_DARK_LAF_THEME_NAME);
        System.out.println("    " + FLAT_MAC_LIGHT_LAF_THEME_NAME);
        System.out.println("    " + FLAT_MAC_DARK_LAF_THEME_NAME);
        System.out.println("    " + FLAT_INTELLIJ_LAF_THEME_NAME);
        System.out.println("    " + FLAT_DARCULA_LAF_THEME_NAME);
        System.out.println();
    }
}