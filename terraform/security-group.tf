resource "aws_security_group" "desafio_itau_security_group" {
  name        = "desafio_itau_sg"
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = aws_vpc.desafio_itau_vpc.id

  tags = {
    Name = "desafio_itau_sg"
  }
}

resource "aws_vpc_security_group_ingress_rule" "desafio_itau_tls_ipv4" {
  security_group_id = aws_security_group.desafio_itau_security_group.id
  cidr_ipv4         = aws_vpc.desafio_itau_vpc.cidr_block
  from_port         = 443
  ip_protocol       = "tcp"
  to_port           = 443
}

resource "aws_vpc_security_group_egress_rule" "desafio_itau_traffic_ipv4" {
  security_group_id = aws_security_group.desafio_itau_security_group.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}


resource "aws_vpc_security_group_ingress_rule" "desafio_itau_http_ipv4" {
  security_group_id = aws_security_group.desafio_itau_security_group.id
  cidr_ipv4         = aws_vpc.desafio_itau_vpc.cidr_block
  from_port         = 8080
  ip_protocol       = "udp"
  to_port           = 8080
}

resource "aws_vpc_security_group_egress_rule" "desafio_itau_http_ipv4" {
  security_group_id = aws_security_group.desafio_itau_security_group.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}
