package adrianrafo.processor.config

final case class PostgresConfig(
  driver: String,
  jdbcUrl: String,
  user: String,
  password: String
)

final case class Config(postgres: PostgresConfig)
