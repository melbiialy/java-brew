package http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Banner utility for JavaBrew HTTP Server
 * Designed to work as a library module without conflicting with host application
 */
public class Banner {

    private static final String DEFAULT_BANNER =
            "\n" +
                    "     ██╗ █████╗ ██╗   ██╗ █████╗ ██████╗ ██████╗ ███████╗██╗    ██╗\n" +
                    "     ██║██╔══██╗██║   ██║██╔══██╗██╔══██╗██╔══██╗██╔════╝██║    ██║\n" +
                    "     ██║███████║██║   ██║███████║██████╔╝██████╔╝█████╗  ██║ █╗ ██║\n" +
                    "██   ██║██╔══██║╚██╗ ██╔╝██╔══██║██╔══██╗██╔══██╗██╔══╝  ██║███╗██║\n" +
                    "╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║██████╔╝██║  ██║███████╗╚███╔███╔╝\n" +
                    " ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ \n" +
                    "\n" +
                    "  :: JavaBrew ::        (v${version})\n" +
                    "  Brewing your HTTP requests with Java excellence\n";

    private static final String MODULE_PROPERTIES = "javabrew.properties";
    private static final String BANNER_FILE = "javabrew-banner.txt";

    private final BannerConfig config;


    public Banner() {
        this(new BannerConfig());
    }


    public Banner(BannerConfig config) {
        this.config = config;
        loadModuleProperties();
    }


    public void print() {
        if (!config.isEnabled()) {
            return;
        }

        String banner = loadBanner();
        banner = replacePlaceholders(banner);
        System.out.println(banner);
    }


    private void loadModuleProperties() {
        try (InputStream is = Banner.class.getClassLoader()
                .getResourceAsStream(MODULE_PROPERTIES)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                config.mergeFromProperties(props);
            }
        } catch (IOException e) {
            // Continue with defaults
        }
    }


    private String loadBanner() {
        String location = config.getBannerLocation();

        try (InputStream is = Banner.class.getClassLoader()
                .getResourceAsStream(location)) {
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (IOException e) {
            if (!BANNER_FILE.equals(location)) {
                return loadBannerFromDefault();
            }
        }

        return DEFAULT_BANNER;
    }

    private String loadBannerFromDefault() {
        try (InputStream is = Banner.class.getClassLoader()
                .getResourceAsStream(BANNER_FILE)) {
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (IOException e) {
            // Fall through
        }
        return DEFAULT_BANNER;
    }

    /**
     * Replace placeholders in banner
     */
    private String replacePlaceholders(String banner) {
        return banner
                .replace("${version}", config.getVersion())
                .replace("${env}", config.getEnvironment());
    }

    /**
     * Configuration class for banner
     */
    public static class BannerConfig {
        private boolean enabled = true;
        private String bannerLocation = BANNER_FILE;
        private String version = "1.0.0";
        private String environment = "";

        public BannerConfig() {
            // Check system properties for overrides
            String mode = System.getProperty("javabrew.banner.mode");
            if (mode != null) {
                this.enabled = !"off".equalsIgnoreCase(mode);
            }
        }

        public BannerConfig enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public BannerConfig bannerLocation(String location) {
            this.bannerLocation = location;
            return this;
        }

        public BannerConfig version(String version) {
            this.version = version;
            return this;
        }

        public BannerConfig environment(String environment) {
            this.environment = environment;
            return this;
        }

        void mergeFromProperties(Properties props) {
            if (props.containsKey("javabrew.banner.mode")) {
                this.enabled = !"off".equalsIgnoreCase(
                        props.getProperty("javabrew.banner.mode"));
            }
            if (props.containsKey("javabrew.banner.location")) {
                this.bannerLocation = props.getProperty("javabrew.banner.location");
            }
            if (props.containsKey("javabrew.version")) {
                this.version = props.getProperty("javabrew.version");
            }
            if (props.containsKey("javabrew.environment")) {
                this.environment = props.getProperty("javabrew.environment");
            }
        }

        public boolean isEnabled() { return enabled; }
        public String getBannerLocation() { return bannerLocation; }
        public String getVersion() { return version; }
        public String getEnvironment() { return environment; }
    }
}


