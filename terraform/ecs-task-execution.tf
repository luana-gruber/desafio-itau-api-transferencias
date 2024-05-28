data "aws_ecs_task_execution" "desafio_itau_task_execution" {
  cluster         = aws_ecs_cluster.desafio_itau_ecs_cluster.id
  task_definition = aws_ecs_task_definition.desafio_itau_ecs_task_definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = aws_subnet.desafio_itau_subnet[*].id
    security_groups  = [aws_security_group.desafio_itau_security_group.id]
    assign_public_ip = false
  }
}