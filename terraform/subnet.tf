resource "aws_subnet" "desafio_itau_subnet" {
  vpc_id     = aws_vpc.desafio_itau_vpc.id
  cidr_block = "10.0.1.0/24"

  tags = {
    Name = "desafio-itau-subnet"
  }
}