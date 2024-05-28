resource "aws_ecs_task_definition" "desafio_itau_ecs_task_definition" {
  family = "family-desafio-itau"
  container_definitions = jsonencode([
    {
      name      = "container-app-desafio-itau"
      image     = "nginx:latest"
      cpu       = 10
      memory    = 512
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]
    }
  ])

  volume {
    name      = "desafio-itau-container-storage"
    host_path = "/ecs/desafio-itau-container-storage"
  }

  placement_constraints {
    type       = "memberOf"
    expression = "attribute:ecs.availability-zone in [sa-east-2a, sa-east-2b]"
  }
}