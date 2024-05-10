package ru.haidarov.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.haidarov.hw.config.LocaleConfig;
import ru.haidarov.hw.config.SecretInfoAvailabilityChecker;

@ShellComponent
@RequiredArgsConstructor
public class TestAppCommands {

    private final LocaleConfig localeConfig;

    private final CommandLineRunner commandLineRunner;

    private final SecretInfoAvailabilityChecker secretInfoAvailabilityChecker;

    @ShellMethod("Run the app (and specify locale - optional)")
    public void run(@ShellOption(defaultValue = "") String locale) throws Exception {
        if (!locale.isEmpty()) {
            localeConfig.setLocale(locale);
        }
        commandLineRunner.run();
    }

    @ShellMethod(value = "Author's info", key = {"info", "i", "cb"})
    public String info() {
        return "Created by Ivan Haidarov";
    }

    @ShellMethod(value = "Author's secret info", key = {"secret", "s"})
    @ShellMethodAvailability(value = "isSecretInfoAvailable")
    public String secret() {
        return "Ivan Haidarov likes cats";
    }

    public Availability isSecretInfoAvailable(){
        if (secretInfoAvailabilityChecker.isSecretInfoAvailable())
            return Availability.available();

        return Availability.unavailable("secret info is unavailable");
    }
}
