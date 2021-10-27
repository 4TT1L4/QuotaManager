package ch.quotamanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String REGULAR_USER = "REGULAR";
	private static final String CUSTOMER_SUPPORT_STAFF = "CUSTOMER_SUPPORT_STAFF";

	@Override
	public void configure(final HttpSecurity httpSecurity) throws Exception {

		final Map<String, String> roleMapping = Map.ofEntries(
				Map.entry("/setSubscription/**", CUSTOMER_SUPPORT_STAFF),
				Map.entry("/usage/**", REGULAR_USER),
				Map.entry("/checkUpload/**", REGULAR_USER)
				);

		final String[] patterns = roleMapping.keySet().toArray(new String[0]);
		final HttpSecurity security = httpSecurity.csrf().disable()
				.requestMatchers().antMatchers(patterns)
				.and()
				.anonymous().disable()
				.httpBasic()
				.and();
		configureRoles(security, roleMapping);
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		return new InMemoryUserDetailsManager(users());
	}

	@Bean
	public List<UserDetails> users() {
		return List.of(
				new User("5e15cbcfef537d543edeb100", "{noop}password", List.of(new SimpleGrantedAuthority(REGULAR_USER))), // password is 'password'
				new User("5e15cbcfef537d543edeb101", "{noop}password", List.of(new SimpleGrantedAuthority(REGULAR_USER))),
				new User("5e15cbcfef537d543edeb102", "{noop}password", List.of(new SimpleGrantedAuthority(REGULAR_USER))),
				new User("5e15cbcfef537d543edeb103", "{noop}password", List.of(new SimpleGrantedAuthority(CUSTOMER_SUPPORT_STAFF))),
				new User("5e15cbcfef537d543edeb104", "{noop}password", List.of(new SimpleGrantedAuthority(REGULAR_USER))),
				new User("5e15cbcfef537d543edeb105", "{noop}password", List.of(new SimpleGrantedAuthority(REGULAR_USER)))
				);

	}

	private static void configureRoles(final HttpSecurity security, final Map<String, String> roleMapping) throws Exception {
		final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = security.authorizeRequests();
		for (final Map.Entry<String, String> entry : roleMapping.entrySet()) {
			expressionInterceptUrlRegistry.antMatchers(entry.getKey()).hasAuthority(entry.getValue());
		}
	}
}
