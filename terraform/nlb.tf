resource "aws_lb" "nlb_desafio_itau" {
  name               = "nlb-desafio-itau"
  internal           = false
  load_balancer_type = "network"
  subnets            = [for subnet in aws_subnet.desafio_itau_subnet : subnet.id]

  enable_deletion_protection = true

  access_logs {
    bucket  = aws_s3_bucket.nlb_desafio_itau_logs.id
    prefix  = "nlblogs"
    enabled = true
  }

  tags = {
    Environment = "development"
  }
}

resource "aws_lb_target_group" "desafio_itau_target_group" {
  name     = "tf-desafio-itau-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = aws_vpc.desafio_itau_vpc.id
}


