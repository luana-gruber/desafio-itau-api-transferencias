resource "aws_ecs_service" "ecs_desafio_itau" {
  name            = "ecs_desafio_itau"
  cluster         = aws_ecs_cluster.desafio_itau_ecs_cluster.id
  task_definition = aws_ecs_task_definition.desafio_itau_ecs_task_definition.arn
  desired_count   = 3
  iam_role        = aws_iam_role.desafio_itau_role.arn
  depends_on      = [aws_iam_role_policy.desafio_itau_policy]

  ordered_placement_strategy {
    type  = "binpack"
    field = "cpu"
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.desafio_itau_target_group.arn
    container_name   = "ecs_desafio_itau"
    container_port   = 8080
  }

  placement_constraints {
    type       = "memberOf"
    expression = "attribute:ecs.availability-zone in [sa-east-1a,sa-east-1b"
  }
}

resource "aws_ecs_service" "ecs_desafio_itau" {
  name    = "ecs_desafio_itau"
  cluster = aws_ecs_cluster.desafio_itau_ecs_cluster.id

}
