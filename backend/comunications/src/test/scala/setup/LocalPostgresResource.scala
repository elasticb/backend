package setup

import java.net.URLEncoder

import cats.effect.IO
import com.spotify.docker.client.DockerClient.ListContainersParam
import com.spotify.docker.client.messages.{ContainerConfig, HostConfig, PortBinding}
import com.spotify.docker.client.{DefaultDockerClient, DockerClient}
import play.api.libs.json.Json

object LocalPostgresResource {
  import scala.collection.JavaConverters._

  private[this] val elasticpgImageName = "uladzimirbaklan/elasticbot_pg:latest"
  private[this] val elasticpgDefaultPort = 5432
  private[this] val elasticpgExposePort = 65000

  private[this] val dockerClient: DockerClient =
    DefaultDockerClient
    .builder()
    .uri(DefaultDockerClient.DEFAULT_UNIX_ENDPOINT)
    .build()

  private[this] val hostConfig = HostConfig
    .builder()
    .portBindings(Map(s"$elasticpgDefaultPort/tcp" ->
      List(PortBinding.of("0.0.0.0", s"$elasticpgExposePort")).asJava).asJava)
    .build()

  private[this] val containerConfig = ContainerConfig
    .builder()
    .hostConfig(hostConfig)
    .image(elasticpgImageName)
    .exposedPorts(s"$elasticpgDefaultPort/tcp")
    .build()

  type ContainerId = String

  def acquireSingleContainer(dockerClient: DockerClient = dockerClient): IO[ContainerId] = {
    for {
      _ <- IO(dockerClient.pull(elasticpgImageName))
      containerId <- getOldOrCreateNewElasticMqContainer(dockerClient)
    } yield containerId
  }

  private[this] def getOldOrCreateNewElasticMqContainer(dockerClient: DockerClient): IO[ContainerId] = {
    findRunningElasticMqContainer(dockerClient).flatMap {
      case c :: _ => IO(c.id())
      case Nil =>
        IO(dockerClient.createContainer(containerConfig).id).map { containerId =>
          dockerClient.startContainer(containerId)
          containerId
        }
    }
  }

  private[this] def findRunningElasticMqContainer(dockerClient: DockerClient) = {
    val filter = Json.obj("ancestor" -> Json.arr(elasticpgImageName), "status" -> Json.arr("running"))
    val filterParam = new ListContainersParam("filters", URLEncoder.encode(filter.toString(), "UTF-8"))
    IO(dockerClient.listContainers(filterParam).asScala.toList)
  }
}
