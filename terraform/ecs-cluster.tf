resource "aws_ecs_cluster" "desafio_itau_ecs_cluster" {
  name = "desafio_itau_ecs_cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}