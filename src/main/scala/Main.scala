import java.util.Properties

import cats.effect._
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import org.apache.kafka.streams.kstream.{ Consumed, Produced }
import org.apache.kafka.streams.scala.{ Serdes, StreamsBuilder }
import org.apache.kafka.streams.{ KafkaStreams, StreamsConfig }

/**
  * This version extending IOApp crashes at runtime
  */
object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    Resource
      .make[IO, KafkaStreams](IO {
        val props = new Properties()
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-app")
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092")
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")

        val b = new StreamsBuilder()

        b.stream[String, String]("test-source")(Consumed.`with`(Serdes.String, Serdes.String))
          .to("test-sink")(Produced.`with`(Serdes.String, Serdes.String))

        new KafkaStreams(b.build(), props)
      })(
        s => IO(s.close())
      )
      .use(
        s => IO.async[Unit](_ => s.start())
      )
      .redeemWith(
        _ => IO.pure(ExitCode.Error),
        _ => IO.pure(ExitCode.Success)
      )
}

/*

// Extending IOApp.WithContext and changing the execution context
// from global the problem doesn't happen

object Main extends IOApp.WithContext {

  def run(args: List[String]): IO[ExitCode] =
    Resource.make ... Same as above

  protected def executionContextResource: Resource[SyncIO, ExecutionContext] =
    Resource.make(SyncIO(ExecutionContext.fromExecutorService(new ForkJoinPool())))(
      pool => SyncIO {
        pool.shutdown()
        pool.awaitTermination(10, TimeUnit.SECONDS)
        ()
      }
    ).asInstanceOf[Resource[SyncIO, ExecutionContext]]
}*/
