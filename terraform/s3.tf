resource "aws_s3_bucket" "nlb_desafio_itau_logs" {
  bucket = "desafio-itau"

  tags = {
    Name        = "desafio-itau-bucket"
    Environment = "development"
  }
}