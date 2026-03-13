package com.example.apimonitor.config;

import com.example.apimonitor.model.MonitoredService;
import com.example.apimonitor.repository.MonitoredServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedMonitoredServices(MonitoredServiceRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            List<MonitoredService> services = List.of(
                    create("github_api", "https://api.github.com", "GitHub API", "Developer"),
                    create("gitlab_api", "https://gitlab.com/api/v4/projects", "GitLab API", "Developer"),
                    create("bitbucket_api", "https://api.bitbucket.org/2.0/repositories", "Bitbucket API", "Developer"),
                    create("npm_registry", "https://registry.npmjs.org", "npm Registry", "Package Registry"),
                    create("pypi", "https://pypi.org/pypi", "PyPI", "Package Registry"),
                    create("docker_hub", "https://hub.docker.com/v2/repositories/library", "Docker Hub", "Container"),
                    create("google", "https://www.google.com", "Google", "Search"),
                    create("bing", "https://www.bing.com", "Bing", "Search"),
                    create("cloudflare_dns", "https://1.1.1.1", "Cloudflare DNS", "Networking"),
                    create("opendns", "https://208.67.222.222", "OpenDNS", "Networking"),
                    create("stripe_api", "https://api.stripe.com/v1/charges", "Stripe API", "Payments"),
                    create("paypal_api", "https://api.paypal.com/v1/payments/payment", "PayPal API", "Payments"),
                    create("twilio_api", "https://api.twilio.com/2010-04-01/Accounts", "Twilio API", "Communication"),
                    create("sendgrid_api", "https://api.sendgrid.com/v3/user/account", "SendGrid API", "Email"),
                    create("mailgun_api", "https://api.mailgun.net/v3/domains", "Mailgun API", "Email"),
                    create("openweathermap", "https://api.openweathermap.org/data/2.5/weather", "OpenWeatherMap", "Weather"),
                    create("weatherapi", "https://api.weatherapi.com/v1/current.json", "WeatherAPI", "Weather"),
                    create("coingecko_api", "https://api.coingecko.com/api/v3/ping", "CoinGecko API", "Crypto"),
                    create("coinbase_api", "https://api.coinbase.com/v2/time", "Coinbase API", "Crypto"),
                    create("kraken_api", "https://api.kraken.com/0/public/Time", "Kraken API", "Crypto"),
                    create("slack_api", "https://slack.com/api/api.test", "Slack API", "Collaboration"),
                    create("discord_api", "https://discord.com/api/v10/gateway", "Discord API", "Collaboration"),
                    create("microsoft_graph", "https://graph.microsoft.com/v1.0/me", "Microsoft Graph", "Productivity"),
                    create("google_calendar", "https://www.googleapis.com/calendar/v3/users/me/calendarList", "Google Calendar API", "Productivity"),
                    create("github_status", "https://www.githubstatus.com/api/v2/status.json", "GitHub Status", "Status"),
                    create("cloudflare_status", "https://www.cloudflarestatus.com/api/v2/status.json", "Cloudflare Status", "Status"),
                    create("aws_status", "https://status.aws.amazon.com/rss/all.rss", "AWS Status RSS", "Cloud"),
                    create("azure_status", "https://status.azure.com/en-us/status", "Azure Status Page", "Cloud"),
                    create("gcp_status", "https://status.cloud.google.com/incidents.json", "GCP Status JSON", "Cloud"),
                    create("ipapi", "https://ipapi.co/json", "ipapi", "Geo"),
                    create("ipify", "https://api.ipify.org?format=json", "ipify", "Networking"),
                    create("postman_echo_get", "https://postman-echo.com/get", "Postman Echo GET", "Testing"),
                    create("postman_echo_delay", "https://postman-echo.com/delay/3", "Postman Echo Delay", "Testing"),
                    create("jsonplaceholder_posts", "https://jsonplaceholder.typicode.com/posts", "JSONPlaceholder Posts", "Testing"),
                    create("jsonplaceholder_comments", "https://jsonplaceholder.typicode.com/comments", "JSONPlaceholder Comments", "Testing"),
                    create("reqres_users", "https://reqres.in/api/users", "ReqRes Users", "Testing"),
                    create("agify_api", "https://api.agify.io?name=michael", "Agify", "Fun"),
                    create("genderize_api", "https://api.genderize.io?name=luc", "Genderize", "Fun"),
                    create("nationalize_api", "https://api.nationalize.io?name=nathaniel", "Nationalize", "Fun"),
                    create("cat_fact", "https://catfact.ninja/fact", "Cat Fact", "Fun"),
                    create("dog_ceo", "https://dog.ceo/api/breeds/image/random", "Dog CEO", "Fun"),
                    create("bored_api", "https://www.boredapi.com/api/activity", "Bored API", "Fun"),
                    create("icanhazip", "https://icanhazip.com", "icanhazip", "Networking"),
                    create("chuck_norris_jokes", "https://api.chucknorris.io/jokes/random", "Chuck Norris Jokes", "Fun"),
                    create("httpbin_get", "https://httpbin.org/get", "httpbin GET", "Testing"),
                    create("httpbin_status_200", "https://httpbin.org/status/200", "httpbin 200", "Testing"),
                    create("httpbin_status_500", "https://httpbin.org/status/500", "httpbin 500", "Testing"),
                    create("httpbin_delay", "https://httpbin.org/delay/2", "httpbin Delay", "Testing"),
                    create("spotify_api", "https://api.spotify.com/v1/search?q=artist:drake&type=track", "Spotify API", "Media"),
                    create("itunes_api", "https://itunes.apple.com/search?term=jack+johnson", "iTunes Search API", "Media")
            );

            repository.saveAll(services);
        };
    }

    private MonitoredService create(String name, String baseUrl, String displayName, String category) {
        MonitoredService service = new MonitoredService();
        service.setName(name);
        service.setBaseUrl(baseUrl);
        service.setDisplayName(displayName);
        service.setCategory(category);
        return service;
    }
}

