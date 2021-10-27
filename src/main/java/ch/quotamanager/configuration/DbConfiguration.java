package ch.quotamanager.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class DbConfiguration  {
	
	final static Logger logger = LoggerFactory.getLogger(DbConfiguration.class);
	
	private static final ConnectionString CONNECTION_STRING = new ConnectionString("mongodb://localhost:12345/quotamanager-db");
	
	@Bean
	public MongoHolder mongoHolder() {
		return new MongoHolder();
	}

	public static class MongoHolder {

		private MongoClient mongoClient;
		private MongodExecutable mongodExecutable;
		private MongodProcess mongod;

		@PostConstruct
		public void init() throws IOException {
			final MongodStarter starter = MongodStarter.getDefaultInstance();

			final String bindIp = "localhost";
			final int port = 12345;
			final IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net(bindIp, port, Network.localhostIsIPv6()))
					.build();

			try {
				mongodExecutable = starter.prepare(mongodConfig);
				mongod = mongodExecutable.start();
				mongoClient = new MongoClient(bindIp, port);
				logger.info("Started embedded mongo..");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		@PreDestroy
		public final synchronized void destroy() {
			if (mongodExecutable != null) {
				logger.info("Stopping embedded mongo.");
				mongoClient.close();
				mongodExecutable.stop();
				mongod.stop();
				logger.info("Stopped embedded mongo.");
			}
		}

		public MongoClient getMongoClient() {
			return mongoClient;
		}
	}

	@Bean
	public MongoDatabase mongoDatabase(final MongoHolder mongoHolder) {
		return mongoHolder.getMongoClient().getDatabase(Objects.requireNonNull(CONNECTION_STRING.getDatabase()));
	}

}
