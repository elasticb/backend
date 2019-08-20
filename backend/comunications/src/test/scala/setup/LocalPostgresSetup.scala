package setup

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, BeforeAndAfterEach}

trait LocalPostgresSetup extends AsyncFlatSpec with BeforeAndAfterAll with BeforeAndAfterEach with DatabaseObjectGenerator {
  import com.elasticbot.db.ExtendedPostgresDriver.api._
  private[this] lazy val config = ConfigFactory.defaultApplication()

  override def beforeAll() = {
    super.beforeAll()
    LocalPostgresResource.acquireSingleContainer().unsafeRunSync()
  }

  override def afterAll() = {
    super.afterAll()
    teardownDatabase()
  }

  override def beforeEach() = {
    super.beforeEach()
  }

  override def afterEach() = {
    super.afterEach()
    teardownDatabase().unsafeRunSync()
  }

  private[this] val teardownData = DBIO.seq (
    sqlu"TRUNCATE TABLE notification CASCADE",
    sqlu"TRUNCATE TABLE dead_questions CASCADE ",
    sqlu"TRUNCATE TABLE answer CASCADE",
    sqlu"TRUNCATE TABLE question CASCADE",
    sqlu"TRUNCATE TABLE service_user CASCADE"
  )

  private[this] def teardownDatabase() = {
    IO.fromFuture(IO(db.run(teardownData)))
  }
}
