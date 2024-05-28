resource "aws_xray_group" "desafio_itau_xray" {
  group_name        = "desafio_itau"
  filter_expression = "responsetime > 5"

  insights_configuration {
    insights_enabled      = true
    notifications_enabled = true
  }
}