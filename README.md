# 🛍️ Sistema de Gerenciamento de Compras de Vinhos

Esta é uma aplicação RESTful desenvolvida em Java com Spring Boot que permite o gerenciamento de compras de vinhos, incluindo funcionalidades como recomendação de produtos, identificação dos clientes mais fiéis e a maior compra por ano.

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data MongoDB
- MongoDB
- MapStruct
- Swagger (Springdoc OpenAPI)
- Docker / Docker Compose
- Lombok

## 📦 Funcionalidades

- Listar todas as compras agrupadas por cliente
- Retornar a maior compra em um determinado ano
- Obter recomendação de produto com base na recorrência por cliente
- Listar os Top 3 clientes mais fiéis (maior valor em compras)
- Documentação interativa com Swagger

---

## 🐳 Rodando com Docker

A aplicação utiliza `docker-compose` para subir o ambiente com MongoDB. Para iniciar, execute:

```bash
docker-compose up -d
