resource "aws_api_gateway_rest_api" "gateway_desafio_itau" {
  body = jsonencode({
    openapi = "3.0.1"
    info = {
      title   = "gateway_desafio_itau"
      version = "1.0"
    }
    paths = {
      "/tranferencias" = {
        get = {
          x-amazon-apigateway-integration = {
            httpMethod           = "POST"
            payloadFormatVersion = "1.0"
            type                 = "HTTP_PROXY"
            uri                  = "https://ip-ranges.amazonaws.com/ip-ranges.json"
          }
        }
      }
    }
  })

  name = "gateway_desafio_itau"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

resource "aws_api_gateway_deployment" "gateway_desafio_itau_deployment" {
  rest_api_id = aws_api_gateway_rest_api.gateway_desafio_itau.id

  triggers = {
    redeployment = sha1(jsonencode(aws_api_gateway_rest_api.gateway_desafio_itau.body))
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "desafio_itau_gateway_stage" {
  deployment_id = aws_api_gateway_deployment.gateway_desafio_itau_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.gateway_desafio_itau.id
  stage_name    = "desafio_itau_gateway_stage"
}